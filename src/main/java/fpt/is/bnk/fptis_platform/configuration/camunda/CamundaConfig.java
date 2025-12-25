package fpt.is.bnk.fptis_platform.configuration.camunda;

import feign.RequestInterceptor;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.camunda.bpm.client.ExternalTaskClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Base64;

/**
 * Admin 12/23/2025
 *
 **/
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CamundaConfig {

    CamundaBasicAuthProvider authProvider;

    @Bean
    public ExternalTaskClient externalTaskClient() {
        return ExternalTaskClient.create()
                .baseUrl("http://localhost:7070/engine-rest")
                .workerId("fptis-platform-business-worker")
                .addInterceptor(request ->
                        request.addHeader(
                                "Authorization",
                                authProvider.getAuthorizationHeader()
                        )
                )
                .build();
    }

    @Bean
    public RequestInterceptor camundaFeignBasicAuth() {
        return template ->
                template.header(
                        "Authorization",
                        authProvider.getAuthorizationHeader()
                );
    }

}