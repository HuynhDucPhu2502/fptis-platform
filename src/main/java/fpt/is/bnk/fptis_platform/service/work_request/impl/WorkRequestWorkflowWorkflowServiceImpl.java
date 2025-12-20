package fpt.is.bnk.fptis_platform.service.work_request.impl;

import fpt.is.bnk.fptis_platform.dto.request.work_request.MentorReviewRequest;
import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestStatus;
import fpt.is.bnk.fptis_platform.mapper.WorkRequestMapper;
import fpt.is.bnk.fptis_platform.repository.AttendanceRepository;
import fpt.is.bnk.fptis_platform.repository.WorkRequestRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestWorkflowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.task.Task;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;

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
public class WorkRequestWorkflowWorkflowServiceImpl implements WorkRequestWorkflowService {

    static String PROCESS_ID = "intern_work_request_process";
    static String MENTOR_REVIEW_ACTIVITY_ID = "user_task_mentor_review";

    // Provider
    CurrentUserProvider currentUserProvider;

    // Repository
    WorkRequestRepository workRequestRepository;
    AttendanceRepository attendanceRepository;

    // Flow
    RuntimeService runtimeService;

    // Mapper
    WorkRequestMapper workRequestMapper;
    TaskService taskService;

    // Mail Sender
    JavaMailSender javaMailSender;
    TemplateEngine templateEngine;

    @Override
    @Transactional
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
                PROCESS_ID,
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
        execution.setVariable("onTimeCheckInRatio", onTimeRatio);
        execution.setVariable("earlyCheckoutRatio", earlyRatio);
    }

    @Transactional
    @Override
    public void updateStatus(Long requestId, String status, String reason) {
        WorkRequest workRequest = workRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));

        workRequest.setWorkRequestStatus(WorkRequestStatus.valueOf(status));

        if (reason != null)
            workRequest.setAdminNote(reason);

        workRequestRepository.save(workRequest);

    }

    @Transactional
    @Override
    public void completeMentorReview(MentorReviewRequest reviewRequest) {
        Task task = taskService.createTaskQuery()
                .taskId(reviewRequest.getTaskId())
                .singleResult();

        if (task == null)
            throw new RuntimeException("Không tìm thấy Task này");


        Long requestId = (Long) runtimeService.getVariable(task.getExecutionId(), "requestId");
        User mentor = currentUserProvider.getCurrentUser();

        WorkRequest workRequest = workRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu: " + requestId));

        workRequest.setApproverName(mentor.getEmail());
        workRequestRepository.save(workRequest);

        Map<String, Object> variables = new HashMap<>();
        variables.put("isApproved", reviewRequest.isApproved());
        variables.put("mentorComment", reviewRequest.getComment());

        taskService.complete(reviewRequest.getTaskId(), variables);
    }

}
