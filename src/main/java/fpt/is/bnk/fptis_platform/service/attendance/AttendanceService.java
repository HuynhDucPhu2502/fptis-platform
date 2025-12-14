package fpt.is.bnk.fptis_platform.service.attendance;

import fpt.is.bnk.fptis_platform.dto.response.attendance.AttendanceResponse;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Admin 12/12/2025
 *
 **/
public interface AttendanceService {
    AttendanceResponse checkIn();

    AttendanceResponse checkOut();

    AttendanceResponse getCurrentAttendanceByUser();

    Page<AttendanceResponse> getAttendanceByUser(Pageable pageable);
}
