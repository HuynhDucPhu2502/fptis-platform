package fpt.is.bnk.fptis_platform.dto.response.process;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/30/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ActionButtonResponse {

    String label;
    String color;
    String variableName;
    Object value;

}
