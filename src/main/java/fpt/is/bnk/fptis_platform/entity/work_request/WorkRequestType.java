package fpt.is.bnk.fptis_platform.entity.work_request;

import lombok.Getter;

/**
 * Admin 12/19/2025
 *
 **/
@Getter
public enum WorkRequestType {
    LEAVE("Nghỉ phép"),
    REMOTE("Làm việc từ xa"),
    MISSION("Đi công tác");

    private final String description;

    WorkRequestType(String description) {
        this.description = description;
    }
}
