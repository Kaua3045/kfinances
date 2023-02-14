package com.kaua.finances.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {

        return new JedisConnectionFactory(new RedisStandaloneConfiguration(
                redisHost,
                Integer.parseInt(redisPort)
        ));
    }
}
