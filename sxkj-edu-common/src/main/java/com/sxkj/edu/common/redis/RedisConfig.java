package com.sxkj.edu.common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


@Data
@Configuration
@ConditionalOnClass(RedisConnectionFactory.class)
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private String port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private int timeout;
    @Value("${spring.redis.time}")
    String cacheTime;

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        // key采用String的序列化方式
        redisTemplate.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        /* 默认配置， 默认超时时间为24h */
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration
                .ofSeconds(Integer.parseInt(cacheTime))).disableCachingNullValues();

        RedisCacheManager cacheManager = RedisCacheManager
                .builder(RedisCacheWriter.nonLockingRedisCacheWriter(factory))
                .cacheDefaults(defaultCacheConfig).build();

        return cacheManager;
    }


//    /**
//     * 创建消息监听容器
//     *
//     * @param redisConnectionFactory
//     * @param messageListenerAdapter
//     * @return
//     */
//    @Bean
//    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory, MessageListenerAdapter messageListenerAdapter) {
//        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
//        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
//        PatternTopic session_message = new PatternTopic("SESSION_MESSAGE");
//        redisMessageListenerContainer.addMessageListener(messageListenerAdapter,session_message);
//        //序列化对象（特别注意：发布的时候需要设置序列化；订阅方也需要设置序列化）
//        Jackson2JsonRedisSerializer seria = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        seria.setObjectMapper(objectMapper);
//        redisMessageListenerContainer.setTopicSerializer(seria);
//        return redisMessageListenerContainer;
//    }


    @Bean
    @ConditionalOnClass(RedissonClient.class)
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://"+host+":"+port)
                .setPassword(password)
                .setConnectionMinimumIdleSize(10)
                .setConnectionPoolSize(100)
                .setIdleConnectionTimeout(600000)
                .setSubscriptionConnectionMinimumIdleSize(10)
                .setSubscriptionConnectionPoolSize(100)
                .setTimeout(timeout);

        config.setCodec(new StringCodec());
        config.setThreads(5);
        config.setNettyThreads(5);

        RedissonClient client = Redisson.create(config);

        return client;
    }

    @Bean
    @ConditionalOnClass(RedisConnectionFactory.class)
    public RedisCache redisCache(RedisTemplate redisTemplate) {
        return new RedisCache(redisTemplate);
    }

//    @Bean
//    @ConditionalOnClass(RedissonClient.class)
//    public RedisLock redisLock(RedissonClient redissonClient) {
//        return new RedisLock(redissonClient);
//    }

}
