package fpt.is.bnk.fptis_platform.controller.test;

import org.springframework.stereotype.Service;

/**
 * Admin 12/17/2025
 *
 **/
@Service
public class TestService {

    public String getDepartment() {
        System.out.println("test service");

        return "my department";
    }

}
