package fpt.is.bnk.fptis_platform.dto.response.process;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/28/2025
 *
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessVariableResponse {
    String variableName;
    String displayName;
    String dataType;
}
