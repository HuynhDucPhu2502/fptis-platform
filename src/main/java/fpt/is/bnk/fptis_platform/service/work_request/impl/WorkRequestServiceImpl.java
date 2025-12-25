package fpt.is.bnk.fptis_platform.service.work_request.impl;

import fpt.is.bnk.fptis_platform.dto.response.work_request.MentorTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.work_request.WorkRequestResponse;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import fpt.is.bnk.fptis_platform.mapper.WorkRequestMapper;
import fpt.is.bnk.fptis_platform.repository.work_request.WorkRequestRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Admin 12/20/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WorkRequestServiceImpl implements WorkRequestService {

    static String MENTOR_REVIEW_ACTIVITY_ID = "user_task_mentor_review";

    // Provider
    CurrentUserProvider currentUserProvider;

    // Repository
    WorkRequestRepository workRequestRepository;

    // Flow
    RuntimeService runtimeService;

    // Mapper
    WorkRequestMapper workRequestMapper;
    TaskService taskService;


    @Override
    public List<MentorTaskResponse> getPendingMentorTasks() {
        List<Task> tasks = taskService.createTaskQuery()
                .taskDefinitionKey(MENTOR_REVIEW_ACTIVITY_ID)
                .active()
                .list();

        if (tasks.isEmpty()) return List.of();

        Map<String, Task> taskMap = new HashMap<>();
        for (Task task : tasks) {
            Long requestId = (Long) runtimeService.getVariable(task.getExecutionId(), "requestId");

            if (requestId != null)
                taskMap.put(requestId.toString(), task);
        }

        List<Long> ids = taskMap.keySet().stream().map(Long::valueOf).toList();
        List<WorkRequest> workRequests = workRequestRepository.findAllById(ids);

        return workRequests.stream().map(request -> {
            Task task = taskMap.get(request.getId().toString());

            MentorTaskResponse response = workRequestMapper.toMentorTaskResponse(request);

            response.setTaskId(task.getId());
            response.setTaskCreateTime(
                    task.getCreateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            );

            Map<String, Object> dmnResult = (Map<String, Object>) runtimeService
                    .getVariable(task.getExecutionId(), "isEligibleForLeave");
            if (dmnResult != null)
                response.setSystemNote((String) dmnResult.get("reason"));


            return response;
        }).toList();
    }

    @Override
    public List<WorkRequestResponse> getCurrentUserWorkRequests() {
        User user = currentUserProvider.getCurrentUser();
        List<WorkRequest> myRequests = workRequestRepository
                .findByProfileProfileId(user.getProfile().getProfileId());

        return myRequests.stream()
                .map(workRequestMapper::toResponse)
                .toList();
    }

}
