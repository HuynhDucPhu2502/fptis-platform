package fpt.is.bnk.fptis_platform.configuration.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * Admin 11/28/2025
 *
 **/
@Configuration
public class AuditConfiguration {

    @Bean
    public AuditorAware<String> auditorProvider() {

        return () -> Optional.ofNullable(
                SecurityContextHolder.getContext().getAuthentication()
        ).map(Authentication::getName);

    }

}
