package fpt.is.bnk.fptis_platform.dto.identity.internal;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 11/25/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationParam {
    String username;
    boolean enabled;
    String email;
    boolean emailVerified;
    String firstName;
    String lastName;
    String federationLink;
}