package fpt.is.bnk.fptis_platform.service.attendance.impl;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.dto.response.attendance.AttendanceResponse;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.mapper.AttendanceMapper;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

/**
 * Admin 12/12/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
public class AttendanceServiceImpl implements fpt.is.bnk.fptis_platform.service.attendance.AttendanceService {

    // Repository
    AttendanceRepository attendanceRepository;

    // Provider
    CurrentUserProvider currentUserProvider;

    // Mapper
    AttendanceMapper attendanceMapper;

    final LocalTime scheduledTimeIn = LocalTime.of(8, 30);
    final LocalTime scheduledTimeOut = LocalTime.of(17, 30);


    @Override
    public AttendanceResponse checkIn() {
        var user = currentUserProvider.getCurrentUser();

        Optional<Attendance> existingAttendance = attendanceRepository
                .findByProfileProfileIdAndDate(user.getProfile().getProfileId(), LocalDate.now());

        if (existingAttendance.isPresent())
            throw new AppException(ErrorCode.ATTENDANCE_ALREADY_CHECKED_IN);


        Attendance attendance = new Attendance();
        attendance.setProfile(user.getProfile());
        attendance.setDate(LocalDate.now());
        attendance.checkIn(scheduledTimeIn);

        var savedAttendance = attendanceRepository.save(attendance);

        return attendanceMapper.toAttendanceResponse(savedAttendance);
    }


    @Override
    public AttendanceResponse checkOut() {
        var user = currentUserProvider.getCurrentUser();

        Optional<Attendance> attendanceOpt = attendanceRepository
                .findByProfileProfileIdAndDate(user.getProfile().getProfileId(), LocalDate.now());

        if (attendanceOpt.isPresent()) {

            Attendance attendance = attendanceOpt.get();

            if (attendance.getCheckOutStatus() != null)
                throw new AppException(ErrorCode.ATTENDANCE_ALREADY_CHECKED_OUT);

            attendance.checkOut(scheduledTimeOut);

            var savedAttendance = attendanceRepository.save(attendance);
            return attendanceMapper.toAttendanceResponse(savedAttendance);

        } else
            throw new AppException(ErrorCode.ATTENDANCE_NOT_FOUND);
    }


    @Override
    public AttendanceResponse getCurrentAttendanceByUser() {
        var user = currentUserProvider.getCurrentUser();
        var attendance = attendanceRepository
                .findByProfileProfileIdAndDate(user.getProfile().getProfileId(), LocalDate.now())
                .orElse(null);

        if (attendance != null)
            return attendanceMapper.toAttendanceResponse(attendance);
        return null;
    }

    @Override
    public Page<AttendanceResponse> getAttendanceByUser(Pageable pageable) {
        var user = currentUserProvider.getCurrentUser();
        return attendanceRepository
                .findByProfileProfileId(user.getProfile().getProfileId(), pageable)
                .map(attendanceMapper::toAttendanceResponse);
    }

}
