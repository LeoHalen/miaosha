package site.halenspace.common.redis.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author Zg.Li · 2020/8/31
 */
@Setter
@Getter
@ConfigurationProperties(prefix = CacheManagerProperties.PREFIX)
public class CacheManagerProperties {

    public static final String PREFIX = "seckill.cache";

    private List<CacheConfig> configs;

    @Setter
    @Getter
    public static class CacheConfig {

        /** redis key */
        private String key;

        /** 过期时间（seconds） */
        private long expire = 300;
    }
}
