package fpt.is.bnk.fptis_platform.service.auth.impl;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.entity.User;
import fpt.is.bnk.fptis_platform.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 12/1/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RemoteUserAppServiceImpl implements fpt.is.bnk.fptis_platform.service.auth.RemoteUserAppService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Override
    public RemoteUser getByUsername(String username) {
        return userRepository
                .findByUsernameWithProfile(username)
                .map(this::mapToRemoteUser)
                .orElse(null);
    }

    @Override
    public RemoteUser getByEmail(String email) {
        return userRepository
                .findByEmailWithProfile(email)
                .map(this::mapToRemoteUser)
                .orElse(null);
    }

    @Override
    public boolean verifyPassword(String username, String rawPassword) {
        var user = userRepository.findByUsernameWithProfile(username).orElse(null);
        if (user == null) {
            System.out.println("null");
            return false;
        }

        return passwordEncoder.matches(rawPassword, user.getPasswordHash());
    }

    private RemoteUser mapToRemoteUser(User user) {
        RemoteUser dto = new RemoteUser();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        if (user.getProfile() != null) {
            dto.setFirstName(user.getProfile().getFirstName());
            dto.setLastName(user.getProfile().getLastName());
            dto.setDob(user.getProfile().getDob().toString());
        }

        dto.setActive(true);

        return dto;
    }

}
