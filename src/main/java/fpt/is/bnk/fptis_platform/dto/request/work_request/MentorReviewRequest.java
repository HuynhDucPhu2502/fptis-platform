package fpt.is.bnk.fptis_platform.dto.request.work_request;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/20/2025
 *
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MentorReviewRequest {

    String taskId;
    boolean approved;
    String comment;

}
