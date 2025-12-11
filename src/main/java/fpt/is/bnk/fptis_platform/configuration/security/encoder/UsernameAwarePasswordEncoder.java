package fpt.is.bnk.fptis_platform.configuration.security.encoder;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Admin 12/10/2025
 *
 **/
public class UsernameAwarePasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return new BCryptPasswordEncoder().encode(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawInput, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawInput.toString(), encodedPassword);
    }
}
