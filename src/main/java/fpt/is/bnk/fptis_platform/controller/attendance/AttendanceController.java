package fpt.is.bnk.fptis_platform.controller.attendance;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.PageResponse;
import fpt.is.bnk.fptis_platform.dto.response.attendance.AttendanceResponse;
import fpt.is.bnk.fptis_platform.service.attendance.AttendanceService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/12/2025
 *
 **/
@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AttendanceController {

    AttendanceService attendanceService;

    @PostMapping("/check-in")
    public ApiResponse<AttendanceResponse> checkIn() {
        try {
            AttendanceResponse attendance = attendanceService.checkIn();
            return ApiResponse.<AttendanceResponse>builder()
                    .result(attendance)
                    .build();
        } catch (IllegalArgumentException e) {

            return ApiResponse.<AttendanceResponse>builder()
                    .result(null)
                    .build();
        }
    }

    @PostMapping("/check-out")
    public ApiResponse<AttendanceResponse> checkOut() {
        try {
            AttendanceResponse attendance = attendanceService.checkOut();
            return ApiResponse.<AttendanceResponse>builder()
                    .result(attendance)
                    .build();
        } catch (IllegalArgumentException e) {

            return ApiResponse.<AttendanceResponse>builder()
                    .result(null)
                    .build();

        }
    }

    @GetMapping("/current")
    public ApiResponse<AttendanceResponse> getCurrentAttendance() {
        var res = attendanceService.getCurrentAttendanceByUser();

        return ApiResponse.<AttendanceResponse>builder()
                .result(res)
                .build();
    }

    @GetMapping("/history")
    public ApiResponse<PageResponse<AttendanceResponse>> getAttendanceHistory(
            @PageableDefault Pageable pageable) {
        Page<AttendanceResponse> res = attendanceService.getAttendanceByUser(pageable);
        PageResponse<AttendanceResponse> pageResponse = new PageResponse<>(res);

        return ApiResponse.<PageResponse<AttendanceResponse>>builder()
                .result(pageResponse)
                .build();
    }

}
