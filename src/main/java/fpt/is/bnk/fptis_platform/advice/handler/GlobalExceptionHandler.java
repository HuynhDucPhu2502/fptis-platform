package fpt.is.bnk.fptis_platform.advice.handler;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
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
 * Admin 11/25/2025
 **/
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==========================================
    // CAMUNDA EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = NullValueException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaNullValue(NullValueException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(404);
        apiResponse.setMessage("Tài nguyên Camunda (Task/Process) không tồn tại hoặc đã hoàn thành.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }

    @ExceptionHandler(value = OptimisticLockingException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaLocking(OptimisticLockingException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(409);
        apiResponse.setMessage("Yêu cầu đang được xử lý bởi một tác vụ khác. Vui lòng thử lại.");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
    }

    @ExceptionHandler(value = ProcessEngineException.class)
    ResponseEntity<ApiResponse<Void>> handleCamundaEngineException(ProcessEngineException e) {
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
        log.error(e.getMessage(), e);
        ErrorCode errorCode = e.getErrorCode();
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    // ==========================================
    // SECURITY EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(AccessDeniedException e) {
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNAUTHORIZED.getCode());
        apiResponse.setMessage(ErrorCode.UNAUTHORIZED.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(apiResponse);
    }

    // ==========================================
    // VALIDATION & REQUEST EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String errorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(errorMessage);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiResponse<Void>> handleBadRequestExceptions(Exception e) {
        log.error(e.getMessage(), e);
        String errorMessage = "Invalid request parameter";
        if (e instanceof MethodArgumentTypeMismatchException ex) {
            Class<?> type = ex.getRequiredType();
            String typeName = type != null ? type.getSimpleName() : "a valid type";
            errorMessage = ex.getName() + " must be of type " + typeName;
        }
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(errorMessage);
        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
        log.error(e.getMessage(), e);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.METHOD_NOT_ALLOWED.getCode());
        apiResponse.setMessage(ErrorCode.METHOD_NOT_ALLOWED.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(apiResponse);
    }

    // ==========================================
    // DATABASE EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolation(DataIntegrityViolationException e) {
        log.error(e.getMessage(), e);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        String message = e.getMostSpecificCause().getMessage();
        if (message.contains("uk_user_email")) apiResponse.setCode(ErrorCode.EMAIL_EXISTED.getCode());
        else if (message.contains("uk_user_username")) apiResponse.setCode(ErrorCode.USER_EXISTED.getCode());
        else apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
        apiResponse.setMessage(ErrorCode.fromCode(apiResponse.getCode()).getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // ==========================================
    // UNCATEGORIZED EXCEPTIONS
    // ==========================================

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error(e.getMessage(), e);
        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }
}