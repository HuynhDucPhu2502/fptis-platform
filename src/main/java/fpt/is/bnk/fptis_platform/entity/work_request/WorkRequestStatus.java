package fpt.is.bnk.fptis_platform.entity.work_request;

import lombok.Getter;

/**
 * Admin 12/19/2025
 *
 **/
@Getter
public enum WorkRequestStatus {

    PENDING_SYSTEM("Đang xác thực điều kiện"),
    PENDING_MENTOR("Chờ Mentor phê duyệt"),
    APPROVED("Đã phê duyệt"),
    REJECTED("Bị từ chối");

    private final String description;

    WorkRequestStatus(String description) {
        this.description = description;
    }
}
