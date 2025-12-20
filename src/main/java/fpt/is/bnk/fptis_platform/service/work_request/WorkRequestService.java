package fpt.is.bnk.fptis_platform.service.work_request;

import fpt.is.bnk.fptis_platform.dto.response.work_request.MentorTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.work_request.WorkRequestResponse;

import java.util.List;

/**
 * Admin 12/20/2025
 *
 **/
public interface WorkRequestService {
    List<MentorTaskResponse> getPendingMentorTasks();

    List<WorkRequestResponse> getMyRequests();
}
