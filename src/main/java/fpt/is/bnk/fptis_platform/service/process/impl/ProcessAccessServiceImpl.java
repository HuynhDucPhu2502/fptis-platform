package fpt.is.bnk.fptis_platform.service.process.impl;

import fpt.is.bnk.fptis_platform.advice.exception.CustomEntityNotFoundException;
import fpt.is.bnk.fptis_platform.entity.proccess.ProcessTask;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.process.ProcessTaskRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.process.ProcessAccessService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

/**
 * Admin 12/29/2025
 *
 **/
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProcessAccessServiceImpl implements ProcessAccessService {

    ProcessTaskRepository processTaskRepository;
    CurrentUserProvider currentUserProvider;

    @Override
    public boolean canExecuteTask(String processCode, String taskCode) {
        User user = currentUserProvider.getCurrentUser();

        ProcessTask taskConfig = processTaskRepository
                .findByProcessProcessCodeAndTaskCode(processCode, taskCode)
                .orElseThrow(() -> new CustomEntityNotFoundException("Task chưa được cấu hình quyền"));

        if (taskConfig.getPermissionRole() == null || taskConfig.getPermissionRole().isEmpty())
            return true;

        return user
                .getRoles()
                .stream()
                .anyMatch(role -> role.getName().equals(taskConfig.getPermissionRole()));
    }
}
