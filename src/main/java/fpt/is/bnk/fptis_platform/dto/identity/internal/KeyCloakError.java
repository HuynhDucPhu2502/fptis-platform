package fpt.is.bnk.fptis_platform.dto.identity.internal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 11/27/2025
 *
 **/
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
public class KeyCloakError {

    String error;
    String errorMessage;
    String error_description;

}
