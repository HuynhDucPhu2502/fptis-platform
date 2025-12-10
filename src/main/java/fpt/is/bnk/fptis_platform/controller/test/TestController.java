package fpt.is.bnk.fptis_platform.controller.test;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/8/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestController {

    RuntimeService runtimeService;

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


}
