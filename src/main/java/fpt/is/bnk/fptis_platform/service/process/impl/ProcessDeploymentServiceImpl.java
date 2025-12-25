package fpt.is.bnk.fptis_platform.service.process.impl;

import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessDefinitionResponse;
import fpt.is.bnk.fptis_platform.dto.response.process.ProcessTaskResponse;
import fpt.is.bnk.fptis_platform.entity.proccess.*;
import fpt.is.bnk.fptis_platform.repository.process.ProcessDefinitionRepository;
import fpt.is.bnk.fptis_platform.repository.process.ProcessVersionRepository;
import fpt.is.bnk.fptis_platform.service.s3.S3Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Admin 12/25/2025
 * Cập nhật: Hỗ trợ tự động phân loại ResourceType và quản lý Version
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessDeploymentServiceImpl implements fpt.is.bnk.fptis_platform.service.process.ProcessDeploymentService {

    S3Service s3Service;
    RepositoryService repositoryService;
    ProcessDefinitionRepository processRepo;
    ProcessVersionRepository versionRepo;

    @Transactional
    @Override
    public String deployNewProcess(ProcessDeployRequest request, MultipartFile file) {
        try {
            // ==========================
            // 1. Khởi tạo/Lấy Process Definition
            // ==========================
            ProcessDefinition processDef = processRepo
                    .findByProcessCode(request.getProcessCode())
                    .orElseGet(() -> {
                        ProcessDefinition newDef = new ProcessDefinition();
                        newDef.setProcessCode(request.getProcessCode());
                        newDef.setName(request.getName());
                        newDef.setCamundaProcessKey(request.getCamundaProcessKey());
                        newDef.setActiveVersion(0);
                        newDef.setStatus(ProcessStatus.DRAFT);
                        return processRepo.save(newDef);
                    });

            // ==========================
            // 2. Xử lý File & Loại tài nguyên
            // ==========================
            String fileName = file.getOriginalFilename();
            ResourceType resourceType = (fileName != null && fileName.toLowerCase().endsWith(".dmn"))
                    ? ResourceType.DMN : ResourceType.BPMN;

            // ==========================
            // 3. Upload S3 (Lưu trữ lịch sử)
            // ==========================
            String s3Key = s3Service.uploadFile(
                    file,
                    "workflows/" + request.getProcessCode() + "/" + resourceType.name(),
                    fileName,
                    false,
                    5000000L
            );

            // ==========================
            // 4. Deploy sang Camunda (Server A)
            // ==========================
            Deployment deployment = repositoryService.createDeployment()
                    .name("Deploy_" + request.getProcessCode() + "_" + resourceType.name())
                    .addInputStream(fileName, file.getInputStream())
                    .deploy();

            // ==========================
            // 5. Lưu Process Version
            // ==========================
            // Lấy version hiện tại cao nhất của loại tài nguyên này để tăng lên 1
            int currentMaxVersion = versionRepo
                    .findByProcessIdAndResourceTypeOrderByVersionDesc(
                            processDef.getId(),
                            resourceType
                    )
                    .stream()
                    .findFirst()
                    .map(ProcessVersion::getVersion).
                    orElse(0);

            ProcessVersion version = new ProcessVersion();
            version.setProcess(processDef);
            version.setResourceType(resourceType);
            version.setVersion(currentMaxVersion + 1);
            version.setDeploymentId(deployment.getId());
            version.setS3Key(s3Key);
            version.setDescription(request.getDescription());
            version.setDeployedAt(LocalDateTime.now());
            versionRepo.save(version);

            // ==========================
            // 6. Cập nhật Master Data (Definition)
            // ==========================
            if (resourceType == ResourceType.BPMN) {
                processDef.setActiveVersion(version.getVersion());
                processDef.setLatestS3Key(s3Key);
            }
            processDef.setLatestDeploymentId(deployment.getId());
            processDef.setStatus(ProcessStatus.ACTIVE);
            processRepo.save(processDef);

            return deployment.getId();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi xử lý file quy trình: " + e.getMessage());
        }
    }


    @Override
    public List<ProcessTaskResponse> getTasksByProcessCode(String processCode) {
        ProcessDefinition def = processRepo.findByProcessCode(processCode)
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
    public List<ProcessDefinitionResponse> getAllProcesses() {
        return processRepo.findAll().stream()
                .map(process -> ProcessDefinitionResponse.builder()
                        .id(process.getId())
                        .name(process.getName())
                        .processCode(process.getProcessCode())
                        .camundaProcessKey(process.getCamundaProcessKey())
                        .activeVersion(process.getActiveVersion())
                        .status(process.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

}