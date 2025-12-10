package fpt.is.bnk.fptis_platform.service.daily_log.impl;

import fpt.is.bnk.fptis_platform.entity.DailyLog;
import fpt.is.bnk.fptis_platform.entity.User;
import fpt.is.bnk.fptis_platform.repository.DailyLogRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DailyLogReportServiceImpl implements fpt.is.bnk.fptis_platform.service.daily_log.DailyLogReportService {

    CurrentUserProvider currentUserProvider;
    DailyLogRepository dailyLogRepository;
    ResourceLoader resourceLoader;

    public ResponseEntity<byte[]> generateReport() throws Exception {
        User user = currentUserProvider.getCurrentUser();

        // Lấy dữ liệu
        List<DailyLog> data = dailyLogRepository
                .findByProfile_ProfileId(user.getProfile().getProfileId());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

        // Lấy template
        InputStream jrxmlStream = resourceLoader
                .getResource("classpath:reports/Daily_Log_Report.jrxml")
                .getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);


        // Đẩy dữ liệu vào Param template
        Map<String, Object> params = new HashMap<>();
        InputStream logoStream = resourceLoader
                .getResource("classpath:reports/fpt-is-logo.png")
                .getInputStream();
        params.put("LOGO", logoStream);


        params.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));

        // Làm ra report
        JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, params, dataSource);

        // Convert dữ liệu thành byte
        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=daily-log-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
