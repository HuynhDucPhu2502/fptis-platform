package fpt.is.bnk.fptis_platform.service.attendance.impl;

import fpt.is.bnk.fptis_platform.dto.report.attendance.StatusCountReportObject;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.service.attendance.utils.AttendanceMapper;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Admin 12/21/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceStatisticServiceImpl implements fpt.is.bnk.fptis_platform.service.attendance.AttendanceStatisticService {

    AttendanceRepository attendanceRepository;
    CurrentUserProvider currentUserProvider;

    @Override
    public List<StatusCountReportObject> getCurrentUserAttendanceStatistic(User user) {

        if (user == null)
            user = currentUserProvider.getCurrentUser();

        List<Object[]> attendanceStatusStatistic = attendanceRepository
                .countStatusByProfileId(user.getProfile().getProfileId());

        return AttendanceMapper.mapToStatusCountReportObject(attendanceStatusStatistic);

    }
}
