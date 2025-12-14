package fpt.is.bnk.fptis_platform.dto.response.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Admin 12/12/2025
 *
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {

    private Long id;
    private Long userId;
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private String checkInStatus;
    private String checkOutStatus;
}
