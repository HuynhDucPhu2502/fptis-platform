package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.authorization.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Admin 12/12/2025
 *
 **/
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
