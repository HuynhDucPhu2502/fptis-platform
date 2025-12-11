//package fpt.is.bnk.fptis_platform.configuration.security.config;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
/// **
// * Admin 12/2/2025
// *
// **/
//public class KeycloakJwtAuthConverter implements Converter<Jwt, JwtAuthenticationToken> {
//
//    @Override
//    public JwtAuthenticationToken convert(Jwt jwt) {
//
//        Collection<SimpleGrantedAuthority> authorities = extractRealmRoles(jwt);
//
//        String principal = jwt.getClaimAsString("email");
//
//        return new JwtAuthenticationToken(jwt, authorities, principal);
//    }
//
//    private Collection<SimpleGrantedAuthority> extractRealmRoles(Jwt jwt) {
//
//        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
//
//        if (realmAccess == null || realmAccess.isEmpty()) return List.of();
//
//        List<String> roles = (List<String>) realmAccess.get("roles");
//
//        return roles.stream()
//                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
//                .collect(Collectors.toSet());
//    }
//
//}
