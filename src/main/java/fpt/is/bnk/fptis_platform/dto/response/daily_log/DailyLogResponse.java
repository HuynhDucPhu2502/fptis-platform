package fpt.is.bnk.fptis_platform.dto.response.daily_log;

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
public class DailyLogResponse {

    Long id;

    String mainTask;
    String result;

    LocalDate workDate;
    LocalTime startTime;
    LocalTime endTime;

    String location;
}
