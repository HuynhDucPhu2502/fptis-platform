package fpt.is.bnk.fptis_platform.service.process;

import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessDefinitionResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessVariableResponse;
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

    List<ProcessTaskResponse> getTasksByProcessCode(String processCode);

    List<ProcessVariableResponse> getVariablesByProcessCode(String processCode);

    List<ProcessDefinitionResponse> getAllProcesses();

    String getProcessXmlContent(String processCode);
}
