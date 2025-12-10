package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.DailyLog;
import fpt.is.bnk.fptis_platform.entity.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Admin 11/28/2025
 *
 **/
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    Page<DailyLog> findDailyLogByProfile_ProfileId(Long profileProfileId,
                                                   Pageable pageable);

    Optional<DailyLog> findByIdAndProfile_ProfileId(Long id, Long profileUserId);

    List<DailyLog> findByProfile_ProfileId(Long profileProfileId);


}
