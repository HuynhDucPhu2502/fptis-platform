package fpt.is.bnk.fptis_platform.controller.test;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.stereotype.Component;

/**
 * Admin 12/18/2025
 *
 **/
@Component
public class UserConfirmTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask task) {
        String initiator =
                (String) task.getVariable("initiator");
        task.setAssignee(initiator);


        String event = task.getEventName();

        if ("create".equals(event)) {
            System.out.println("Create Task");
        }

        if ("assignment".equals(event)) {
            System.out.println("Assignment Task");
        }

        if ("complete".equals(event)) {
            System.out.println("Complete Task");
        }

        if ("delete".equals(event)) {
            System.out.println("Delete Task");

        }
    }
}
