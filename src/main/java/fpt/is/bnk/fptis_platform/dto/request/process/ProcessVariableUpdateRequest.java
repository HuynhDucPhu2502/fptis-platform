package fpt.is.bnk.fptis_platform.dto.request.process;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/28/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessVariableUpdateRequest {

    String variableName;
    String defaultValue;
    String dataType;

}
