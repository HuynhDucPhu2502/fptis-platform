package fpt.is.bnk.fptis_platform.controller.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

/**
 * Admin 12/10/2025
 **/
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TestDelegate implements JavaDelegate {

    ObjectMapper objectMapper;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        var username = delegateExecution.getVariable("userName");
        var userObj2Json = delegateExecution.getVariable("userObj2");
        var userObj3Json = delegateExecution.getVariable("userObj3");

        String user2String = objectMapper.writeValueAsString(userObj2Json);
        String user3String = objectMapper.writeValueAsString(userObj3Json);


        System.out.println("==========================================");
        System.out.println("userName: " + username);
        System.out.println("userObj2: " + user2String);
        System.out.println("userObj3: " + user3String);
        System.out.println("==========================================");

        String test = "HDP was here";

        delegateExecution.setVariable("test", test);

    }
}
