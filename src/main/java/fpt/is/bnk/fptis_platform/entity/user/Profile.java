package fpt.is.bnk.fptis_platform.entity.user;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import fpt.is.bnk.fptis_platform.entity.attendance.Attendance;
import fpt.is.bnk.fptis_platform.entity.daily_log.DailyLog;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

/**
 * Admin 11/25/2025
 *
 **/
@Entity
@Table(
        name = "profiles",
        indexes = {
                @Index(name = "idx_profile_user_id", columnList = "user_id")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_profile_user_id", columnNames = "user_id")
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Profile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long profileId;

    String firstName;
    String lastName;
    LocalDate dob;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    User user;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<DailyLog> dailyLogs;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<Attendance> attendances;

}
