package fpt.is.bnk.fptis_platform.service.attendance;

import fpt.is.bnk.fptis_platform.dto.report.attendance.StatusCountReportObject;
import fpt.is.bnk.fptis_platform.entity.user.User;

import java.util.List;

/**
 * Admin 12/21/2025
 *
 **/
public interface AttendanceStatisticService {
    List<StatusCountReportObject> getCurrentUserAttendanceStatistic(User user);
}
