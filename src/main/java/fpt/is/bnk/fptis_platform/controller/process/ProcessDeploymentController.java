package fpt.is.bnk.fptis_platform.controller.process;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessDefinitionResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessVariableResponse;
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

    @GetMapping("/{processCode}/tasks")
    public ApiResponse<List<ProcessTaskResponse>> getTasks(@PathVariable String processCode) {
        List<ProcessTaskResponse> tasks = processDeploymentService.
                getTasksByProcessCode(processCode);

        return ApiResponse.<List<ProcessTaskResponse>>builder()
                .result(tasks)
                .build();
    }

    @GetMapping("/{processCode}/variables")
    public ApiResponse<List<ProcessVariableResponse>> getVariables(@PathVariable String processCode) {
        List<ProcessVariableResponse> tasks = processDeploymentService.
                getVariablesByProcessCode(processCode);

        return ApiResponse.<List<ProcessVariableResponse>>builder()
                .result(tasks)
                .build();
    }

    @GetMapping("/all")
    public ApiResponse<List<ProcessDefinitionResponse>> getAllProcesses() {
        List<ProcessDefinitionResponse> processes = processDeploymentService.getAllProcesses();
        return ApiResponse.<List<ProcessDefinitionResponse>>builder()
                .result(processes)
                .build();
    }


}
