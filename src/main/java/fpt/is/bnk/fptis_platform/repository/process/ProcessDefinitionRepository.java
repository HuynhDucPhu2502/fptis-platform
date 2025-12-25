package fpt.is.bnk.fptis_platform.repository.process;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Admin 12/24/2025
 *
 **/
@Repository
public interface ProcessDefinitionRepository extends JpaRepository<ProcessDefinition, Long> {
    Optional<ProcessDefinition> findByProcessCode(String processCode);
}
