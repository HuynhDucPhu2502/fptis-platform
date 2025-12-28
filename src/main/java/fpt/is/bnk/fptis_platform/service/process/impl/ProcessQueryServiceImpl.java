package fpt.is.bnk.fptis_platform.service.process.impl;

import fpt.is.bnk.fptis_platform.dto.response.process.ProcessDefinitionResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessVariableResponse;
import fpt.is.bnk.fptis_platform.entity.proccess.ProcessDefinition;
import fpt.is.bnk.fptis_platform.entity.proccess.ProcessVersion;
import fpt.is.bnk.fptis_platform.entity.proccess.constant.ResourceType;
import fpt.is.bnk.fptis_platform.repository.process.ProcessDefinitionRepository;
import fpt.is.bnk.fptis_platform.service.process.ProcessQueryService;
import fpt.is.bnk.fptis_platform.service.s3.S3Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin 12/28/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessQueryServiceImpl implements ProcessQueryService {

    // Service
    S3Service s3Service;

    // Repository
    ProcessDefinitionRepository processRepository;

    @Override
    public List<ProcessTaskResponse> getTasksByProcessCode(String processCode) {
        ProcessDefinition def = processRepository.findByProcessCode(processCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quy trình"));

        return def.getTasks().stream()
                .map(task -> ProcessTaskResponse.builder()
                        .taskCode(task.getTaskCode())
                        .taskName(task.getTaskName())
                        .permission(task.getPermissionRole())
                        .isActive(task.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessVariableResponse> getVariablesByProcessCode(String processCode) {
        ProcessDefinition def = processRepository.findByProcessCode(processCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quy trình"));

        return def.getVariables().stream()
                .map(var -> ProcessVariableResponse.builder()
                        .variableName(var.getVariableName())
                        .displayName(var.getDisplayName())
                        .defaultValue(var.getDefaultValue())
                        .dataType(var.getDataType())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ProcessDefinitionResponse> getAllProcesses() {
        return processRepository.findAll().stream()
                .map(process -> {
                    ResourceType latestType = process.getVersions().stream()
                            .max(Comparator.comparing(ProcessVersion::getVersion))
                            .map(ProcessVersion::getResourceType)
                            .orElse(ResourceType.BPMN);

                    return ProcessDefinitionResponse.builder()
                            .id(process.getId())
                            .name(process.getName())
                            .processCode(process.getProcessCode())
                            .activeVersion(process.getActiveVersion())
                            .status(process.getStatus())
                            .resourceType(latestType)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getProcessXmlContent(String processCode) {
        ProcessDefinition processDef = processRepository.findByProcessCode(processCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quy trình với mã: " + processCode));

        String s3Key = processDef.getLatestS3Key();
        if (s3Key == null || s3Key.isEmpty()) {
            throw new RuntimeException("Quy trình chưa có file đính kèm.");
        }

        return s3Service.downloadFileAsText(s3Key);
    }

}
