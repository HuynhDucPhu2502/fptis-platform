package fpt.is.bnk.fptis_platform.service.process.impl;

import fpt.is.bnk.fptis_platform.advice.exception.CustomEntityNotFoundException;
import fpt.is.bnk.fptis_platform.dto.request.process.ProcessDeployRequest;
import fpt.is.bnk.fptis_platform.dto.request.process.TaskPermissionRequest;
import fpt.is.bnk.fptis_platform.entity.proccess.*;
import fpt.is.bnk.fptis_platform.entity.proccess.constant.ProcessStatus;
import fpt.is.bnk.fptis_platform.entity.proccess.constant.ResourceType;
import fpt.is.bnk.fptis_platform.repository.process.ProcessDefinitionRepository;
import fpt.is.bnk.fptis_platform.repository.process.ProcessTaskRepository;
import fpt.is.bnk.fptis_platform.repository.process.ProcessVariableRepository;
import fpt.is.bnk.fptis_platform.repository.process.ProcessVersionRepository;
import fpt.is.bnk.fptis_platform.service.process.ProcessDeploymentService;
import fpt.is.bnk.fptis_platform.service.s3.S3Service;
import fpt.is.bnk.fptis_platform.util.WorkflowParserUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.repository.Deployment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Admin 12/25/2025
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessDeploymentServiceImpl implements ProcessDeploymentService {

    // Service
    S3Service s3Service;
    RepositoryService repositoryService;

    // Repository
    ProcessDefinitionRepository processRepository;
    ProcessVersionRepository versionRepository;
    ProcessTaskRepository processTaskRepository;
    ProcessVariableRepository processVariableRepository;

    @Transactional
    @Override
    public String deployNewProcess(ProcessDeployRequest request, MultipartFile file) {
        try {
            // ==========================
            // 1. Khởi tạo/Lấy Process Definition
            // ==========================
            ProcessDefinition processDef = processRepository
                    .findByProcessCode(request.getProcessCode())
                    .orElseGet(() -> {
                        ProcessDefinition newDef = new ProcessDefinition();
                        newDef.setProcessCode(request.getProcessCode());
                        newDef.setName(request.getName());
                        newDef.setActiveVersion(0);
                        newDef.setStatus(ProcessStatus.DRAFT);
                        return processRepository.save(newDef);
                    });

            // Đọc file vào mảng byte để dùng nhiều lần
            byte[] fileBytes = file.getBytes();

            // ==========================
            // 2. Xác định Loại tài nguyên
            // ==========================
            String fileName = file.getOriginalFilename();
            ResourceType resourceType = (fileName != null && fileName.toLowerCase().endsWith(".dmn"))
                    ? ResourceType.DMN : ResourceType.BPMN;

            // ==========================
            // 3. Upload S3
            // ==========================
            String s3Key = s3Service.uploadFile(
                    file,
                    "workflows/" + request.getProcessCode() + "/" + resourceType.name(),
                    fileName,
                    false,
                    5000000L
            );

            // ==========================
            // 4. Deploy sang Camunda Engine
            // ==========================
            Deployment deployment = repositoryService.createDeployment()
                    .name("Deploy_" + request.getProcessCode() + "_" + resourceType.name())
                    .addInputStream(fileName, new ByteArrayInputStream(fileBytes)) // Dùng byte array
                    .deploy();

            // ==========================
            // 5. Bóc tách và Lưu cấu hình chi tiết (Tasks/Variables)
            // ==========================
            if (resourceType == ResourceType.BPMN) {
                // Xóa Task cũ của quy trình này
                processTaskRepository.deleteByProcessId(processDef.getId());

                // Parse và lưu Task mới
                List<ProcessTask> tasks = WorkflowParserUtils.extractBpmnTasks(new ByteArrayInputStream(fileBytes));
                tasks.forEach(t -> {
                    t.setProcess(processDef);
                    t.setActive(true);
                });
                processTaskRepository.saveAll(tasks);
            } else {
                // Xóa Variable cũ của quy trình này
                processVariableRepository.deleteByProcessId(processDef.getId());

                // Parse và lưu Variable mới
                List<ProcessVariable> variables = WorkflowParserUtils.extractDmnVariables(new ByteArrayInputStream(fileBytes));
                variables.forEach(v -> v.setProcess(processDef));
                processVariableRepository.saveAll(variables);
            }

            // ==========================
            // 6. Quản lý Version & Master Data
            // ==========================
            int currentMaxVersion = versionRepository
                    .findByProcessIdAndResourceTypeOrderByVersionDesc(processDef.getId(), resourceType)
                    .stream().findFirst()
                    .map(ProcessVersion::getVersion).orElse(0);

            ProcessVersion version = new ProcessVersion();
            version.setProcess(processDef);
            version.setResourceType(resourceType);
            version.setVersion(currentMaxVersion + 1);
            version.setDeploymentId(deployment.getId());
            version.setS3Key(s3Key);
            version.setDeployedAt(LocalDateTime.now());
            versionRepository.save(version);

            // Cập nhật Master Data
            processDef.setActiveVersion(version.getVersion());

            String resolvedName = Optional
                    .ofNullable(request.getName())
                    .filter(s -> !s.isBlank())
                    .orElseGet(() ->
                            Optional
                                    .ofNullable(processDef.getName())
                                    .filter(s -> !s.isBlank())
                                    .orElse(processDef.getProcessCode())
                    );
            processDef.setName(resolvedName);

            processDef.setLatestS3Key(s3Key);
            processDef.setLatestDeploymentId(deployment.getId());
            processDef.setStatus(ProcessStatus.ACTIVE);
            processRepository.save(processDef);

            return deployment.getId();
        } catch (IOException e) {
            throw new RuntimeException("Lỗi triển khai quy trình: " + e.getMessage());
        }
    }

    @Override
    public void updateTaskPermission(TaskPermissionRequest request) {
        ProcessTask task = processTaskRepository
                .findByProcessProcessCodeAndTaskCode(request.getProcessCode(), request.getTaskCode())
                .orElseThrow(() -> new CustomEntityNotFoundException("Không tìm thấy Task: " + request.getTaskCode()));

        task.setPermissionRole(request.getPermissionRole());

        processTaskRepository.save(task);
    }
}