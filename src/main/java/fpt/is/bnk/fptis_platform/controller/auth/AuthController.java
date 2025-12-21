package fpt.is.bnk.fptis_platform.controller.auth;

import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.PageResponse;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.dto.request.authentication.LoginRequest;
import fpt.is.bnk.fptis_platform.dto.request.authentication.RegistrationRequest;
import fpt.is.bnk.fptis_platform.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Map;

/**
 * Admin 11/25/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/auth")
public class AuthController {

    AuthService authService;

    @NonFinal
    @Value("${cookie.sameSite}")
    String sameSite;

    @NonFinal
    @Value("${cookie.secure}")
    boolean secure;

    @GetMapping("/me")
    public ApiResponse<RemoteUser> getCurrentUserProfile() {
        return ApiResponse.<RemoteUser>builder()
                .result(authService.getCurrentUserProfile())
                .build();
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(
            @RequestBody @Valid LoginRequest request
    ) {
        Map<String, ? extends Serializable> res = authService.login(request);

        String accessToken = (String) res.get("accessToken");
        String refreshToken = (String) res.get("refreshToken");

        var refreshTokenCookie = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .path("/")
                .sameSite(sameSite)
                .secure(secure)
                .maxAge((Long) res.get("refreshExpiresIn"))
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new ApiResponse<>(1000, null, accessToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<String>> refresh(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) {

        if (refreshToken == null || refreshToken.isBlank())
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        var result = authService.refresh(refreshToken);

        var cookie = ResponseCookie.from("refresh_token", (String) result.get("refreshToken"))
                .httpOnly(true)
                .path("/")
                .sameSite(sameSite)
                .secure(secure)
                .maxAge((Long) result.get("refreshExpiresIn"))
                .build();

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse<>(1000, null, (String) result.get("accessToken")));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(name = "Authorization", required = false) String authHeader
    ) {
        String token = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        authService.logout(token);

        var refreshTokenCookie = ResponseCookie
                .from("refresh_token", "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .build();
    }


    @PostMapping("/register")
    public ApiResponse<RemoteUser> register(@RequestBody @Valid RegistrationRequest request) {
        return ApiResponse.<RemoteUser>builder()
                .result(authService.register(request))
                .build();
    }


}
