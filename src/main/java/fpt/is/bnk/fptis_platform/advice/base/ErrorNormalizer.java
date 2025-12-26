package fpt.is.bnk.fptis_platform.advice.base;

import feign.FeignException;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

/**
 * Admin 11/27/2025
 *
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorNormalizer {

    public AppException handleKeyCloakException(FeignException exception) {
        return switch (exception.status()) {
            case 400 -> new AppException(ErrorCode.INVALID_KEY);
            case 401 -> new AppException(ErrorCode.INVALID_CREDENTIALS);
            case 403 -> new AppException(ErrorCode.UNAUTHORIZED);
            case 409 -> new AppException(ErrorCode.USER_EXISTED);
            default -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        };
    }

}
