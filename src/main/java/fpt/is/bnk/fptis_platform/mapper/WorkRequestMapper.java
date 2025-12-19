package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.request.work_request.WorkRequestRequest;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import org.mapstruct.Mapper;

/**
 * Admin 12/19/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface WorkRequestMapper {

    WorkRequest toWorkRequest(WorkRequestRequest workRequestRequest);

}
