package fpt.is.bnk.fptis_platform.service.user.impl;

import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.mapper.UserMapper;
import fpt.is.bnk.fptis_platform.repository.auth.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Admin 12/21/2025
 *
 **/
@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements fpt.is.bnk.fptis_platform.service.user.UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    @Override
    public Page<RemoteUser> getAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return users.map(x -> userMapper.toRemoteUser(x, x.getProfile()));
    }

}
