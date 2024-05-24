package com.example.diplomchick.security;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class SaltManager {

    public static String generateSalt(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be a positive integer");
        }
        SecureRandom secureRandom;
        try {
            secureRandom = SecureRandom.getInstanceStrong();
        } catch (NoSuchAlgorithmException e) {
            secureRandom = new SecureRandom();
        }
        byte[] saltBytes = new byte[length];
        secureRandom.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

}