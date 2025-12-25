package fpt.is.bnk.fptis_platform.dto.response.process;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessStatus;
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
    String camundaProcessKey;
    Integer activeVersion;
    ProcessStatus status;
}