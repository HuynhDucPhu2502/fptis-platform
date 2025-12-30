package fpt.is.bnk.fptis_platform.service.work_request.impl;

import fpt.is.bnk.fptis_platform.advice.exception.CustomAccessDeniedException;
import fpt.is.bnk.fptis_platform.dto.request.process.TaskCompleteRequest;
import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestStatus;
import fpt.is.bnk.fptis_platform.mapper.WorkRequestMapper;
import fpt.is.bnk.fptis_platform.repository.attendance.AttendanceRepository;
import fpt.is.bnk.fptis_platform.repository.work_request.WorkRequestRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.process.ProcessAccessService;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestOrchestrationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkRequestOrchestrationServiceImpl implements WorkRequestOrchestrationService {

    //  =================================================
    // Metadata
    //  =================================================

    // Process Metadata

    static String BPMN_PROCESS_ID = "intern_work_request_process";
    static String ACTIVITY_MENTOR_REVIEW_ID = "user_task_mentor_review";

    static String VAR_TOTAL_ATTENDANCE = "totalAttendance";
    static String VAR_ON_TIME_RATIO = "onTimeCheckInRatio";

    static String VAR_EARLY_CHECKOUT_RATIO = "earlyCheckoutRatio";

    // General Metadata

    static String VAR_REQUEST_ID = "requestId";

    static String STATUS_ON_TIME = "CHECKED_IN_ON_TIME";
    static String STATUS_LATE = "CHECKED_IN_LATE";
    static String STATUS_EARLY_LEAVE = "CHECKED_OUT_EARLY";

    //  =================================================

    // Provider
    CurrentUserProvider currentUserProvider;

    // Repository
    WorkRequestRepository workRequestRepository;
    AttendanceRepository attendanceRepository;

    // Camunda
    RuntimeService runtimeService;
    TaskService taskService;

    // Mapper
    WorkRequestMapper workRequestMapper;

    // Service
    ProcessAccessService processAccessService;

    @Override
    @Transactional
    public void createRequest(WorkRequestRequest request) {
        User user = currentUserProvider.getCurrentUser();
        WorkRequest workRequest = workRequestMapper.toWorkRequest(request);
        workRequest.setWorkRequestStatus(WorkRequestStatus.PENDING_SYSTEM);
        workRequest.setProfile(user.getProfile());
        var savedWorkRequest = workRequestRepository.save(workRequest);

        Map<String, Object> vars = new HashMap<>();
        vars.put(VAR_REQUEST_ID, savedWorkRequest.getId());

        var processInstance = runtimeService.startProcessInstanceByKey(
                BPMN_PROCESS_ID,
                savedWorkRequest.getId().toString(),
                vars
        );

        workRequest.setProcessInstanceId(processInstance.getId());
        workRequestRepository.save(workRequest);
    }

    @Override
    @Transactional
    public void aggregateStatistics(Long requestId) {
        WorkRequest workRequest = workRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));

        List<Object[]> res = attendanceRepository
                .countStatusByProfileId(workRequest.getProfile().getProfileId());

        Map<String, Long> statsMap = new HashMap<>();
        for (Object[] row : res) {
            statsMap.put((String) row[0], (Long) row[1]);
        }

        long onTimeIn = statsMap.getOrDefault(STATUS_ON_TIME, 0L);
        long lateIn = statsMap.getOrDefault(STATUS_LATE, 0L);
        long earlyOut = statsMap.getOrDefault(STATUS_EARLY_LEAVE, 0L);

        int totalAttendance = (int) (onTimeIn + lateIn);
        double onTimeRatio = totalAttendance > 0 ? (double) onTimeIn / totalAttendance : 0.0;
        double earlyRatio = totalAttendance > 0 ? (double) earlyOut / totalAttendance : 0.0;

        if (workRequest.getProcessInstanceId() != null) {
            Map<String, Object> workflowVars = new HashMap<>();
            workflowVars.put(VAR_TOTAL_ATTENDANCE, totalAttendance);
            workflowVars.put(VAR_ON_TIME_RATIO, onTimeRatio);
            workflowVars.put(VAR_EARLY_CHECKOUT_RATIO, earlyRatio);

            runtimeService.setVariables(workRequest.getProcessInstanceId(), workflowVars);
        }

        workRequest.setTotalAttendance(totalAttendance);
        workRequest.setOnTimeRatio(onTimeRatio);
        workRequest.setEarlyCheckoutRatio(earlyRatio);
        workRequestRepository.save(workRequest);
    }

    @Transactional
    @Override
    public void updateStatus(Long requestId, String status, String reason) {
        WorkRequest workRequest = workRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));
        workRequest.setWorkRequestStatus(WorkRequestStatus.valueOf(status));
        if (reason != null) workRequest.setAdminNote(reason);
        workRequestRepository.save(workRequest);
    }

    @Transactional
    @Override
    public void completeMentorReview(TaskCompleteRequest request) {

        if (!processAccessService.canExecuteTask(
                BPMN_PROCESS_ID,
                ACTIVITY_MENTOR_REVIEW_ID)
        )
            throw new CustomAccessDeniedException("Bạn không có quyền thực hiện bước này");


        Task task = taskService
                .createTaskQuery()
                .taskId(request.getTaskId())
                .singleResult();

        if (task == null)
            throw new RuntimeException("Không tìm thấy Task hoặc Task đã hoàn thành");

        Long requestId = (Long) runtimeService
                .getVariable(task.getExecutionId(), "requestId");

        User mentor = currentUserProvider
                .getCurrentUser();

        WorkRequest workRequest = workRequestRepository
                .findById(requestId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ yêu cầu"));

        workRequest.setApproverName(mentor.getEmail());
        workRequestRepository.save(workRequest);

        taskService.complete(request.getTaskId(), request.getVariables());
    }


    // ==============================================================================
}