package fpt.is.bnk.fptis_platform.dto.request.authentication;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 11/27/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @Size(min = 4, message = "Tài khoản ít nhất 4 ký tự")
    String username;

    @Size(min = 6, message = "Mật khẩu ít nhất 8 ký tự")
    String password;

}
