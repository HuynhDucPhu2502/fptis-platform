package fpt.is.bnk.fptis_platform.configuration.security.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import fpt.is.bnk.fptis_platform.configuration.security.encoder.UsernameAwarePasswordEncoder;
import fpt.is.bnk.fptis_platform.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.List;
import java.util.stream.Stream;

/**
 * Admin 12/11/2025
 *
 **/
@Configuration
@RequiredArgsConstructor
public class AuthConfiguration {

    @Value("${app.jwt.secret}")
    private String jwtKey;

    public static final MacAlgorithm MAC_ALGORITHM = MacAlgorithm.HS512;

    private final PermissionRepository permissionRepository;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder
                .withSecretKey(getSecretKey())
                .macAlgorithm(MAC_ALGORITHM)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new UsernameAwarePasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            List<String> roles = jwt.getClaimAsStringList("roles");
            if (roles.isEmpty()) return List.of();

            List<GrantedAuthority> roleAuthorities = roles
                    .stream()
                    .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                    .map(a -> (GrantedAuthority) a)
                    .toList();

            List<GrantedAuthority> permissionAuthorities = roles
                    .stream()
                    .flatMap(r -> permissionRepository
                            .findPermissionByRoles_Name(r)
                            .stream()
                            .map(p -> new SimpleGrantedAuthority(p.getName()))
                    )
                    .map(a -> (GrantedAuthority) a)
                    .toList();


            return Stream
                    .concat(roleAuthorities.stream(), permissionAuthorities.stream())
                    .toList();
        });

        return converter;
    }


    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();

        return new SecretKeySpec(keyBytes, MAC_ALGORITHM.getName());
    }

}
