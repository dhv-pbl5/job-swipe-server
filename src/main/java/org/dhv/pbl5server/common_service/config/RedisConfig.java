package org.dhv.pbl5server.common_service.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final Environment env;
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.jedis.pool.max-active}")
    private int maxConnection;
    @Value("${spring.data.redis.jedis.pool.max-idle}")
    private int maxConnectionIdle;
    @Value("${spring.data.redis.jedis.pool.min-idle}")
    private int minConnectionIdle;
    @Value("${spring.data.redis.jedis.pool.max-wait}")
    private int maxConnectionWait;
    @Value("${spring.profiles.active}")
    private String profile;

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        if (profile.equals(CommonConstant.PROD_PROFILE)) {
            configuration.setPassword(env.getProperty("spring.data.redis.password"));
            configuration.setUsername(env.getProperty("spring.data.redis.username"));
            return new JedisConnectionFactory(
                configuration,
                getJedisClientConfiguration(true)
            );
        }
        return new JedisConnectionFactory(configuration, getJedisClientConfiguration(false));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setExposeConnection(true);
        template.setEnableTransactionSupport(true);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }

    private JedisClientConfiguration getJedisClientConfiguration(boolean useSsl) {
        JedisClientConfiguration.JedisClientConfigurationBuilder builder = JedisClientConfiguration.builder();
        GenericObjectPoolConfig<?> genericObjectPoolConfig = new GenericObjectPoolConfig<>();
        genericObjectPoolConfig.setMaxTotal(maxConnection);
        genericObjectPoolConfig.setMaxIdle(maxConnectionIdle);
        genericObjectPoolConfig.setMinIdle(minConnectionIdle);
        genericObjectPoolConfig.setMaxWait(Duration.ofSeconds(maxConnectionWait));
        return useSsl
            ? builder.usePooling().poolConfig(genericObjectPoolConfig).and().useSsl().build()
            : builder.usePooling().poolConfig(genericObjectPoolConfig).build();
    }
}
