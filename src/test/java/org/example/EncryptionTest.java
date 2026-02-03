package org.example;

import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {
    @Test
    void shouldEncryptAndDecryptCorrectly() {
        Encryption key = new Encryption();

        try {
            SecretKey secretKey = key.setSecretKey();
            byte[] cipherText = key.encryptPassword("Hello26", secretKey);
            String base64 = Base64.getEncoder().encodeToString(cipherText);
            String result = key.decryptPassword(base64, secretKey);

            assertEquals("Hello26", result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}