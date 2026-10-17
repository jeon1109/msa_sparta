package com.sparta.msa.lesson.common.util;

import org.springframework.context.annotation.*;
import org.springframework.data.redis.serializer.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.session.data.redis.*;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisIndexedHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

@Configuration
@EnableRedisIndexedHttpSession(redisNamespace = "lesson:session")
public class SessionConfig {
  @Bean
  public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
    return new JdkSerializationRedisSerializer(getClass().getClassLoader());
  }

  @Bean
  public SessionRegistry sessionRegistry(RedisIndexedSessionRepository repository) {
    return new SpringSessionBackedSessionRegistry<>(repository);
  }
}
