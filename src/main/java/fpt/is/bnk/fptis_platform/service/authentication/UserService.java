package fpt.is.bnk.fptis_platform.service.authentication;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.dto.request.authentication.LoginRequest;
import fpt.is.bnk.fptis_platform.dto.request.authentication.RegistrationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Admin 11/25/2025
 *
 **/
public interface UserService {
    RemoteUser getCurrentUserProfile();

    Page<RemoteUser> getAllUsers(Pageable pageable);

    Map<String, ? extends Serializable> login(LoginRequest request);

    Map<String, ? extends Serializable> refresh(String refreshToken);

    RemoteUser register(RegistrationRequest request);
}
