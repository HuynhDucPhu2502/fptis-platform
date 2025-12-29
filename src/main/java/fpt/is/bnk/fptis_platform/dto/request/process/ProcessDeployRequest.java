package fpt.is.bnk.fptis_platform.dto.request.process;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/24/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessDeployRequest {
    String name;

    @NotBlank(message = "Process code không được để trống")
    String processCode;
}
