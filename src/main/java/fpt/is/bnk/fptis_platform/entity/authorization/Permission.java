package fpt.is.bnk.fptis_platform.entity.authorization;

import fpt.is.bnk.fptis_platform.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

/**
 * Admin 12/11/2025
 *
 **/
@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Permission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(unique = true, nullable = false, length = 100)
    String name;

    String description;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    List<RolePermission> rolePermissions;

    @OneToMany(mappedBy = "permission", fetch = FetchType.LAZY)
    List<ApiPermission> apiPermissions;
}

