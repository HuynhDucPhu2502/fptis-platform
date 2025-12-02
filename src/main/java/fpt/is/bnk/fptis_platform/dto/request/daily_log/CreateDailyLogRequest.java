package fpt.is.bnk.fptis_platform.dto.request.daily_log;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Admin 11/28/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateDailyLogRequest {

    @NotBlank(message = "Nhiệm vụ chính không được để trống")
    String mainTask;

    @NotBlank(message = "Kết quả đạt được không được để trống")
    String result;

    @NotNull(message = "Ngày làm việc không được để trống")
    LocalDate workDate;

    LocalTime startTime;
    LocalTime endTime;

    @NotBlank(message = "Địa điểm không được để trống")
    String location;

}
