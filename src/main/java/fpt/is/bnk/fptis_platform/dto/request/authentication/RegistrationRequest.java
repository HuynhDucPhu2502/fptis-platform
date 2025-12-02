package fpt.is.bnk.fptis_platform.dto.request.authentication;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Admin 11/25/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegistrationRequest {
    @Size(min = 4, message = "Tài khoản ít nhất 4 ký tự")
    String username;

    @Size(min = 6, message = "Mật khẩu ít nhất 8 ký tự")
    String password;

    String email;
    String firstName;
    String lastName;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate dob;
}
