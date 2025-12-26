package fpt.is.bnk.fptis_platform;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.camunda.community.rest.EnableCamundaRestClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableMethodSecurity
@EnableCamundaRestClient
@OpenAPIDefinition(
        info = @Info(
                title = "FPT IS Place REST API Documentation",
                description = "Internal REST APIs for Place management used by FPT IS applications.",
                version = "v1",
                contact = @Contact(
                        name = "Huỳnh Đức Phú",
                        email = "HuynhDucPhu2502@gmail.com",
                        url = "https://www.linkedin.com/in/huynhducphu2502/"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
public class FptisPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(FptisPlatformApplication.class, args);
    }

}
