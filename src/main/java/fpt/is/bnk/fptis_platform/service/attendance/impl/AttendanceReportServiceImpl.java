package fpt.is.bnk.fptis_platform.service.attendance.impl;

import fpt.is.bnk.fptis_platform.dto.report.attendance.AttendanceReportObject;
import fpt.is.bnk.fptis_platform.dto.report.attendance.StatusCountReportObject;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.entity.attendance.AttendanceStatus;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceReportServiceImpl implements fpt.is.bnk.fptis_platform.service.attendance.AttendanceReportService {

    CurrentUserProvider currentUserProvider;
    ResourceLoader resourceLoader;
    AttendanceRepository attendanceRepository;

    @Override
    public ResponseEntity<byte[]> generateReport() throws Exception {
        User user = currentUserProvider.getCurrentUser();

        List<Attendance> data = attendanceRepository.findByProfileProfileId(user.getProfile().getProfileId());
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        List<AttendanceReportObject> reportData = data.stream()
                .map(attendance -> new AttendanceReportObject(
                        attendance.getDate() != null
                                ? attendance.getDate().format(dateFmt) : "",
                        attendance.getTimeIn() != null
                                ? attendance.getTimeIn().format(timeFmt) : "",
                        attendance.getCheckInStatus() != null
                                ? attendance.getCheckInStatus().toString() : "",
                        attendance.getTimeOut() != null
                                ? attendance.getTimeOut().format(timeFmt) : "",
                        attendance.getCheckOutStatus() != null
                                ? attendance.getCheckOutStatus().toString() : ""
                ))
                .toList();

        JRDataSource dataSource = new JRBeanCollectionDataSource(reportData);

        List<Object[]> statusCount = attendanceRepository.countStatusByUserId(user.getId());
        List<StatusCountReportObject> statusCountList = transformToStatusCountDTO(statusCount);

        JRDataSource pieChartDataSource = new JRBeanCollectionDataSource(statusCountList);

        InputStream jrxmlStream = resourceLoader
                .getResource("classpath:reports/Attendance_Report.jrxml")
                .getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);

        Map<String, Object> params = new HashMap<>();

        InputStream logoStream = resourceLoader
                .getResource("classpath:reports/fpt-is-logo.png")
                .getInputStream();
        params.put("LOGO", logoStream);
        params.put("USERNAME", user.getUsername());
        String currentDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        params.put("CURRENT_DATE", currentDate);

        params.put("PIE_CHART_DS", pieChartDataSource);

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, dataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);

        return ResponseEntity.ok()
                .header("Content-Type", "application/pdf")
                .header("Content-Disposition", "inline; filename=attendance_report.pdf")
                .body(byteArrayOutputStream.toByteArray());
    }


    private List<StatusCountReportObject> transformToStatusCountDTO(List<Object[]> statusCount) {
        return statusCount.stream()
                .map(obj -> {
                    AttendanceStatus statusEnum =
                            AttendanceStatus.valueOf((String) obj[0]);
                    return new StatusCountReportObject(
                            statusEnum.toString(),
                            ((Number) obj[1]).longValue()
                    );
                })
                .toList();
    }

}