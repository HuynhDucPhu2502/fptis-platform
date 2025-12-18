package fpt.is.bnk.fptis_platform.configuration.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.List;

/**
 * Admin 12/11/2025
 *
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomBearerTokenResolver implements BearerTokenResolver {

    BearerTokenResolver delegate = new DefaultBearerTokenResolver();
    AntPathMatcher pathMatcher = new AntPathMatcher();

    List<String> skipPaths = List.of(
            "/api/users/login",
            "/api/users/register",
            "/api/users/refresh",
            "/actuator/health/**"
    );

    RedisTemplate<String, String> redis;

    public CustomBearerTokenResolver(
            @Qualifier("redisStringTemplate") RedisTemplate<String, String> redis
    ) {
        this.redis = redis;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        String path = request.getRequestURI();

        if (skipPaths.stream().anyMatch(pattern -> pathMatcher.match(pattern, path)))
            return null;


        String token = delegate.resolve(request);
        if (token == null) return null;

        if (Boolean.TRUE.equals(redis.hasKey("ACCESS_BLACKLIST:" + token)))
            return null;

        return token;
    }

}
