package fpt.is.bnk.fptis_platform.configuration.intergration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * Admin 8/16/2025
 **/
@Configuration
public class RedisConfiguration {

    @Value("${redis.host}")
    private String redisHost;

    @Value("${redis.port}")
    private int redisPort;

    @Value("${redis.password}")
    private String redisPassword;

    // =====================================================================
    // Kết nối tới Redis
    //    - Định nghĩa RedisConnectionFactory với host, port, password
    //    - Bean này dùng chung cho RedisTemplate & Spring Cache
    // =====================================================================
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration serverConfig =
                new RedisStandaloneConfiguration(redisHost, redisPort);

        if (!redisPassword.isBlank()) {
            serverConfig.setPassword(RedisPassword.of(redisPassword));
        }

        return new LettuceConnectionFactory(serverConfig);
    }

    // =====================================================================
    // Đăng ký Object Mapper
    // =====================================================================
    @Bean
    public RedisTemplate<String, String> redisStringTemplate(RedisConnectionFactory cf) {
        var tpl = new RedisTemplate<String, String>();
        tpl.setConnectionFactory(cf);

        var stringSer = new StringRedisSerializer();
        tpl.setKeySerializer(stringSer);
        tpl.setValueSerializer(stringSer);

        tpl.afterPropertiesSet();
        return tpl;
    }
}
