package fpt.is.bnk.fptis_platform.dto.response.process;

import fpt.is.bnk.fptis_platform.entity.proccess.constant.ProcessStatus;
import fpt.is.bnk.fptis_platform.entity.proccess.constant.ResourceType;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/25/2025
 *
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessDefinitionResponse {
    Long id;
    String name;
    String processCode;
    Integer activeVersion;
    ProcessStatus status;
    ResourceType resourceType;
}