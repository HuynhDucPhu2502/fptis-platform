package fpt.is.bnk.fptis_platform.advice.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

/**
 * Admin 11/25/2025
 *
 **/
@Getter
public enum ErrorCode {

    // ===== System / Common =====
    UNCATEGORIZED_EXCEPTION(9999, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),
    METHOD_NOT_ALLOWED(1008, "Phương thức HTTP không được hỗ trợ", HttpStatus.METHOD_NOT_ALLOWED),
    INVALID_KEY(1001, "Khoá không hợp lệ", HttpStatus.BAD_REQUEST),

    // ===== Authentication / Authorization =====
    UNAUTHENTICATED(1006, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1012, "Token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1013, "Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),

    // ===== User / Account =====
    EMAIL_EXISTED(1009, "Email đã tồn tại, vui lòng chọn email khác", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1010, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1011, "Vui lòng nhập tên đăng nhập", HttpStatus.BAD_REQUEST),

    // ===== Attendance =====
    ATTENDANCE_ALREADY_CHECKED_IN(2001, "Bạn đã điểm danh hôm nay", HttpStatus.BAD_REQUEST),
    ATTENDANCE_NOT_FOUND(2002, "Không có điểm danh cho ngày hôm nay", HttpStatus.BAD_REQUEST),
    ATTENDANCE_ALREADY_CHECKED_OUT(2003, "Bạn đã check-out hôm nay", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public static ErrorCode fromCode(int code) {
        for (ErrorCode ec : values()) {
            if (ec.code == code) return ec;
        }
        return UNCATEGORIZED_EXCEPTION;
    }
}
