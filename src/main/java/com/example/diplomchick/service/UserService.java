package com.example.diplomchick.service;

import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.model.UserRepository;
import com.example.diplomchick.security.SaltManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {

    @Autowired
    SaltManager saltManager;

    @Autowired
    UserRepository userRepository;

    String salt = BCrypt.gensalt();


    public boolean isExist(String email) {
        MyUser user = getByEmail(email);
        return user != null;
    }


    public MyUser getByEmail(String email) {
        return userRepository.findByEmail(email);
    }


    public void createUser(MyUser myUser) {
        myUser.setPassword(getHashCode(myUser.getPassword()));
        userRepository.save(myUser);
    }

    public boolean checkCredential(String email, String password) {
        MyUser user = getByEmail(email);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }


    public boolean checkBlock(String email) {
        MyUser user = getByEmail(email);
        return user.isBlocked();

    }

    public String getHashCode(String password) {
        return BCrypt.hashpw(password, Objects.requireNonNull(SaltManager.salt));
    }
}
