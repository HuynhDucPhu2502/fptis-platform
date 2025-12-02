package fpt.is.bnk.fptis_platform.dto.identity;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/1/2025
 *
 **/
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CheckPasswordRequest {
    String username;
    String password;
}
