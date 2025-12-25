package fpt.is.bnk.fptis_platform.worker;

import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestOrchestrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

/**
 * Admin 12/23/2025
 *
 **/
@ExternalTaskSubscription("update-request-status")
@Component
@RequiredArgsConstructor
@Slf4j
public class StatusUpdateWorker implements ExternalTaskHandler {

    private final WorkRequestOrchestrationService orchestrationService;

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
        Long requestId = externalTask.getVariable("requestId");

        String status = externalTask.getVariable("targetStatus");

        orchestrationService.updateStatus(requestId, status, "Hệ thống tự động thực hiện");

        externalTaskService.complete(externalTask);
    }
}
