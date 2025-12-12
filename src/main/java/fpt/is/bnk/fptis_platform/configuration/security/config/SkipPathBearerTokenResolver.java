package fpt.is.bnk.fptis_platform.configuration.security.config;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Admin 12/11/2025
 *
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SkipPathBearerTokenResolver implements BearerTokenResolver {

    BearerTokenResolver delegate = new DefaultBearerTokenResolver();

    List<String> skipPaths = List.of(
            "/api/users/login",
            "/api/users/register",
            "/api/users/refresh",
            "/actuator/health"
    );

    RedisTemplate<String, String> redis;

    public SkipPathBearerTokenResolver(
            @Qualifier("redisStringTemplate") RedisTemplate<String, String> redis
    ) {
        this.redis = redis;
    }

    @Override
    public String resolve(HttpServletRequest request) {
        String path = request.getRequestURI();

        for (String skip : skipPaths) {
            if (path.contains(skip)) {
                return null;
            }
        }

        String token = delegate.resolve(request);
        if (token == null) return null;

        String isBlacklisted = redis.opsForValue().get("ACCESS_BLACKLIST:" + token);
        if (isBlacklisted != null) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return token;
    }

}
