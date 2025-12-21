package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.dto.response.work_request.MentorTaskResponse;
import fpt.is.bnk.fptis_platform.dto.response.work_request.WorkRequestResponse;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Admin 12/19/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface WorkRequestMapper {

    WorkRequest toWorkRequest(WorkRequestRequest workRequestRequest);

    WorkRequestResponse toResponse(WorkRequest workRequest);

    @Mapping(target = "taskId", ignore = true)
    @Mapping(target = "taskCreateTime", ignore = true)
    @Mapping(target = "systemNote", ignore = true)
    @Mapping(target = "requestId", source = "id")
    @Mapping(target = "type", source = "workRequestType")
    @Mapping(target = "internEmail", source = "profile.user.email")
    MentorTaskResponse toMentorTaskResponse(WorkRequest workRequest);

}
