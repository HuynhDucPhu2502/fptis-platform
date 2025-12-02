package fpt.is.bnk.fptis_platform.dto.request.daily_log;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 11/28/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateDailyLogRequest {

    @NotBlank(message = "Nhiệm vụ chính không được để trống")
    String mainTask;

    @NotBlank(message = "Kết quả đạt được không được để trống")
    String result;

}
