
package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.request.daily_log.CreateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.response.attendance.AttendanceResponse;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.entity.daily_log.DailyLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Admin 11/28/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "attendance.user.id", target = "userId")
    AttendanceResponse toAttendanceResponse(Attendance attendance);


}
