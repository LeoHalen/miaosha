package site.halenspace.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import site.halenspace.common.core.constant.SymbolPool;
import site.halenspace.common.redis.properties.CacheManagerProperties;
import site.halenspace.common.redis.serializer.RedisObjectSerializer;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @Author Zg.Li · 2020/8/31
 */
@ConditionalOnBean(RedisConnectionFactory.class)
@ConditionalOnProperty(prefix = CacheManagerProperties.PREFIX, name = "type", havingValue = "redis")
@EnableConfigurationProperties({CacheManagerProperties.class})
@Slf4j
public class RedisAutoConfiguration {

    @Autowired
    private CacheManagerProperties cacheManagerProperties;


    @PostConstruct
    public void init() {
        log.info("Common 'Redis-Cache-Manager': enabled");
    }

    @Bean
    public RedisSerializer<String> redisKeySerializer() {
        return RedisSerializer.string();
    }

    @Bean
    public RedisSerializer<Object> redisValueSerializer() {
//        return RedisSerializer.java();
        // 替换使用Jackson2JsonRedisSerializer序列化器
//        return RedisSerializer.json();
        // 自定义Jackson2序列化器
        return new RedisObjectSerializer();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory,
                                                       RedisSerializer<String> redisKeySerializer,
                                                       RedisSerializer<Object> redisValueSerializer) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setValueSerializer(redisValueSerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);
        redisTemplate.setHashValueSerializer(redisValueSerializer);

        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(LettuceConnectionFactory factory,
                                     RedisSerializer<String> redisKeySerializer,
                                     RedisSerializer<Object> redisValueSerializer) {
        // 获取默认缓存配置
        RedisCacheConfiguration defaultCacheConfiguration =
                getDefaultCacheConfiguration(redisKeySerializer, redisValueSerializer).entryTtl(Duration.ofSeconds(3600));
        // 获取自定义缓存配置
        Map<String, RedisCacheConfiguration> customCacheConfigurations =
                getCustomCacheConfigurations(redisKeySerializer, redisValueSerializer);

        return RedisCacheManager.builder(factory)
                .cacheDefaults(defaultCacheConfiguration)
                .withInitialCacheConfigurations(customCacheConfigurations)
                .build();
    }

    /**
     * 获取默认的redisCache配置
     */
    private RedisCacheConfiguration getDefaultCacheConfiguration(RedisSerializer<String> redisKeySerializer,
                                                                 RedisSerializer<Object> redisValueSerializer) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .computePrefixWith(cacheKeyPrefix -> "cache".concat(SymbolPool.COLON).concat(cacheKeyPrefix).concat(SymbolPool.COLON))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisKeySerializer))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisValueSerializer));
    }

    /**
     * 获取自定义的redisCache配置
     */
    private Map<String, RedisCacheConfiguration> getCustomCacheConfigurations(RedisSerializer<String> redisKeySerializer,
                                                                              RedisSerializer<Object> redisValueSerializer) {
        //自定义的缓存过期时间配置
        int configSize = cacheManagerProperties.getConfigs() == null ? 0 : cacheManagerProperties.getConfigs().size();
        Map<String, RedisCacheConfiguration> redisCacheConfigurations = new HashMap<>(configSize);
        if (configSize > 0) {
            cacheManagerProperties.getConfigs().forEach(cacheConfig -> {
                RedisCacheConfiguration conf = getDefaultCacheConfiguration(redisKeySerializer, redisValueSerializer)
                        .entryTtl(Duration.ofSeconds(cacheConfig.getExpire()));
                redisCacheConfigurations.put(cacheConfig.getKey(), conf);
            });
        }

        return redisCacheConfigurations;
    }
}
