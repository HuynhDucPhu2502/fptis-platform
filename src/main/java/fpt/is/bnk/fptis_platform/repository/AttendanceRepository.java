package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByProfileProfileIdAndDate(Long profileId, LocalDate date);

    Page<Attendance> findByProfileProfileId(Long profileId, Pageable pageable);

    List<Attendance> findByProfileProfileId(Long profileId);

    @Query("SELECT 'CHECKED_IN_ON_TIME' AS status, COUNT(a) AS count " +
            "FROM Attendance a WHERE a.profile.profileId = :profileId AND a.checkInStatus = 'CHECKED_IN_ON_TIME' " +
            "UNION ALL " +
            "SELECT 'CHECKED_IN_LATE' AS status, COUNT(a) AS count " +
            "FROM Attendance a WHERE a.profile.profileId = :profileId AND a.checkInStatus = 'CHECKED_IN_LATE' " +
            "UNION ALL " +
            "SELECT 'CHECKED_OUT_ON_TIME' AS status, COUNT(a) AS count " +
            "FROM Attendance a WHERE a.profile.profileId = :profileId AND a.checkOutStatus = 'CHECKED_OUT_ON_TIME' " +
            "UNION ALL " +
            "SELECT 'CHECKED_OUT_EARLY' AS status, COUNT(a) AS count " +
            "FROM Attendance a WHERE a.profile.profileId = :profileId AND a.checkOutStatus = 'CHECKED_OUT_EARLY'")
    List<Object[]> countStatusByProfileId(Long profileId);
}