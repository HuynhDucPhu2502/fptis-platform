package fpt.is.bnk.fptis_platform.service.daily_log;

import org.springframework.http.ResponseEntity;

/**
 * Admin 12/8/2025
 *
 **/
public interface DailyLogReportService {
    ResponseEntity<byte[]> generateReport() throws Exception;
}
