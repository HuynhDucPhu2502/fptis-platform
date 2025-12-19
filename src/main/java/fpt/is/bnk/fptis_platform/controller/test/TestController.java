package fpt.is.bnk.fptis_platform.controller.test;

import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Admin 12/8/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestController {

    RuntimeService runtimeService;
    CurrentUserProvider currentUserProvider;
    TaskService taskService;

    @GetMapping("/execute/test")
    public String execute() {

        runtimeService.startProcessInstanceByKey("first_bpmn_id");

        return "Process started";
    }

    @GetMapping("/execute/test2")
    public String execute2() {

        runtimeService.startProcessInstanceByKey("diagram_3");

        return "Process started";
    }

    @GetMapping("/execute/test3")
    public String execute3() {

        runtimeService.startProcessInstanceByKey("diagram_4");

        return "Process started";
    }

    @GetMapping("/execute/test4")
    public String execute4() {

        runtimeService.startProcessInstanceByKey("diagram_5");

        return "Process started";
    }

    @GetMapping("/execute/test5")
    public String execute5() {

        runtimeService.startProcessInstanceByKey("diagram_6");

        return "Process started";
    }

    @GetMapping("/execute/test6")
    public String execute6() {

        runtimeService.startProcessInstanceByKey("diagram_7");

        return "Process started";
    }

    @GetMapping("/execute/test7")
    public String execute7() {

        runtimeService.startProcessInstanceByKey("diagram_8");

        return "Process started";
    }

    @GetMapping("/execute/test8")
    public String execute8() {

        runtimeService.startProcessInstanceByKey("diagram_9");

        return "Process started";
    }

    @GetMapping("/execute/test9")
    public String execute9() {

        runtimeService.startProcessInstanceByKey("diagram_10");

        return "Process started";
    }

    @GetMapping("/execute/test10")
    public String execute10() {

        runtimeService.startProcessInstanceByKey("diagram_11");

        return "Process started";
    }

    @GetMapping("/execute/test11")
    public String execute11() {

        runtimeService.startProcessInstanceByKey("diagram_12");

        return "Process started";
    }

    @GetMapping("/execute/test12")
    public String execute12() {

        runtimeService.startProcessInstanceByKey("diagram_13");

        return "Process started";
    }

    @GetMapping("/execute/test13")
    public String execute13() {

        runtimeService.startProcessInstanceByKey("diagram_14");

        return "Process started";
    }

    @GetMapping("/execute/test14")
    public String execute14() {

        runtimeService.startProcessInstanceByKey("diagram_15");

        return "Process started";
    }

    @GetMapping("/execute/test15")
    public String execute15() {

        var currentUserId = currentUserProvider.getCurrentUser().getId();

        runtimeService.startProcessInstanceByKey(
                "diagram_16",
                Map.of("initiator", currentUserId.toString())
        );

        return "Process started";
    }

    @GetMapping("/execute/complete")
    public String completeUserTask() {
        var userId = currentUserProvider.getCurrentUser().getId();

        List<Task> tasks = taskService.createTaskQuery()
                .processDefinitionKey("diagram_16")
                .taskAssignee(userId.toString())
                .list();

        if (tasks.isEmpty()) {
            return "No task for user";
        }

        Task taskToComplete = tasks.get(0);
        taskService.complete(taskToComplete.getId());

        return "Task completed. (Remaining tasks for this user: " + (tasks.size() - 1) + ")";
    }

    @GetMapping("/execute/cleanup")
    public String cleanup() {
        var instances = runtimeService.createProcessInstanceQuery()
                .processDefinitionKey("diagram_16")
                .list();
        for (var ins : instances) {
            runtimeService.deleteProcessInstance(ins.getId(), "Clear to fix metadata error");
        }
        return "All old instances deleted. Now you can start a fresh one.";
    }


}
