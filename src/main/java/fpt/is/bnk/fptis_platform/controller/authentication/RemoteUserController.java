package fpt.is.bnk.fptis_platform.controller.authentication;

import fpt.is.bnk.fptis_platform.dto.identity.CheckPasswordRequest;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.service.authentication.RemoteUserAppService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Admin 12/1/2025
 *
 **/
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class RemoteUserController {

    private final RemoteUserAppService service;

    @GetMapping("/{username}")
    public ResponseEntity<RemoteUser> getByUsername(@PathVariable String username) {
        System.out.println(username);
        RemoteUser user = service.getByUsername(username);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<RemoteUser> getByEmail(@PathVariable String email) {
        System.out.println(email);
        return ResponseEntity.ok(service.getByEmail(email));
    }

    @PostMapping("/auth/check")
    public ResponseEntity<Boolean> checkPassword(@RequestBody CheckPasswordRequest req) {
        System.out.println(req);
        boolean ok = service.verifyPassword(req.getUsername(), req.getPassword());
        return ResponseEntity.ok(ok);
    }
}

