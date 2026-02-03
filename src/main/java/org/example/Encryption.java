package org.example;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Encryption {
    // Данный метод отвечает за создание ключа шифрования
    SecretKey setSecretKey() throws NoSuchAlgorithmException {
        // Указываем каким алгоритмом мы будем пользоваться при создании ключа
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        // Также не забываем указать размер
        keyGenerator.init(256);
        // Генерируем ключ
        SecretKey secretKey = keyGenerator.generateKey();

        return secretKey;
    }
    // Метод отвечает за шифровку пароля
    byte[] encryptPassword(String plainText, SecretKey key) throws Exception {
        // Указываем алгоритм шифрования
        Cipher cipher = Cipher.getInstance("AES");
        // Указываем, что мы хотим именно зашифровать наш пароль, а также ключ
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // Выполняем шифровку
        return cipher.doFinal(plainText.getBytes());
    }
    // Данный метод отвечает за расшифровку пароля
    String decryptPassword(String encryptedData, SecretKey key) throws Exception {
        // Указываем алгоритм шифрования
        Cipher cipher = Cipher.getInstance("AES");
        // Также указываем, что мы хотим расшифровать наш пароль, ну и ключ
        cipher.init(Cipher.DECRYPT_MODE, key);
        // Кладём в данный массив наш расшифрованный пароль
        byte[] decodeBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodeBytes);
        // Преобразуем этот массив в строку
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
