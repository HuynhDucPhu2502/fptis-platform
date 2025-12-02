package fpt.is.bnk.fptis_platform.mapper;

import fpt.is.bnk.fptis_platform.dto.request.daily_log.CreateDailyLogRequest;
import fpt.is.bnk.fptis_platform.dto.response.daily_log.DailyLogResponse;
import fpt.is.bnk.fptis_platform.entity.DailyLog;
import org.mapstruct.Mapper;

/**
 * Admin 11/28/2025
 *
 **/
@Mapper(componentModel = "spring")
public interface DailyLogMapper {

    DailyLogResponse toDailyLogResponse(DailyLog dailyLog);

    DailyLog toDailyLog(CreateDailyLogRequest dailyLogRequest);

}
