package fpt.is.bnk.fptis_platform.entity.proccess;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Admin 12/24/2025
 *
 **/
@Entity
@Table(name = "process_versions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessVersion extends BaseEntity {

    // ==========================
    // Identity
    // ==========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private ProcessDefinition process;

    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    // ==========================
    // Deloyment
    // ==========================
    private Integer version;
    private String deploymentId;
    private LocalDateTime deployedAt;

    // ==========================
    // Storage
    // ==========================
    private String s3Key;

}
