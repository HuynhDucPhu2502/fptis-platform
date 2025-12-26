package fpt.is.bnk.fptis_platform.controller.auth;

import fpt.is.bnk.fptis_platform.dto.identity.internal.CheckPasswordRequest;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.service.auth.UserFederationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin 12/1/2025
 *
 **/
@Tag(
        name = "User Federation (Internal)",
        description = "Internal APIs for user federation, including user lookup and credential verification for identity providers."
)
@RestController
@RequestMapping("/api/internal/users")
@RequiredArgsConstructor
public class UserFederationController {

    private final UserFederationService service;

    @GetMapping("/{username}")
    public ResponseEntity<RemoteUser> getByUsername(@PathVariable String username) {
        RemoteUser user = service.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<RemoteUser> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @PostMapping("/auth/check")
    public ResponseEntity<Boolean> checkPassword(@RequestBody CheckPasswordRequest req) {
        boolean ok = service.verifyPassword(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(ok);
    }
}

