package fpt.is.bnk.fptis_platform.controller.work_request;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.request.process.TaskCompleteRequest;
import fpt.is.bnk.fptis_platform.dto.response.work_request.MentorTaskResponse;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestService;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestOrchestrationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin 12/20/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/mentor/work-requests")
public class MentorWorkRequestController {

    WorkRequestService workRequestService;
    WorkRequestOrchestrationService workRequestOrchestrationService;

    @GetMapping
    public ApiResponse<List<MentorTaskResponse>> getPendingTasks() {
        return ApiResponse.<List<MentorTaskResponse>>builder()
                .result(workRequestService.getPendingMentorTasks())
                .build();
    }

    @PostMapping
    public ApiResponse<String> completeTask(@RequestBody TaskCompleteRequest request) {
        workRequestOrchestrationService.completeMentorReview(request);
        return ApiResponse.<String>builder()
                .result("Thành công")
                .build();
    }

}
