package fpt.is.bnk.fptis_platform.service.process;

import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.request.process.ProcessVariableUpdateRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Admin 12/25/2025
 *
 **/
public interface ProcessDeploymentService {
    @Transactional
    String deployNewProcess(ProcessDeployRequest request, MultipartFile file);

    @Transactional
    void updateVariableDefaults(String processCode, List<ProcessVariableUpdateRequest> updates);
}
