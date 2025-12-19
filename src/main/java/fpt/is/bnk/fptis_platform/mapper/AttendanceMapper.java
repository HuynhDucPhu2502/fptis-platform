
package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.response.attendance.AttendanceResponse;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Admin 11/28/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(source = "attendance.profile.profileId", target = "profileId")
    AttendanceResponse toAttendanceResponse(Attendance attendance);


}
