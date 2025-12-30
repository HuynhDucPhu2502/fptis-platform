package fpt.is.bnk.fptis_platform.dto.request.process;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Admin 12/30/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TaskActionsUpdateRequest {

    String processCode;
    String taskCode;
    List<ActionButtonRequest> buttons;

}
