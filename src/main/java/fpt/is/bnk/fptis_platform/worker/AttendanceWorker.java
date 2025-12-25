package fpt.is.bnk.fptis_platform.worker;

import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import fpt.is.bnk.fptis_platform.repository.work_request.WorkRequestRepository;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestOrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Admin 12/23/2025
 *
 **/
@ExternalTaskSubscription("aggregate-attendance-stats")
@Component
@RequiredArgsConstructor
@Slf4j
public class AttendanceWorker implements ExternalTaskHandler {

    private final WorkRequestOrchestrationService orchestrationService;
    private final WorkRequestRepository workRequestRepository;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Long requestId = externalTask.getVariable("requestId");

        orchestrationService.aggregateStatistics(requestId);

        WorkRequest wr = workRequestRepository.findById(requestId).orElseThrow();

        Map<String, Object> variables = Map.of(
                "totalAttendance", wr.getTotalAttendance(),
                "onTimeCheckInRatio", wr.getOnTimeRatio(),
                "earlyCheckoutRatio", wr.getEarlyCheckoutRatio()
        );

        externalTaskService.complete(externalTask, variables);
    }
}
