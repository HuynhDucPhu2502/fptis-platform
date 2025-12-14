package fpt.is.bnk.fptis_platform.entity.daily_log;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import fpt.is.bnk.fptis_platform.entity.user.Profile;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Admin 11/28/2025
 *
 **/
@Entity
@Table(name = "daily_logs")
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DailyLog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(columnDefinition = "TEXT")
    String mainTask;
    @Column(columnDefinition = "TEXT")
    String result;

    @Column(nullable = false)
    LocalDate workDate;
    LocalTime startTime;
    LocalTime endTime;

    String location;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profile_id", nullable = false)
    Profile profile;
}
