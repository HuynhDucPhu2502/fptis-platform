package fpt.is.bnk.fptis_platform.controller.work_request;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import fpt.is.bnk.fptis_platform.dto.response.work_request.WorkRequestResponse;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestService;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestWorkflowService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin 12/19/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/intern/work-requests")
public class InternWorkRequestController {

    WorkRequestService workRequestService;
    WorkRequestWorkflowService workRequestWorkflowService;

    @PostMapping
    public ApiResponse<String> createRequest(@RequestBody WorkRequestRequest request) {
        workRequestWorkflowService.createRequest(request);
        return ApiResponse.<String>builder()
                .result("Đã gửi yêu cầu thành công và khởi chạy quy trình kiểm tra!")
                .build();
    }

    @GetMapping
    public ApiResponse<List<WorkRequestResponse>> getMyHistory() {
        return ApiResponse.<List<WorkRequestResponse>>builder()
                .result(workRequestService.getMyRequests())
                .build();
    }

}
