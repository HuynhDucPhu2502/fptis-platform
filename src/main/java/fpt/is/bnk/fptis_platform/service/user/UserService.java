package fpt.is.bnk.fptis_platform.service.user;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Admin 12/21/2025
 *
 **/
public interface UserService {
    Page<RemoteUser> getAllUsers(Pageable pageable);
}
