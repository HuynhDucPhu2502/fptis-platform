package fpt.is.bnk.fptis_platform.service.auth.impl;

import feign.FeignException;
import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.dto.identity.TokenExchangeResponse;
import fpt.is.bnk.fptis_platform.dto.identity.internal.UserCreationParam;
import fpt.is.bnk.fptis_platform.dto.request.authentication.LoginRequest;
import fpt.is.bnk.fptis_platform.dto.request.authentication.RegistrationRequest;
import fpt.is.bnk.fptis_platform.entity.Profile;
import fpt.is.bnk.fptis_platform.entity.User;
import fpt.is.bnk.fptis_platform.advice.base.ErrorNormalizer;
import fpt.is.bnk.fptis_platform.mapper.UserMapper;
import fpt.is.bnk.fptis_platform.repository.IdentityClient;
import fpt.is.bnk.fptis_platform.repository.ProfileRepository;
import fpt.is.bnk.fptis_platform.repository.UserRepository;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.auth.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Map;

/**
 * Admin 11/25/2025
 *
 **/
@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserServiceImpl implements UserService {

    // Provider
    CurrentUserProvider currentUserProvider;

    // Repository
    ProfileRepository profileRepository;
    UserRepository userRepository;

    // Mapper
    UserMapper userMapper;

    // Keycloak
    IdentityClient identityClient;
    ErrorNormalizer errorNormalizer;

    // Util
    PasswordEncoder passwordEncoder;


    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client_secret}")
    @NonFinal
    String clientSecert;

    @Value("${remote-federation.link}")
    @NonFinal
    String federationLink;

    @Override
    public RemoteUser getCurrentUserProfile() {
        var user = currentUserProvider.getCurrentUser();

        return userMapper.toRemoteUser(user, user.getProfile());
    }

    @Override
    public Page<RemoteUser> getAllUsers(Pageable pageable) {
        var users = userRepository.findAll(pageable);
        return users.map(x -> userMapper.toRemoteUser(x, x.getProfile()));
    }

    @Override
    public Map<String, ? extends Serializable> login(LoginRequest request) {
        try {
            var token = exchangeToken(Map.of(
                    "grant_type", "password",
                    "client_id", clientId,
                    "client_secret", clientSecert,
                    "username", request.getUsername(),
                    "password", request.getPassword(),
                    "scope", "openid"
            ));

            return Map.of(
                    "accessToken", token.getAccessToken(),
                    "refreshToken", token.getRefreshToken(),
                    "refreshExpiresIn", token.getRefreshExpiresIn()
            );
        } catch (FeignException e) {
            if (e.status() == 401) {
                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
            }
            
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }

    @Override
    public Map<String, ? extends Serializable> refresh(String refreshToken) {

        var token = exchangeToken(Map.of(
                "grant_type", "refresh_token",
                "client_id", clientId,
                "client_secret", clientSecert,
                "refresh_token", refreshToken,
                "scope", "openid"
        ));

        return Map.of(
                "accessToken", token.getAccessToken(),
                "refreshToken", token.getRefreshToken(),
                "refreshExpiresIn", token.getRefreshExpiresIn()
        );
    }


    @Override
    public RemoteUser register(RegistrationRequest request) {

        String normalizedUsername = request.getUsername().toLowerCase();
        String combined = normalizedUsername + ":" + request.getPassword();

        // Tạo User
        User user = new User();
        user.setUsername(normalizedUsername);
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(combined));
        user = userRepository.save(user);

        // Tạo Profile
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDob(request.getDob());
        profile.setUser(user);
        profileRepository.save(profile);

        // Lấy Token service account
        var token = exchangeToken(Map.of(
                "grant_type", "client_credentials",
                "client_id", clientId,
                "client_secret", clientSecert
        ));

        // Tạo Metadata trong Keycloak
        try {
            identityClient.createUser(
                    "Bearer " + token.getAccessToken(),
                    UserCreationParam.builder()
                            .username(normalizedUsername)
                            .email(request.getEmail())
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .enabled(true)
                            .emailVerified(true)
                            .federationLink(federationLink)
                            .build()
            );
        } catch (FeignException e) {
            throw errorNormalizer.handleKeyCloakException(e);
        }

        return userMapper.toRemoteUser(user, profile);
    }


    // ====================================================================================
    // Utility Functions
    // ====================================================================================
    private TokenExchangeResponse exchangeToken(Map<String, String> form) {
        return identityClient.exchangeToken(form);
    }


}
