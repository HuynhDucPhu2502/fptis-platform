package fpt.is.bnk.fptis_platform.dto.identity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 11/25/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenExchangeResponse {

    String accessToken;
    Long expiresIn;

    String refreshToken;
    Long refreshExpiresIn;

    String idToken;

    String tokenType;
    String scope;

}

