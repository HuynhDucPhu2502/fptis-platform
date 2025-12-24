package fpt.is.bnk.fptis_platform.repository.daily_log;

import fpt.is.bnk.fptis_platform.entity.daily_log.DailyLog;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * Admin 11/28/2025
 *
 **/
@Repository
public interface DailyLogRepository extends JpaRepository<DailyLog, Long> {

    Page<DailyLog> findDailyLogByProfile_ProfileId(Long profileProfileId,
                                                   Pageable pageable);

    Optional<DailyLog> findByIdAndProfile_ProfileId(Long id, Long profileUserId);

    @QueryHints(value = @QueryHint(name = org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE, value = "500"))
    @Transactional(readOnly = true)
    Stream<DailyLog> streamAllByProfile_ProfileId(Long profileId);


}
