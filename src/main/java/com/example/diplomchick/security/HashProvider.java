package com.example.diplomchick.security;

import com.example.diplomchick.dto.MyPassword;
import org.springframework.stereotype.Service;

@Service
public class HashProvider {


    public MyPassword getHashCode(String password) {
        if (password == null) {
            return null;
        }
        String salt = SaltManager.generateSalt(16);
        password += password + salt;
        password=SHA512.sha512(password);
        MyPassword myPassword = new MyPassword();
        myPassword.setValue(password);
        myPassword.setSalt(salt);
        return myPassword;
    }

    public String getPasswordWithSalt(String password, String salt) {
        if (password == null) {
            return null;
        }
        password += password + salt;
        password=SHA512.sha512(password);
        return password;
    }

}
