package fpt.is.bnk.fptis_platform.service.auth;

import fpt.is.bnk.fptis_platform.entity.user.User;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * Admin 12/11/2025
 *
 **/
public interface JwtService {
    String buildJwt(User user, Long expirationRate);

    Jwt decodeJwt(String token);
}
