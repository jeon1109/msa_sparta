package com.sparta.msa.lesson.domain.ai.service;

import com.sparta.msa.lesson.common.exception.*;
import com.sparta.msa.lesson.domain.ai.dto.*;
import java.time.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {
  private final ObjectProvider<ChatClient.Builder> builders;
  private final StringRedisTemplate redis;

  @Value("${lesson.ai.daily-limit:10}")
  private int dailyLimit;

  // Increment and TTL are atomic, including on the first request.
  private static final DefaultRedisScript<Long> COUNT =
      new DefaultRedisScript<>(
          "local n=redis.call('INCR',KEYS[1]); if n==1 then redis.call('EXPIRE',KEYS[1],ARGV[1]);"
              + " end; return n",
          Long.class);

  private ChatClient client(Long userId) {
    var builder = builders.getIfAvailable();
    if (builder == null) throw new DomainException(DomainExceptionCode.AI_SERVICE_UNAVAILABLE);
    var now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
    long ttl =
        Duration.between(now, now.toLocalDate().plusDays(1).atStartOfDay(now.getZone()))
                .getSeconds()
            + 60;
    Long count =
        redis.execute(
            COUNT,
            List.of("lesson:ai:usage:" + userId + ":" + now.toLocalDate()),
            Long.toString(ttl));
    if (count == null || count > dailyLimit)
      throw new DomainException(DomainExceptionCode.AI_QUOTA_EXCEEDED);
    return builder.build();
  }

  public ChatResponse chat(Long userId, String text, boolean context) {
    try {
      var prompt = client(userId).prompt().user(text);
      if (context)
        prompt = prompt.system("당신은 쇼핑 도우미입니다. 상품과 주문에 대해 명확하고 간결하게 답변하세요. 모르는 정보는 추측하지 마세요.");
      String result = prompt.call().content();
      if (result == null || result.isBlank())
        throw new DomainException(DomainExceptionCode.AI_RESPONSE_ERROR);
      return new ChatResponse(result);
    } catch (DomainException e) {
      throw e;
    } catch (Exception e) {
      throw new DomainException(DomainExceptionCode.AI_RESPONSE_ERROR);
    }
  }

  public String generate(
      Long userId, String system, String text, double temperature, int maxTokens) {
    try {
      return client(userId)
          .prompt()
          .system(system)
          .user(text)
          .options(ChatOptions.builder().temperature(temperature).maxTokens(maxTokens).build())
          .call()
          .content();
    } catch (DomainException e) {
      throw e;
    } catch (Exception e) {
      throw new DomainException(DomainExceptionCode.AI_RESPONSE_ERROR);
    }
  }

  public ChatResponse cached(Long userId, String message) {
    String hash;
    try {
      hash =
          java.util.HexFormat.of()
              .formatHex(
                  java.security.MessageDigest.getInstance("SHA-256")
                      .digest(message.getBytes(java.nio.charset.StandardCharsets.UTF_8)));
    } catch (java.security.NoSuchAlgorithmException e) {
      throw new IllegalStateException(e);
    }
    String key = "lesson:ai:cache:" + userId + ":" + hash;
    String cached = redis.opsForValue().get(key);
    if (cached != null) return new ChatResponse(cached);
    ChatResponse result = chat(userId, message, false);
    redis.opsForValue().set(key, result.message(), Duration.ofHours(1));
    return result;
  }

  public ProductAnalysisResponse analyze(Long userId, String review) {
    try {
      return client(userId)
          .prompt()
          .user(
              "다음 상품 리뷰의 sentiment(positive/neutral/negative), score(0~10), summary를 분석하세요: "
                  + review)
          .call()
          .entity(ProductAnalysisResponse.class);
    } catch (DomainException e) {
      throw e;
    } catch (Exception e) {
      throw new DomainException(DomainExceptionCode.AI_RESPONSE_ERROR);
    }
  }
}
