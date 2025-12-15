package fpt.is.bnk.fptis_platform.service.attendance;

import org.springframework.http.ResponseEntity;

/**
 * Admin 12/15/2025
 *
 **/
public interface AttendanceReportService {
    ResponseEntity<byte[]> generateReport() throws Exception;
}
