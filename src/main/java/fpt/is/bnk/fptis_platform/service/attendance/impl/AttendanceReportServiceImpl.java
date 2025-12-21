package fpt.is.bnk.fptis_platform.service.attendance.impl;

import fpt.is.bnk.fptis_platform.dto.report.attendance.AttendanceReportObject;
import fpt.is.bnk.fptis_platform.dto.report.attendance.StatusCountReportObject;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.service.attendance.AttendanceStatisticService;
import fpt.is.bnk.fptis_platform.service.attendance.utils.AttendanceMapper;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRSwapFile;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceReportServiceImpl implements fpt.is.bnk.fptis_platform.service.attendance.AttendanceReportService {

    // Provider
    CurrentUserProvider currentUserProvider;
    ResourceLoader resourceLoader;

    // Repository
    AttendanceRepository attendanceRepository;

    // Service
    AttendanceStatisticService attendanceStatisticService;

    static String SWAP_PATH = "D:\\Projects\\fptis\\tempSwapDir";

    @Override
    public byte[] generateReport() throws Exception {
        User user = currentUserProvider.getCurrentUser();
        JRSwapFileVirtualizer virtualizer = null;


        try {
            // =====================================================
            // Chuẩn bị swap file
            // =====================================================
            File swapDir = new File(SWAP_PATH);
            if (!swapDir.exists()) swapDir.mkdirs();

            JRSwapFile swapFile = new JRSwapFile(SWAP_PATH, 8192, 100);
            virtualizer = new JRSwapFileVirtualizer(300, swapFile, true);

            // =====================================================
            // Chuẩn bị dữ liệu
            // =====================================================
            List<Attendance> attendances = attendanceRepository
                    .findByProfileProfileId(user.getProfile().getProfileId());

            List<AttendanceReportObject> attendancesReportData =
                    AttendanceMapper.mapToAttendanceReportObject(attendances);

            List<StatusCountReportObject> attendanceStatusStatisticReportData =
                    attendanceStatisticService.getCurrentUserAttendanceStatistic(user);

            // =====================================================
            // Định nghĩa Datasource để đẩy vào Jasper
            // =====================================================
            JRDataSource dataSource =
                    new JRBeanCollectionDataSource(attendancesReportData);
            JRDataSource pieChartDataSource =
                    new JRBeanCollectionDataSource(attendanceStatusStatisticReportData);

            // =====================================================
            // Lấy template
            // =====================================================
            InputStream jrxmlStream = resourceLoader
                    .getResource("classpath:reports/Attendance_Report.jrxml")
                    .getInputStream();

            JasperReport jasperReport = JasperCompileManager
                    .compileReport(jrxmlStream);


            // =====================================================
            // Định nghĩa tham số
            // =====================================================
            Map<String, Object> params = new HashMap<>();

            InputStream logoStream = resourceLoader
                    .getResource("classpath:reports/fpt-is-logo.png")
                    .getInputStream();
            params.put("LOGO", logoStream);
            params.put("USERNAME", user.getUsername());
            params.put("CURRENT_DATE", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            params.put("PIE_CHART_DS", pieChartDataSource);

            params.put(JRParameter.REPORT_LOCALE, Locale.forLanguageTag("vi-VN"));
            params.put(JRParameter.REPORT_VIRTUALIZER, virtualizer);


            // =====================================================
            // Đẩy dữ liệu vào + xuất báo cáo
            // =====================================================
            JasperPrint jasperPrint =
                    JasperFillManager.fillReport(jasperReport, params, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } finally {
            if (virtualizer != null) {
                virtualizer.cleanup();
            }
        }
    }


}