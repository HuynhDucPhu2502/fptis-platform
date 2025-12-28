package fpt.is.bnk.fptis_platform.entity.proccess;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Admin 12/28/2025
 *
 **/
@Entity
@Table(name = "process_variables")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProcessVariable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    ProcessDefinition process;

    @Column(name = "variable_name", nullable = false)
    String variableName;

    @Column(name = "display_name")
    String displayName;

    @Column(name = "default_value")
    String defaultValue;

    @Column(name = "data_type")
    String dataType;

}
