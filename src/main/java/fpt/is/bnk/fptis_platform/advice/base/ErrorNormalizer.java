package fpt.is.bnk.fptis_platform.advice.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.dto.identity.internal.KeyCloakError;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Admin 11/27/2025
 *
 **/
@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ErrorNormalizer {

    ObjectMapper objectMapper;
    Map<String, ErrorCode> errorCodeMap;

    public ErrorNormalizer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.errorCodeMap = initErrorCodeMap();
    }

    private Map<String, ErrorCode> initErrorCodeMap() {
        Map<String, ErrorCode> map = new HashMap<>();
        map.put("User exists with same username", ErrorCode.USER_EXISTED);
        map.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        map.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
        map.put("Invalid user credentials", ErrorCode.INVALID_CREDENTIALS);
        return map;
    }

    public AppException handleKeyCloakException(FeignException exception) {
        try {
            KeyCloakError response =
                    objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);

            String message = extractMessage(response);
            log.warn("Keycloak error message: {}", message);

            ErrorCode mapped = errorCodeMap.get(message);
            if (mapped != null) {
                return new AppException(mapped);
            }

        } catch (JsonProcessingException e) {
            log.error("Failed to parse Keycloak error response", e);
        }

        return new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

    private String extractMessage(KeyCloakError error) {
        if (error == null) return null;
        if (error.getErrorMessage() != null) return error.getErrorMessage();
        if (error.getError_description() != null) return error.getError_description();
        return error.getError();
    }
}
