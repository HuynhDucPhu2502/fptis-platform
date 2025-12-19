package fpt.is.bnk.fptis_platform.service.work_request;

import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 12/19/2025
 *
 **/
public interface WorkRequestService {
    void createRequest(WorkRequestRequest request);

    void aggregateStatistics(
            Long requestId,
            DelegateExecution execution
    );

    @Transactional
    void updateStatus(Long requestId, String status, String reason);
}
