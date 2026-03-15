package com.pfm.backend.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
public class RedisConfig {
	
	@Bean
	public RedisCacheConfiguration cacheConfiguration() {
		//This means cached data expires after 10 min
		return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10));
	}
}
