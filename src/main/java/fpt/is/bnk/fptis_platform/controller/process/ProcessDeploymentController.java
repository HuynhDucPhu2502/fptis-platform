package fpt.is.bnk.fptis_platform.controller.process;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.request.process.ProcessVariableUpdateRequest;
import fpt.is.bnk.fptis_platform.service.process.ProcessDeploymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Admin 12/25/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/processes")
public class ProcessDeploymentController {

    ProcessDeploymentService processDeploymentService;

    @PostMapping(value = "/deploy", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> deploy(
            @RequestPart("request") ProcessDeployRequest request,
            @RequestPart("file") MultipartFile file
    ) {
        String deploymentId = processDeploymentService.deployNewProcess(request, file);
        return ApiResponse.<String>builder()
                .result("Triển khai thành công. Deployment ID: " + deploymentId)
                .build();
    }

    @PutMapping("/{processCode}/variables/defaults")
    public ApiResponse<String> updateVariableDefaults(
            @PathVariable String processCode,
            @RequestBody List<ProcessVariableUpdateRequest> request
    ) {

        processDeploymentService.updateVariableDefaults(processCode, request);

        return ApiResponse.<String>builder()
                .result("Cập nhật giá trị mặc định thành công")
                .build();
    }


}
