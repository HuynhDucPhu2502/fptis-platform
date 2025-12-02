package fpt.is.bnk.fptis_platform.service.daily_log.impl;

import fpt.is.bnk.fptis_platform.dto.request.daily_log.CreateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.request.daily_log.UpdateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import fpt.is.bnk.fptis_platform.entity.DailyLog;
import fpt.is.bnk.fptis_platform.mapper.DailyLogMapper;
import fpt.is.bnk.fptis_platform.repository.DailyLogRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.daily_log.DailyLogService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 11/28/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DailyLogServiceImpl implements DailyLogService {

    DailyLogRepository dailyLogRepository;
    CurrentUserProvider currentUserProvider;
    DailyLogMapper dailyLogMapper;


    @Override
    public Page<DailyLogResponse> getCurrentUserDailyLog(Pageable pageable) {
        Long profileId = currentUserProvider.getCurrentUser().getProfile().getProfileId();

        return dailyLogRepository
                .findDailyLogByProfile_ProfileId(profileId, pageable)
                .map(dailyLogMapper::toDailyLogResponse);
    }

    @Override
    public DailyLogResponse createDailyLog(CreateDailyLogRequest request) {
        var profile = currentUserProvider.getCurrentUser().getProfile();

        var entity = dailyLogMapper.toDailyLog(request);
        entity.setProfile(profile);

        var saved = dailyLogRepository.save(entity);

        return dailyLogMapper.toDailyLogResponse(saved);

    }

    @Override
    public DailyLogResponse getDailyLogById(Long id) {
        Long profileId = currentUserProvider.getCurrentUser().getProfile().getProfileId();

        DailyLog entity = dailyLogRepository.findByIdAndProfile_ProfileId(id, profileId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhật ký"));

        return dailyLogMapper.toDailyLogResponse(entity);
    }

    @Override
    public DailyLogResponse updateDailyLog(Long id, UpdateDailyLogRequest request) {
        Long profileId = currentUserProvider.getCurrentUser().getProfile().getProfileId();

        DailyLog entity = dailyLogRepository.findByIdAndProfile_ProfileId(id, profileId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhật ký"));

        entity.setMainTask(request.getMainTask());
        entity.setResult(request.getResult());

        DailyLog saved = dailyLogRepository.save(entity);
        return dailyLogMapper.toDailyLogResponse(saved);

    }

    @Override
    public void deleteDailyLog(Long id) {
        Long profileId = currentUserProvider.getCurrentUser().getProfile().getProfileId();

        DailyLog entity = dailyLogRepository.findByIdAndProfile_ProfileId(id, profileId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhật ký"));

        dailyLogRepository.delete(entity);
    }


}
