package fpt.is.bnk.fptis_platform.service.daily_log;

import fpt.is.bnk.fptis_platform.dto.request.daily_log.CreateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.request.daily_log.UpdateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Admin 11/28/2025
 *
 **/
public interface DailyLogService {

    Page<DailyLogResponse> getCurrentUserDailyLog(Pageable pageable);

    DailyLogResponse createDailyLog(CreateDailyLogRequest request);

    DailyLogResponse getDailyLogById(Long id);

    DailyLogResponse updateDailyLog(Long id, UpdateDailyLogRequest request);

    void deleteDailyLog(Long id);
}
