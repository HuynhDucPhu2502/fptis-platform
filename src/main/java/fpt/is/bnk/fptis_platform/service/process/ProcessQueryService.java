package fpt.is.bnk.fptis_platform.service.process;

import fpt.is.bnk.fptis_platform.dto.response.process.ProcessDefinitionResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessVariableResponse;

import java.util.List;

/**
 * Admin 12/28/2025
 *
 **/
public interface ProcessQueryService {
    List<ProcessTaskResponse> getTasksByProcessCode(String processCode);

    List<ProcessVariableResponse> getVariablesByProcessCode(String processCode);

    List<ProcessDefinitionResponse> getAllProcesses();

    String getProcessXmlContent(String processCode);
}
