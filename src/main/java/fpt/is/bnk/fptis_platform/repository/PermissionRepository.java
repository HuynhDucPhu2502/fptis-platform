package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.authorization.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Admin 12/12/2025
 *
 **/
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findPermissionByRoles_Name(String rolesName);

}
