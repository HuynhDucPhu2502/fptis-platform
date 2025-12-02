package fpt.is.bnk.fptis_platform.controller;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.PageResponse;
import fpt.is.bnk.fptis_platform.dto.request.daily_log.CreateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.request.daily_log.UpdateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import fpt.is.bnk.fptis_platform.service.daily_log.DailyLogService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Admin 11/28/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/daily-logs")
public class DailyLogController {

    DailyLogService dailyLogService;

    @PreAuthorize("hasRole('INTERN_LOG_READ')")
    @GetMapping
    public ApiResponse<PageResponse<DailyLogResponse>> getCurrentUserDailyLogs(
            @PageableDefault Pageable pageable
    ) {
        Page<DailyLogResponse> res = dailyLogService.getCurrentUserDailyLog(pageable);
        PageResponse<DailyLogResponse> pageResponse = new PageResponse<>(res);

        return ApiResponse.<PageResponse<DailyLogResponse>>builder()
                .result(pageResponse)
                .build();
    }

    @PreAuthorize("hasRole('INTERN_LOG_READ')")
    @GetMapping("/{id}")
    public ApiResponse<DailyLogResponse> getDailyLogById(@PathVariable Long id) {
        return ApiResponse.<DailyLogResponse>builder()
                .result(dailyLogService.getDailyLogById(id))
                .build();
    }

    @PreAuthorize("hasRole('INTERN_LOG_CREATE')")
    @PostMapping
    public ApiResponse<DailyLogResponse> createDailyLog(
            @Valid @RequestBody CreateDailyLogRequest request
    ) {
        return ApiResponse.<DailyLogResponse>builder()
                .result(dailyLogService.createDailyLog(request))
                .build();
    }

    @PreAuthorize("hasRole('INTERN_LOG_UPDATE')")
    @PutMapping("/{id}")
    public ApiResponse<DailyLogResponse> updateDailyLog(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDailyLogRequest request
    ) {
        return ApiResponse.<DailyLogResponse>builder()
                .result(dailyLogService.updateDailyLog(id, request))
                .build();
    }

    @PreAuthorize("hasRole('INTERN_LOG_DELETE')")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteDailyLog(@PathVariable Long id) {
        dailyLogService.deleteDailyLog(id);
        return ApiResponse.<Void>builder().build();
    }


}
