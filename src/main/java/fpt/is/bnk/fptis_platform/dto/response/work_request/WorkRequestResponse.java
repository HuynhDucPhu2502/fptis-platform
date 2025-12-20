package fpt.is.bnk.fptis_platform.dto.response.work_request;

import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestStatus;
import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequestType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Admin 12/20/2025
 *
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkRequestResponse {

    Long id;
    WorkRequestType workRequestType;
    WorkRequestStatus workRequestStatus;
    LocalDate fromDate;
    LocalDate toDate;
    String reason;

    String approverName;
    String adminNote;

    Integer totalAttendance;
    Double onTimeRatio;
    Double earlyCheckoutRatio;
    
}
