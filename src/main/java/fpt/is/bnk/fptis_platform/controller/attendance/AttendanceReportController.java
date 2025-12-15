package fpt.is.bnk.fptis_platform.controller.attendance;

import fpt.is.bnk.fptis_platform.service.attendance.AttendanceReportService;
import fpt.is.bnk.fptis_platform.service.daily_log.impl.DailyLogReportServiceImpl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/15/2025
 *
 **/
@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceReportController {

    private final AttendanceReportService attendanceReportService;

    @GetMapping("/report")
    public ResponseEntity<byte[]> exportDailyLog() throws Exception {
        return attendanceReportService.generateReport();
    }

}
