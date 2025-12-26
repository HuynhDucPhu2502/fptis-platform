package fpt.is.bnk.fptis_platform.controller.attendance;

import fpt.is.bnk.fptis_platform.service.attendance.AttendanceReportService;
import fpt.is.bnk.fptis_platform.service.daily_log.impl.DailyLogReportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/15/2025
 *
 **/
@Tag(name = "Attendance")
@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceReportController {

    private final AttendanceReportService attendanceReportService;

    @GetMapping("/report")
    public ResponseEntity<byte[]> exportDailyLog() throws Exception {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=attendance_report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(attendanceReportService.generateReport());
    }

}
