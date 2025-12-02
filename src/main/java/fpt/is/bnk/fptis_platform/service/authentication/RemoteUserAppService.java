package fpt.is.bnk.fptis_platform.service.authentication;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;

/**
 * Admin 12/1/2025
 *
 **/
public interface RemoteUserAppService {
    RemoteUser getByUsername(String username);

    RemoteUser getByEmail(String email);

    boolean verifyPassword(String username, String rawPassword);
}
