package fpt.is.bnk.fptis_platform.controller.work_request;

import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.service.work_request.WorkRequestService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/19/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/work-requests")
public class WorkRequestController {

    private final WorkRequestService workRequestService;

    @PostMapping
    public ResponseEntity<String> createRequest(@RequestBody WorkRequestRequest request) {
        workRequestService.createRequest(request);
        return ResponseEntity.ok("Đã gửi yêu cầu thành công và khởi chạy quy trình kiểm tra!");
    }

}
