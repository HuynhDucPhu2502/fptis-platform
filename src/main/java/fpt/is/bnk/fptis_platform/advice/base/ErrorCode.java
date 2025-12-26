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

    // ===== System / Common ===== 1000 - 1099
    INVALID_KEY(1000, "Khoá không hợp lệ", HttpStatus.BAD_REQUEST),
    METHOD_NOT_ALLOWED(1001, "Phương thức HTTP không được hỗ trợ", HttpStatus.METHOD_NOT_ALLOWED),
    UNCATEGORIZED_EXCEPTION(1099, "Lỗi không xác định", HttpStatus.INTERNAL_SERVER_ERROR),

    // ===== Authentication / Authorization ===== 1100 - 1199
    UNAUTHENTICATED(1100, "Chưa xác thực", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1101, "Bạn không có quyền truy cập", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1102, "Token không hợp lệ hoặc đã hết hạn", HttpStatus.UNAUTHORIZED),
    INVALID_CREDENTIALS(1103, "Tên đăng nhập hoặc mật khẩu không đúng", HttpStatus.UNAUTHORIZED),

    // ===== User / Account ===== 1200 - 1299
    EMAIL_EXISTED(1200, "Email đã tồn tại, vui lòng chọn email khác", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1201, "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác", HttpStatus.BAD_REQUEST),
    USERNAME_IS_MISSING(1202, "Vui lòng nhập tên đăng nhập", HttpStatus.BAD_REQUEST),

    // ===== Attendance ===== 2000 - 2099
    ATTENDANCE_ALREADY_CHECKED_IN(2000, "Bạn đã điểm danh hôm nay", HttpStatus.BAD_REQUEST),
    ATTENDANCE_NOT_FOUND(2001, "Không có điểm danh cho ngày hôm nay", HttpStatus.BAD_REQUEST),
    ATTENDANCE_ALREADY_CHECKED_OUT(2002, "Bạn đã check-out hôm nay", HttpStatus.BAD_REQUEST),

    // ===== Data / Persistence ===== 3000 - 3099
    ENTITY_NOT_FOUND(3000, "Không tìm thấy dữ liệu", HttpStatus.NOT_FOUND),
    DATA_INTEGRITY_VIOLATION(3001, "Dữ liệu vi phạm ràng buộc", HttpStatus.CONFLICT),
    ;

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
