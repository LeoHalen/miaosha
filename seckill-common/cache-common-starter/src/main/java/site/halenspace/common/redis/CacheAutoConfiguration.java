package site.halenspace.common.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import site.halenspace.common.core.constant.SymbolPool;

import java.util.StringJoiner;

/**
 * @Author Zg.Li · 2020/8/31
 */
@Import({
        RedisAutoConfiguration.class
})
@EnableCaching
@Slf4j
public class CacheAutoConfiguration {


    /**
     * 自定义key生成规则
     * rule:
     *  className + ":" + methodName + ":" + paramArg1 + ":" + ... + ":" + paramArgn
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringJoiner keyJoiner = new StringJoiner(SymbolPool.COLON);
            keyJoiner.add(target.getClass().getName());
            keyJoiner.add(method.getName());
            for (Object param : params) {
                keyJoiner.add(param.toString());
            }

            return keyJoiner.toString();
        };
    }
}
