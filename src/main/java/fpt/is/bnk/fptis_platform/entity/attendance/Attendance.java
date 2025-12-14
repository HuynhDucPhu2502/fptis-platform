package fpt.is.bnk.fptis_platform.entity.attendance;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import fpt.is.bnk.fptis_platform.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Admin 12/12/2025
 *
 **/
@Entity
@Table(name = "attendances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Attendance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime timeIn;

    @Column
    private LocalTime timeOut;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus checkInStatus;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus checkOutStatus;

    public void checkIn(LocalTime scheduledTimeIn) {
        if (timeIn == null) {
            this.timeIn = LocalTime.now();

            LocalTime latestCheckIn = scheduledTimeIn.plusMinutes(15);

            if (this.timeIn.isBefore(scheduledTimeIn.minusMinutes(15))) {
                throw new IllegalArgumentException("Không thể check-in sớm hơn quy định.");
            }

            if (this.timeIn.isBefore(latestCheckIn) || this.timeIn.equals(latestCheckIn)) {
                this.checkInStatus = AttendanceStatus.CHECKED_IN_ON_TIME;
            } else {
                this.checkInStatus = AttendanceStatus.CHECKED_IN_LATE;
            }
        }
    }

    public void checkOut(LocalTime scheduledTimeOut) {
        if (timeOut == null && timeIn != null) {
            this.timeOut = LocalTime.now();

            if (this.timeOut.isBefore(scheduledTimeOut.minusMinutes(15))) {
                this.checkOutStatus = AttendanceStatus.CHECKED_OUT_EARLY;
            } else {
                this.checkOutStatus = AttendanceStatus.CHECKED_OUT_ON_TIME;
            }
        }
    }
}
