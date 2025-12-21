package fpt.is.bnk.fptis_platform.service.auth;

/**
 * Admin 12/21/2025
 *
 **/
public interface RsaService {
    String getPublicKey();

    String decrypt(String encryptedBase64);
}
