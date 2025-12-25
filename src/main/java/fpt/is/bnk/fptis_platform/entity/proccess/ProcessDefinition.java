package fpt.is.bnk.fptis_platform.entity.proccess;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Admin 12/24/2025
 *
 **/
@Entity
@Table(name = "processes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessDefinition extends BaseEntity {

    // ==========================
    // Identity
    // ==========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String name;

    @Column(name = "process_code", nullable = false, unique = true)
    String processCode;

    @Column(name = "camunda_process_key", nullable = false)
    String camundaProcessKey;

    // ==========================
    // Version Control
    // ==========================

    @Column(name = "latest_deployment_id")
    String latestDeploymentId;

    @Column(name = "active_version")
    Integer activeVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    ProcessStatus status;

    // ==========================
    // Storage
    // ==========================

    @Column(name = "s3_key")
    String latestS3Key;

    // ==========================
    // Relationships
    // ==========================

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("version DESC")
    List<ProcessVersion> versions;

    @OneToMany(mappedBy = "process", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("taskCode ASC")
    List<ProcessTask> tasks;

}
