package fpt.is.bnk.fptis_platform.configuration.camunda;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Admin 12/24/2025
 *
 **/
@Component
public class CamundaBasicAuthProvider {

    private final String basicAuthHeader;

    public CamundaBasicAuthProvider(
            @Value("${camunda.bpm.client.basic-auth.username}") String username,
            @Value("${camunda.bpm.client.basic-auth.password}") String password
    ) {
        this.basicAuthHeader = "Basic " + Base64.getEncoder()
                .encodeToString(
                        (username + ":" + password)
                                .getBytes(StandardCharsets.UTF_8)
                );
    }

    public String getAuthorizationHeader() {
        return basicAuthHeader;
    }
}
