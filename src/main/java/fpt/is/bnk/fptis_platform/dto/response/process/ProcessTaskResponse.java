package fpt.is.bnk.fptis_platform.dto.response.process;

import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/25/2025
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProcessTaskResponse {
    String taskCode;
    String taskName;
    String permission;
    Boolean isActive;
}
