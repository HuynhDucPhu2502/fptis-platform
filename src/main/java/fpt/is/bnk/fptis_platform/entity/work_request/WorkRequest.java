package fpt.is.bnk.fptis_platform.entity.work_request;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import fpt.is.bnk.fptis_platform.entity.user.Profile;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

/**
 * Admin 12/19/2025
 *
 **/
@Entity
@Table(name = "work_request_logs")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WorkRequest extends BaseEntity {

    // ------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_request_type", nullable = false)
    WorkRequestType workRequestType;

    @Enumerated(EnumType.STRING)
    @Column(name = "work_request_status")
    WorkRequestStatus workRequestStatus;

    // ------
    @Column(name = "from_date", nullable = false)
    LocalDate fromDate;

    @Column(name = "to_date", nullable = false)
    LocalDate toDate;

    @Column(columnDefinition = "TEXT")
    String reason;

    // ------
    @Column(name = "process_instance_id")
    String processInstanceId;

    @Column(name = "total_attendance")
    Integer totalAttendance;

    @Column(name = "on_time_ratio")
    Double onTimeRatio;

    @Column(name = "early_checkout_ratio")
    Double earlyCheckoutRatio;

    // ------
    @Column(name = "approver_id")
    String approverId;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    String adminNote;

    // ------
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    @ToString.Exclude
    Profile profile;

}
