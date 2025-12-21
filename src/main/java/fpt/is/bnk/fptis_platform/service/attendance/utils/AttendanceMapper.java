package fpt.is.bnk.fptis_platform.service.attendance.utils;

import fpt.is.bnk.fptis_platform.dto.report.attendance.AttendanceReportObject;
import fpt.is.bnk.fptis_platform.dto.report.attendance.StatusCountReportObject;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.entity.attendance.AttendanceStatus;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Admin 12/21/2025
 *
 **/
public class AttendanceMapper {

    public static List<AttendanceReportObject> mapToAttendanceReportObject(List<Attendance> data) {
        DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        return data.stream()
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
    }

    public static List<StatusCountReportObject> mapToStatusCountReportObject(List<Object[]> statusCount) {
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
