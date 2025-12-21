package fpt.is.bnk.fptis_platform.service.auth.impl;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * Admin 12/21/2025
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RsaServiceImpl implements fpt.is.bnk.fptis_platform.service.auth.RsaService {

    @Value("${app.rsa.public-key}")
    @NonFinal
    String publicKeyBase64;

    @Value("${app.rsa.private-key}")
    @NonFinal
    String privateKeyBase64;

    @NonFinal
    PrivateKey privateKey;

    @PostConstruct
    void init() {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            privateKey = kf.generatePrivate(
                    new PKCS8EncodedKeySpec(
                            Base64.getDecoder().decode(privateKeyBase64)
                    )
            );
        } catch (Exception e) {
            throw new IllegalStateException("Init RSA private key failed", e);
        }
    }

    @Override
    public String getPublicKey() {
        return publicKeyBase64;
    }

    @Override
    public String decrypt(String encryptedBase64) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT
            );

            cipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);

            byte[] decrypted = cipher.doFinal(
                    Base64.getDecoder().decode(encryptedBase64)
            );
            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            log.error("RSA decryption failed: {}", e.getMessage());
            throw new IllegalArgumentException("RSA decrypt failed", e);
        }
    }
}
