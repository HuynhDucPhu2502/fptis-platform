package fpt.is.bnk.fptis_platform.service.auth.impl;

import feign.FeignException;
import fpt.is.bnk.fptis_platform.advice.base.ErrorCode;
import fpt.is.bnk.fptis_platform.advice.exception.AppException;
import fpt.is.bnk.fptis_platform.dto.identity.RemoteUser;
import fpt.is.bnk.fptis_platform.dto.identity.TokenExchangeResponse;
import fpt.is.bnk.fptis_platform.dto.identity.internal.UserCreationParam;
import fpt.is.bnk.fptis_platform.dto.request.authentication.LoginRequest;
import fpt.is.bnk.fptis_platform.dto.request.authentication.RegistrationRequest;
import fpt.is.bnk.fptis_platform.entity.user.Profile;
import fpt.is.bnk.fptis_platform.entity.user.User;
import fpt.is.bnk.fptis_platform.advice.base.ErrorNormalizer;
import fpt.is.bnk.fptis_platform.mapper.UserMapper;
import fpt.is.bnk.fptis_platform.repository.auth.IdentityClient;
import fpt.is.bnk.fptis_platform.repository.auth.ProfileRepository;
import fpt.is.bnk.fptis_platform.repository.auth.UserRepository;
import fpt.is.bnk.fptis_platform.service.auth.JwtService;
import fpt.is.bnk.fptis_platform.service.auth.RsaService;
import fpt.is.bnk.fptis_platform.service.common.CurrentUserProvider;
import fpt.is.bnk.fptis_platform.service.auth.AuthService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@Transactional
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthServiceImpl implements AuthService {

    // Service
    CurrentUserProvider currentUserProvider;
    JwtService jwtService;
    RsaService rsaService;

    IdentityClient identityClient;

    // Repository
    ProfileRepository profileRepository;
    UserRepository userRepository;

    // Mapper
    UserMapper userMapper;

    // Caching
    RedisTemplate<String, String> redis;

    // Utils
    PasswordEncoder passwordEncoder;
    ErrorNormalizer errorNormalizer;


    public AuthServiceImpl(
            CurrentUserProvider currentUserProvider, ProfileRepository profileRepository,
            UserRepository userRepository, JwtService jwtService,
            UserMapper userMapper, IdentityClient identityClient,
            @Qualifier("redisStringTemplate") RedisTemplate<String, String> redis,
            PasswordEncoder passwordEncoder,
            ErrorNormalizer errorNormalizer,
            RsaService rsaService
    ) {
        this.currentUserProvider = currentUserProvider;
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.identityClient = identityClient;
        this.redis = redis;
        this.passwordEncoder = passwordEncoder;
        this.errorNormalizer = errorNormalizer;
        this.rsaService = rsaService;
    }

    @Value("${app.jwt.access-token-expiration}")
    @NonFinal
    Long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-expiration}")
    @NonFinal
    Long refreshTokenExpiration;

    @Value("${idp.client-id}")
    @NonFinal
    String clientId;

    @Value("${idp.client_secret}")
    @NonFinal
    String clientSecret;

    @Value("${remote-federation.link}")
    @NonFinal
    String federationLink;

    static Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    // =====================================================================
    // USER INFORMATION
    // =====================================================================
    @Override
    public RemoteUser getCurrentUserProfile() {
        var user = currentUserProvider.getCurrentUser();

        var res = userMapper.toRemoteUser(user, user.getProfile());

        List<String> roles = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .map(Object::toString)
                .toList();

        res.setRoles(roles);

        return res;
    }


    // =====================================================================
    // LOGIN
    // =====================================================================
    @Override
    public Map<String, ? extends Serializable> login(LoginRequest request) {
        try {
            String password;


            if (request.getIsCrypted() != null && request.getIsCrypted())
                password = rsaService.decrypt(request.getPassword());
            else
                password = request.getPassword();


            exchangeToken(Map.of(
                    "grant_type", "password",
                    "client_id", clientId,
                    "client_secret", clientSecret,
                    "username", request.getUsername(),
                    "password", password,
                    "scope", "openid"
            ));


            String username = request.getUsername();
            boolean isEmail = EMAIL_PATTERN.matcher(username).matches();

            var user = isEmail
                    ? userRepository.findByEmailWithProfile(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USERNAME_IS_MISSING))
                    : userRepository.findByUsernameWithProfile(username)
                    .orElseThrow(() -> new AppException(ErrorCode.USERNAME_IS_MISSING));

            return generateToken(user);

        } catch (FeignException e) {
            if (e.status() == 401) {
                throw new AppException(ErrorCode.INVALID_CREDENTIALS);
            }
            throw errorNormalizer.handleKeyCloakException(e);
        }
    }


    // =====================================================================
    // LOGIN
    // =====================================================================
    @Override
    public void logout(String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            return;
        }

        redis.opsForValue().set(
                "ACCESS_BLACKLIST:" + accessToken,
                "1",
                Duration.ofSeconds(accessTokenExpiration)
        );
    }

    // =====================================================================
    // REFRESH TOKEN
    // =====================================================================
    @Override
    public Map<String, ? extends Serializable> refresh(String refreshToken) {

        var jwt = jwtService.decodeJwt(refreshToken);
        String email = jwt.getSubject();

        var user = userRepository.findByEmailWithProfile(email)
                .orElseThrow(() -> new AppException(ErrorCode.USERNAME_IS_MISSING));

        String sessionKey = "USER_SESSION:" + user.getId();
        String storedRefresh = (String) redis.opsForHash().get(sessionKey, "refreshToken");

        if (storedRefresh == null || !storedRefresh.equals(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        return generateToken(user);
    }


    // =====================================================================
    // REGISTER
    // =====================================================================
    @Override
    public RemoteUser register(RegistrationRequest request) {

        String normalizedUsername = request.getUsername().toLowerCase();
        String combined = normalizedUsername + ":" + request.getPassword();

        User user = new User();
        user.setUsername(normalizedUsername);
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(combined));
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setDob(request.getDob());
        profile.setUser(user);
        profileRepository.save(profile);

        var token = exchangeToken(Map.of(
                "grant_type", "client_credentials",
                "client_id", clientId,
                "client_secret", clientSecret
        ));

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


    // =====================================================================
    // INTERNAL UTILS
    // =====================================================================
    private TokenExchangeResponse exchangeToken(Map<String, String> form) {
        return identityClient.exchangeToken(form);
    }


    // =====================================================================
    // TOKEN GENERATION
    // =====================================================================
    private Map<String, ? extends Serializable> generateToken(User user) {

        String newAccess = jwtService.buildJwt(user, accessTokenExpiration);
        String newRefresh = jwtService.buildJwt(user, refreshTokenExpiration);

        String sessionKey = "USER_SESSION:" + user.getId();

        String oldAccess = (String) redis.opsForHash().get(sessionKey, "accessToken");
        if (oldAccess != null) {
            redis.opsForValue().set(
                    "ACCESS_BLACKLIST:" + oldAccess,
                    "1",
                    Duration.ofSeconds(accessTokenExpiration)
            );
        }

        redis.opsForHash().put(sessionKey, "refreshToken", newRefresh);
        redis.opsForHash().put(sessionKey, "accessToken", newAccess);

        redis.expire(sessionKey, Duration.ofSeconds(refreshTokenExpiration));

        return Map.of(
                "accessToken", newAccess,
                "refreshToken", newRefresh,
                "refreshExpiresIn", refreshTokenExpiration
        );
    }

}
