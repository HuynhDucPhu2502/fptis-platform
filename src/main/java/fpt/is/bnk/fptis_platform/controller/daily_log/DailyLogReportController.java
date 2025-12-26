package fpt.is.bnk.fptis_platform.controller.daily_log;

import fpt.is.bnk.fptis_platform.service.daily_log.DailyLogReportService;
import fpt.is.bnk.fptis_platform.service.daily_log.impl.DailyLogReportServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/8/2025
 *
 **/
@Tag(
        name = "Daily Logs"
)
@RestController
@RequiredArgsConstructor
public class DailyLogReportController {

    private final DailyLogReportService dailyLogReportService;

    @GetMapping("/api/daily-logs/report")
    public ResponseEntity<byte[]> exportDailyLog() throws Exception {
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=daily_log_report.pdf"
                )
                .contentType(MediaType.APPLICATION_PDF)
                .body(dailyLogReportService.generateReport());
    }
}
