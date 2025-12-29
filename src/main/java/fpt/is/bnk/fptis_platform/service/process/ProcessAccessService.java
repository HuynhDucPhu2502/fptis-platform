package fpt.is.bnk.fptis_platform.service.process;

/**
 * Admin 12/29/2025
 *
 **/
public interface ProcessAccessService {
    boolean canExecuteTask(String processCode, String taskCode);
}
