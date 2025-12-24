package fpt.is.bnk.fptis_platform.service.common.impl;

import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.repository.auth.UserRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 11/26/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrentUserProviderImpl implements CurrentUserProvider {

    UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("sub");

        var user = userRepository
                .findUserByEmailIgnoreCase(email);

        if (user.isPresent())
            return user.get();

        throw new EntityNotFoundException("Không tìm thấy người dùng");
    }


}
