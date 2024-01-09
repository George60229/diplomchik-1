package com.example.diplomchick.service;

import org.springframework.stereotype.Component;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
public class BlowfishService {

    Map<String, SecretKey> keyStringMap = new HashMap<>();


    public String encrypt(String password) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {

        KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
        SecretKey secretKey = keyGenerator.generateKey();
        System.out.println(secretKey);
        Cipher cipher = Cipher.getInstance("Blowfish");

        byte[] plainTextBytes = password.getBytes(StandardCharsets.UTF_8);


        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainTextBytes);


        String encryptedText = Base64.getEncoder().encodeToString(encryptedBytes);
        System.out.println("Encrypted Text: " + encryptedText);

        keyStringMap.put(encryptedText, secretKey);

        return encryptedText;
    }

    public String decrypt(String encryptedText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("Blowfish");
        SecretKey key = keyStringMap.get(encryptedText);
        if (key == null) {
            throw new InvalidKeyException("The encrypted text is wrong");
        }
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));


        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }


}


