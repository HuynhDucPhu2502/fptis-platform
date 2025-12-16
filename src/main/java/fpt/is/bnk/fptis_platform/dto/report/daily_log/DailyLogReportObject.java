package fpt.is.bnk.fptis_platform.dto.report.daily_log;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/16/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyLogReportObject {

    Long id;
    String mainTask;
    String result;
    String workDate;

}
