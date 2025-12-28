package fpt.is.bnk.fptis_platform.advice.handler;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.advice.exception.CustomDataIntegrityViolationException;
import fpt.is.bnk.fptis_platform.advice.exception.CustomEntityNotFoundException;
import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.OptimisticLockingException;
import org.camunda.bpm.engine.ProcessEngineException;
import org.camunda.bpm.engine.exception.NullValueException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Global Exception Handler with Centralized Logging
 * Admin 12/28/2025
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==========================================
    // CAMUNDA EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = NullValueException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaNullValue(NullValueException e) {
        log.error("[CAMUNDA NULL VALUE] Resource not found: {}", e.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(404);
        apiResponse.setMessage("Tài nguyên Camunda (Task/Process) không tồn tại hoặc đã hoàn thành.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(value = OptimisticLockingException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaLocking(OptimisticLockingException e) {
        log.warn("[CAMUNDA CONFLICT] Optimistic locking failure: {}", e.getMessage());
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(409);
        apiResponse.setMessage("Yêu cầu đang được xử lý bởi một tác vụ khác. Vui lòng thử lại.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(value = ProcessEngineException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaEngineException(ProcessEngineException e) {
        log.error("[CAMUNDA ENGINE ERROR] Logic execution failed: ", e);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(500);

        String msg = "Lỗi hệ thống thực thi quy trình.";
        if (e.getMessage().contains("Unknown property used in expression")) {
            msg = "Lỗi cấu hình quy trình: Biến truyền vào không hợp lệ.";
        }

        apiResponse.setMessage(msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    // ==========================================
    // APPLICATION EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<Void>> handlingAppException(AppException e) {
        log.error("[BUSINESS ERROR] Code {}: {}", e.getErrorCode().getCode(), e.getErrorCode().getMessage());
        ErrorCode errorCode = e.getErrorCode();

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    // ==========================================
    // SECURITY EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        log.warn("[SECURITY] Access Denied: {}", e.getMessage());
        var errorCode = ErrorCode.UNAUTHORIZED;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getCode())
                .body(apiResponse);
    }

    // ==========================================
    // VALIDATION & REQUEST EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("[VALIDATION FAILED] Params invalid: {}", errorMessage);
        var errorCode = ErrorCode.INVALID_KEY;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorMessage);

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            MethodArgumentTypeMismatchException.class}
    )
    public ResponseEntity<ApiResponse<Void>> handleBadRequestExceptions(Exception e) {
        log.warn("[BAD REQUEST] Argument mismatch or unreadable: {}", e.getMessage());
        var errorCode = ErrorCode.INVALID_KEY;
        String errorMessage;

        if (e instanceof MethodArgumentTypeMismatchException ex) {
            String field = ex.getName();
            String expectedType = ex.getRequiredType() != null
                    ? ex.getRequiredType().getSimpleName()
                    : "giá trị hợp lệ";

            errorMessage = String.format("Giá trị của '%s' không hợp lệ. Yêu cầu kiểu %s.", field, expectedType);
        } else {
            errorMessage = e.getMessage();
        }

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorMessage);

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.warn("[METHOD NOT ALLOWED] HTTP Method {} not supported", e.getMethod());
        var errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // ==========================================
    // DATABASE EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        String message = e.getMostSpecificCause().getMessage();
        log.error("[DATABASE CONSTRAINT] Violation detail: {}", message);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        if (message.contains("uk_user_email"))
            apiResponse.setCode(ErrorCode.EMAIL_EXISTED.getCode());
        else if (message.contains("uk_user_username"))
            apiResponse.setCode(ErrorCode.USER_EXISTED.getCode());
        else
            apiResponse.setCode(ErrorCode.DATA_INTEGRITY_VIOLATION.getCode());

        apiResponse.setMessage(ErrorCode.fromCode(apiResponse.getCode()).getMessage());

        return ResponseEntity.status(apiResponse.getCode()).body(apiResponse);
    }

    @ExceptionHandler(value = CustomEntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(CustomEntityNotFoundException e) {
        log.warn("[NOT FOUND] Entity not found: {}", e.getMessage());
        var error = ErrorCode.ENTITY_NOT_FOUND;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessage(e.getMessage());

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = CustomDataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(CustomDataIntegrityViolationException e) {
        log.error("[CUSTOM DB ERROR] {}", e.getMessage());
        var error = ErrorCode.DATA_INTEGRITY_VIOLATION;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(error.getCode());
        apiResponse.setMessage(e.getMessage());

        return ResponseEntity.status(error.getStatusCode()).body(apiResponse);
    }

    // ==========================================
    // UNCATEGORIZED EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("[INTERNAL SERVER ERROR] Uncategorized exception occurred: ", e);
        var errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION;

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }
}