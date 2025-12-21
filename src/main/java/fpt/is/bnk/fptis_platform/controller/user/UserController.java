package fpt.is.bnk.fptis_platform.controller.user;

import fpt.is.bnk.fptis_platform.dto.ApiResponse;
import fpt.is.bnk.fptis_platform.dto.PageResponse;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.service.user.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Admin 12/21/2025
 *
 **/
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/users")
public class UserController {

    UserService userService;

    @PreAuthorize("hasAuthority('USERS_VIEW')")
    @GetMapping
    public ApiResponse<PageResponse<RemoteUser>> getAllUser(@PageableDefault Pageable pageable) {
        PageResponse<RemoteUser> res = new PageResponse<>(userService.getAllUsers(pageable));

        return ApiResponse.<PageResponse<RemoteUser>>builder()
                .result(res)
                .build();
    }


}
