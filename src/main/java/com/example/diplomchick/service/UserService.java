package com.example.diplomchick.service;

import com.example.diplomchick.dto.MyPassword;
import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.model.SecretKey;
import com.example.diplomchick.repository.UserRepository;
import com.example.diplomchick.repository.SecretKeyRepository;
import com.example.diplomchick.repository.UserInfoRepository;
import com.example.diplomchick.security.HashProvider;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    SecretKeyRepository secretKeyRepository;

    @Autowired
    HashProvider hashProvider;

    @Autowired
    EmailService emailService;


    public boolean isExist(String email) {
        MyUser user = getByEmail(email);
        return user != null;
    }


    public MyUser getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void createUser(MyUser myUser) {
        MyPassword myPassword;
        do {
            myPassword = hashProvider.getHashCode(myUser.getPassword());
        } while (userRepository.findByPassword(myPassword.getValue()).size() != 0);
        myUser.setPassword(myPassword.getValue());
        myUser.setSalt(myPassword.getSalt());
        myUser.setReason("Waiting of pending");
        userRepository.save(myUser);
        SecretKey secretKey = new SecretKey();
        secretKey.setUsername(myUser.getEmail());
        secretKey.setToken(generateToken());
        secretKeyRepository.save(secretKey);
    }

    public boolean checkCredential(String email, String password) {
        MyUser user = getByEmail(email);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public boolean isAdmin(String email) {
        return email.equals("pashnev.mega@gmail.com");
    }

    public boolean checkBlock(String email) {
        MyUser user = getByEmail(email);
        return user.isBlocked();
    }

    public boolean isNewLocation(String country, String email) {
        return userInfoRepository.findByCountryAndEmail(country, email).size() == 1;

    }

    public void updateUser(MyUser user, boolean value, String reason) {

        if (value) {
            user.setReason("Unblocked");
            user.setBlocked(false);
        } else {
            user.setBlocked(!user.isBlocked());
            user.setReason(reason);
        }

        userRepository.save(user);
    }


    public static String generateToken() {
        SecureRandom secureRandom = new SecureRandom();
        int TOKEN_LENGTH = 32;
        byte[] randomBytes = new byte[TOKEN_LENGTH / 2];
        secureRandom.nextBytes(randomBytes);
        return new BigInteger(1, randomBytes).toString(16);
    }

    @Transactional
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
        secretKeyRepository.deleteByUsername(email);
    }

    public void changePassword(String email, String newPassword) {
        MyUser myUser = getByEmail(email);
        MyPassword myPassword = hashProvider.getHashCode(newPassword);
        myUser.setPassword(myPassword.getValue());
        myUser.setSalt(myPassword.getSalt());
        userRepository.save(myUser);
    }

}
