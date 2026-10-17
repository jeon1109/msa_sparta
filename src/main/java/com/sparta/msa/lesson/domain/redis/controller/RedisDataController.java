package com.sparta.msa.lesson.domain.redis.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/redis-demo")
public class RedisDataController {

  private final StringRedisTemplate redis;

  // Isolated lecture keys: never read or delete session/cache data.
  @PostMapping
  public ApiResponse<Map<String, Object>> demonstrate(
      @AuthenticationPrincipal CustomUserDetails user) {
    String key = "lesson:demo:" + user.getUserId() + ":";
    redis.opsForValue().set(key + "string", "Hello World", Duration.ofHours(1));
    Long count = redis.opsForValue().increment(key + "counter");
    redis.delete(
        List.of(
            key + "tasks",
            key + "participants",
            key + "event1",
            key + "event2",
            key + "profile",
            key + "leaderboard"));
    redis.opsForList().leftPushAll(key + "tasks", "Task1", "Task2", "Task3");
    String first = redis.opsForList().rightPop(key + "tasks");
    redis.opsForSet().add(key + "participants", "User1", "User2", "User1");
    redis.opsForSet().add(key + "event1", "User1", "User2", "User4");
    redis.opsForSet().add(key + "event2", "User2", "User3", "User4");
    redis.opsForHash().putAll(key + "profile", Map.of("name", "John Doe", "city", "Seoul"));
    redis.opsForZSet().add(key + "leaderboard", "Player1", 1500);
    redis.opsForZSet().add(key + "leaderboard", "Player2", 2000);
    redis.opsForZSet().add(key + "leaderboard", "Player3", 1800);
    redis.opsForZSet().incrementScore(key + "leaderboard", "Player1", 100);
    for (String suffix :
        List.of("counter", "tasks", "participants", "event1", "event2", "profile", "leaderboard")) {
      redis.expire(key + suffix, Duration.ofHours(1));
    }
    Map<String, Object> result = new LinkedHashMap<>();
    result.put("string", redis.opsForValue().get(key + "string"));
    result.put("counter", count);
    result.put("firstTask", first);
    result.put("remainingTasks", redis.opsForList().range(key + "tasks", 0, -1));
    result.put("participants", redis.opsForSet().members(key + "participants"));
    result.put("intersection", redis.opsForSet().intersect(key + "event1", key + "event2"));
    result.put("union", redis.opsForSet().union(key + "event1", key + "event2"));
    result.put("difference", redis.opsForSet().difference(key + "event1", key + "event2"));
    result.put("profile", redis.opsForHash().entries(key + "profile"));
    result.put("topPlayers", redis.opsForZSet().reverseRange(key + "leaderboard", 0, 1));
    result.put("playersInRange", redis.opsForZSet().rangeByScore(key + "leaderboard", 1600, 1900));
    result.put("player3Rank", redis.opsForZSet().rank(key + "leaderboard", "Player3"));
    return ApiResponse.ok(result);
  }
}
