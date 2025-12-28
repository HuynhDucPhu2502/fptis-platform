package fpt.is.bnk.fptis_platform.repository.process;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessVariable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Admin 12/28/2025
 *
 **/
public interface ProcessVariableRepository extends JpaRepository<ProcessVariable, Long> {

    void deleteByProcessId(Long processId);

}
