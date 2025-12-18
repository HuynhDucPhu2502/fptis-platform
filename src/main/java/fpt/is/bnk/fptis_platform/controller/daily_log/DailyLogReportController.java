package fpt.is.bnk.fptis_platform.controller.daily_log;

import fpt.is.bnk.fptis_platform.service.daily_log.DailyLogReportService;
import fpt.is.bnk.fptis_platform.service.daily_log.impl.DailyLogReportServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/8/2025
 *
 **/
@RestController
@RequiredArgsConstructor
public class DailyLogReportController {

    private final DailyLogReportService reportService;

    @GetMapping("/api/daily-logs/report")
    public ResponseEntity<byte[]> exportDailyLog() throws Exception {
        return reportService.generateReport();
    }
}
