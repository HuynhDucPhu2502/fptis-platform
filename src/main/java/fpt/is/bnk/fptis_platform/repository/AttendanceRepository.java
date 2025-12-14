package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Admin 12/12/2025
 *
 **/
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByUserIdAndDate(Long userId, LocalDate date);

    Page<Attendance> findByUserId(Long userId, Pageable pageable);


}
