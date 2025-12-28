package fpt.is.bnk.fptis_platform.repository.process;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessVersion;
import fpt.is.bnk.fptis_platform.entity.proccess.constant.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Admin 12/24/2025
 *
 **/
@Repository
public interface ProcessVersionRepository extends JpaRepository<ProcessVersion, Long> {

    List<ProcessVersion> findByProcessIdAndResourceTypeOrderByVersionDesc(Long processId, ResourceType resourceType);
}
