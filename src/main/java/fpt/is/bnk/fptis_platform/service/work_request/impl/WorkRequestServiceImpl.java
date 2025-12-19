package fpt.is.bnk.fptis_platform.service.work_request.impl;

import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestStatus;
import fpt.is.bnk.fptis_platform.mapper.WorkRequestMapper;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.repository.WorkRequestRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin 12/19/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkRequestServiceImpl implements fpt.is.bnk.fptis_platform.service.work_request.WorkRequestService {

    // Provider
    CurrentUserProvider currentUserProvider;

    // Repository
    WorkRequestRepository workRequestRepository;
    AttendanceRepository attendanceRepository;

    RuntimeService runtimeService;

    // Mapper
    WorkRequestMapper workRequestMapper;

    @Override
    public void createRequest(WorkRequestRequest request) {
        User user = currentUserProvider.getCurrentUser();

        WorkRequest workRequest = workRequestMapper.toWorkRequest(request);
        workRequest.setWorkRequestStatus(WorkRequestStatus.PENDING_SYSTEM);
        workRequest.setProfile(user.getProfile());


        var savedWorkRequest = workRequestRepository.save(workRequest);


        Map<String, Object> vars = new HashMap<>();
        vars.put("requestId", savedWorkRequest.getId());
        vars.put("profileId", user.getProfile().getProfileId());

        var processInstance = runtimeService.startProcessInstanceByKey(
                "intern_leave_eligibility",
                savedWorkRequest.getId().toString(),
                vars
        );

        workRequest.setProcessInstanceId(processInstance.getId());
        workRequestRepository.save(workRequest);
    }

    @Override
    @Transactional
    public void aggregateStatistics(
            Long requestId,
            DelegateExecution execution
    ) {
        WorkRequest workRequest = workRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));

        List<Object[]> res = attendanceRepository
                .countStatusByProfileId(workRequest.getProfile().getProfileId());

        Map<String, Long> statsMap = new HashMap<>();
        for (Object[] row : res) {
            statsMap.put((String) row[0], (Long) row[1]);
        }

        long onTimeIn = statsMap.getOrDefault("CHECKED_IN_ON_TIME", 0L);
        long lateIn = statsMap.getOrDefault("CHECKED_IN_LATE", 0L);
        long earlyOut = statsMap.getOrDefault("CHECKED_OUT_EARLY", 0L);

        int totalAttendance = (int) (onTimeIn + lateIn);
        double onTimeRatio = totalAttendance > 0 ? (double) onTimeIn / totalAttendance : 0.0;
        double earlyRatio = totalAttendance > 0 ? (double) earlyOut / totalAttendance : 0.0;

        workRequest.setTotalAttendance(totalAttendance);
        workRequest.setOnTimeRatio(onTimeRatio);
        workRequest.setEarlyCheckoutRatio(earlyRatio);
        workRequestRepository.save(workRequest);

        execution.setVariable("totalAttendance", totalAttendance);
        execution.setVariable("onTi meCheckInRatio", onTimeRatio);
        execution.setVariable("earlyCheckoutRatio", earlyRatio);
    }
}
