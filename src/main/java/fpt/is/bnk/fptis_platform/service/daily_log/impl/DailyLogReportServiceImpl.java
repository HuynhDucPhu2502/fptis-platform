package fpt.is.bnk.fptis_platform.service.daily_log.impl;

import fpt.is.bnk.fptis_platform.dto.report.daily_log.DailyLogReportObject;
import fpt.is.bnk.fptis_platform.entity.daily_log.DailyLog;
import fpt.is.bnk.fptis_platform.entity.user.User;
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
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        List<DailyLogReportObject> reportData = data.stream()
                .map(dl -> new DailyLogReportObject(
                        dl.getId(),
                        dl.getMainTask(),
                        dl.getResult(),
                        dl.getWorkDate() != null ? dl.getWorkDate().format(fmt) : ""
                ))
                .toList();

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(reportData);


        // Lấy template
        InputStream jrxmlStream = resourceLoader
                .getResource("classpath:reports/Daily_Log_Report.jrxml")
                .getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        // ===========================================================================
        // Tạo tham số cho báo cáo
        Map<String, Object> params = new HashMap<>();

        // Tham số: LOGO
        InputStream logoStream = resourceLoader
                .getResource("classpath:reports/fpt-is-logo.png")
                .getInputStream();
        params.put("LOGO", logoStream);

        // Tham số: USERNAME
        params.put("USERNAME", user.getUsername());

        // Tham số: CURRENT_DATE
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());  // Định dạng ngày hiện tại
        params.put("CURRENT_DATE", currentDate);
        // ===========================================================================


        params.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=daily-log-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
