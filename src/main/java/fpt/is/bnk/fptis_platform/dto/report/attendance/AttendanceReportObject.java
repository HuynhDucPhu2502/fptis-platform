package fpt.is.bnk.fptis_platform.dto.report.attendance;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Admin 12/15/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttendanceReportObject {

    String date;
    String timeIn;
    String checkInStatus;
    String timeOut;
    String checkOutStatus;
    
}
