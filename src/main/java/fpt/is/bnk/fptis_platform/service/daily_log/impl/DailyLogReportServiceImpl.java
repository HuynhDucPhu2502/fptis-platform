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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<byte[]> generateReport() throws Exception {
        User user = currentUserProvider.getCurrentUser();
        JRSwapFileVirtualizer virtualizer = null;

        // Mở Stream để đọc từng bản ghi từ Database
        try (Stream<DailyLog> dailyLogStream = dailyLogRepository.streamAllByProfile_ProfileId(user.getProfile().getProfileId())) {

            log.info("Khởi tạo hệ thống Virtualizer cho 1 triệu bản ghi...");

            // 1. Cấu hình Swap File tối ưu
            String swapPath = "D:\\Projects\\fptis\\tempSwapDir";
            File swapDir = new File(swapPath);
            if (!swapDir.exists()) swapDir.mkdirs();

            // blockSize: 8KB (Giảm lãng phí đĩa cứng cực lớn so với 1MB cũ)
            // minBlocks: 100
            JRSwapFile swapFile = new JRSwapFile(swapPath, 8192, 100);

            // Giữ 500 trang trên RAM (vì bạn set 8GB Heap nên có thể giữ nhiều hơn để tăng tốc)
            virtualizer = new JRSwapFileVirtualizer(500, swapFile, true);

            // 2. Chuyển đổi Stream sang Iterator để nạp dữ liệu Lazy
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Iterator<DailyLogReportObject> reportIterator = dailyLogStream.map(dl -> new DailyLogReportObject(
                    dl.getId(),
                    dl.getMainTask(),
                    dl.getResult(),
                    dl.getWorkDate() != null ? dl.getWorkDate().format(fmt) : ""
            )).iterator();

            // 3. Sử dụng Custom DataSource không dùng Reflection
            JRDataSource dataSource = new DailyLogDataSource(reportIterator);

            // 4. Load Template
            InputStream jrxmlStream = resourceLoader.getResource("classpath:reports/Daily_Log_Report.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

            // 5. Cấu hình Parameters
            Map<String, Object> params = new HashMap<>();
            params.put("USERNAME", user.getUsername());
            params.put("CURRENT_DATE", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            params.put(JRParameter.REPORT_LOCALE, new Locale("vi", "VN"));

            // Kích hoạt Virtualizer để "ép" dữ liệu xuống đĩa D:
            params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);

            InputStream logoStream = resourceLoader.getResource("classpath:reports/fpt-is-logo.png").getInputStream();
            params.put("LOGO", logoStream);

            log.info("Đang xử lý Fill Report (Quá trình này tốn đĩa tạm thời)...");

            // 6. Fill Report - Jasper sẽ viết xuống đĩa D: mỗi khi vượt quá 500 trang
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

            log.info("Đang kết xuất PDF...");
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=daily-log-report.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfBytes);

        } catch (Exception e) {
            log.error("Lỗi xuất báo cáo: ", e);
            throw e;
        } finally {
            // Xóa sạch file swap trong thư mục D: sau khi hoàn thành
            if (virtualizer != null) {
                virtualizer.cleanup();
                log.info("Đã xóa sạch bộ nhớ tạm ổ đĩa.");
            }
        }
    }
}