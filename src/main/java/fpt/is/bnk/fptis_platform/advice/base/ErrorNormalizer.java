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
import java.util.Objects;

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

    public ErrorNormalizer() {
        objectMapper = new ObjectMapper();
        errorCodeMap = new HashMap<>();

        errorCodeMap.put("User exists with same username", ErrorCode.USER_EXISTED);
        errorCodeMap.put("User exists with same email", ErrorCode.EMAIL_EXISTED);
        errorCodeMap.put("User name is missing", ErrorCode.USERNAME_IS_MISSING);
        errorCodeMap.put("Invalid user credentials", ErrorCode.INVALID_CREDENTIALS);
    }

    public AppException handleKeyCloakException(FeignException exception) {
        try {
            var response = objectMapper.readValue(exception.contentUTF8(), KeyCloakError.class);
            System.out.println(response);

            String msg = response.getErrorMessage();
            if (msg == null) msg = response.getError_description();
            if (msg == null) msg = response.getError();

            System.out.println(msg);

            ErrorCode mapped = errorCodeMap.get(msg);
            if (mapped != null) {
                throw new AppException(mapped);
            }

        } catch (JsonProcessingException ignored) {
        }

        throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
    }

}
