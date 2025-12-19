package fpt.is.bnk.fptis_platform.controller.test;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/18/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class TestSubProcessController {

    RuntimeService runtimeService;

    @GetMapping("/execute/subprocess/test1")
    public String s() {

        runtimeService.startProcessInstanceByKey("subprocess_diagram_1");

        return "Process started";
    }


}
