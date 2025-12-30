package fpt.is.bnk.fptis_platform.repository.process;

import fpt.is.bnk.fptis_platform.entity.proccess.ProcessTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Admin 12/25/2025
 *
 **/
@Repository
public interface ProcessTaskRepository extends JpaRepository<ProcessTask, Long> {

    Optional<ProcessTask> findByProcessProcessCodeAndTaskCode(String processProcessCode, String taskCode);

    @Modifying
    void deleteByProcessId(Long processId);

    Optional<ProcessTask> findByCamundaActivityId(String camundaActivityId);

}
