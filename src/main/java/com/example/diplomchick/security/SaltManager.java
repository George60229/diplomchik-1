package com.example.diplomchick.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:application.properties")
public class SaltManager {

    public static String salt;

    @Value("${salt}")
    private void setSalt(String salt) {
        SaltManager.salt = salt;
    }
}