package fpt.is.bnk.fptis_platform.service.auth.impl;

import fpt.is.bnk.fptis_platform.configuration.security.config.AuthConfiguration;
import fpt.is.bnk.fptis_platform.entity.authorization.Role;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.mapper.UserMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Admin 12/11/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class JwtServiceImpl implements fpt.is.bnk.fptis_platform.service.auth.JwtService {

    JwtEncoder jwtEncoder;
    JwtDecoder jwtDecoder;

    UserMapper userMapper;

    @Override
    public String buildJwt(User user, Long expirationRate) {
        // Lấy thời điểm hiện tại
        Instant now = Instant.now();

        // Tính toán thời điểm JWT sẽ hết hạn
        Instant validity = now.plus(expirationRate, ChronoUnit.SECONDS);

        // Khai báo phần Header của JWT
        // Ở đây chứa thông tin về thuật toán ký (MAC algorithm) mà hệ thống đang dùng
        JwsHeader jwsHeader = JwsHeader.with(AuthConfiguration.MAC_ALGORITHM).build();

        // Khai báo phần Body (Claims) của JWT, bao gồm:
        // + issuedAt: thời điểm token được tạo ra
        // + expiresAt: thời điểm token hết hạn
        // + subject: email của người dùng (được dùng làm định danh chính)
        // + claim "user": thông tin cơ bản của người dùng, được map sang DTO UserSessionResponse
        // + claim "role": tên chức vụ của người dùng
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(user.getEmail())
                .claim("user", userMapper.toRemoteUser(user, user.getProfile()))
                .claim("roles", user.getRoles().stream().map(Role::getName).toList())
                .build();

        // Cuối cùng, encode JWT và lấy ra chuỗi token trả về
        return jwtEncoder
                .encode(JwtEncoderParameters.from(jwsHeader, claims))
                .getTokenValue();
    }


    @Override
    public Jwt decodeJwt(String token) {
        return jwtDecoder.decode(token);
    }

}
