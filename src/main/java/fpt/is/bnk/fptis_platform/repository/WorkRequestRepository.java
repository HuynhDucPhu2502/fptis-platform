package fpt.is.bnk.fptis_platform.repository;

import fpt.is.bnk.fptis_platform.entity.work_request.WorkRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Admin 12/19/2025
 *
 **/
@Repository
public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> {
}
