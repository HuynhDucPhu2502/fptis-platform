package fpt.is.bnk.fptis_platform.dto.request.work_request;

import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Admin 12/19/2025
 *
 **/
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkRequestRequest {
    WorkRequestType workRequestType;
    String reason;
    LocalDate fromDate;
    LocalDate toDate;
}