package fpt.is.bnk.fptis_platform.entity.proccess;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/25/2025
 *
 **/
@Entity
@Table(name = "process_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessTask extends BaseEntity {

    // ==========================
    // Identity & Mapping
    // ==========================

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    ProcessDefinition process;

    @Column(name = "task_code", nullable = false)
    String taskCode;

    @Column(name = "task_name")
    String taskName;

    @Column(name = "camunda_activity_id")
    String camundaActivityId;

    // ==========================
    // Business Configuration
    // ==========================

    @Column(name = "permission_role")
    String permissionRole;

    @Column(name = "form_key")
    String formKey;

    // ==========================
    // Extended Metadata (JSON)
    // ==========================

    @Column(name = "action_buttons", columnDefinition = "TEXT")
    String actionButtons;

    @Column(name = "task_parameters", columnDefinition = "TEXT")
    String taskParameters;

}