package fpt.is.bnk.fptis_platform.service.daily_log.impl;

import fpt.is.bnk.fptis_platform.dto.report.daily_log.DailyLogReportObject;
import fpt.is.bnk.fptis_platform.entity.daily_log.DailyLog;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.DailyLogRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.daily_log.datasource.DailyLogDataSource;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class DailyLogReportServiceImpl implements fpt.is.bnk.fptis_platform.service.daily_log.DailyLogReportService {

    CurrentUserProvider currentUserProvider;
    DailyLogRepository dailyLogRepository;
    ResourceLoader resourceLoader;

    static String SWAP_PATH = "D:\\Projects\\fptis\\tempSwapDir";

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> generateReport() throws Exception {
        User user = currentUserProvider.getCurrentUser();
        JRSwapFileVirtualizer virtualizer = null;

        try (Stream<DailyLog> dailyLogStream = dailyLogRepository.
                streamAllByProfile_ProfileId(user.getProfile().getProfileId())) {


            File swapDir = new File(SWAP_PATH);
            if (!swapDir.exists()) swapDir.mkdirs();
            JRSwapFile swapFile = new JRSwapFile(SWAP_PATH, 8192, 100);

            virtualizer = new JRSwapFileVirtualizer(500, swapFile, true);

            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Iterator<DailyLogReportObject> reportIterator = dailyLogStream.map(dl -> new DailyLogReportObject(
                    dl.getId(),
                    dl.getMainTask(),
                    dl.getResult(),
                    dl.getWorkDate() != null ? dl.getWorkDate().format(fmt) : ""
            )).iterator();

            JRDataSource dataSource = new DailyLogDataSource(reportIterator);

            InputStream jrxmlStream = resourceLoader.getResource("classpath:reports/Daily_Log_Report.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            Map<String, Object> params = new HashMap<>();
            params.put("USERNAME", user.getUsername());
            params.put("CURRENT_DATE", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            params.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));

            params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

            InputStream logoStream = resourceLoader.getResource("classpath:reports/fpt-is-logo.png").getInputStream();
            params.put("LOGO", logoStream);


            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=daily-log-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Lỗi xuất báo cáo: ", e);
            throw e;
        } finally {
            if (virtualizer != null) {
                virtualizer.cleanup();
                log.info("Đã xóa sạch bộ nhớ tạm ổ đĩa.");
            }
        }
    }
}