package com.example.diplomchick.controller;

import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.model.UserInfo;
import com.example.diplomchick.model.UserRepository;
import com.example.diplomchick.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminController {

    private final UserInfoRepository userInfoRepository;

    private final UserRepository userRepository;


    @Autowired
    public AdminController(UserInfoRepository userInfoRepository, UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        List<UserInfo> users = userInfoRepository.findAll();
        model.addAttribute("users", users);
        return "adminPage";
    }


    @GetMapping("/adminUsers")
    public String adminPageWithUsers(Model model) {
        List<MyUser> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "adminPageWithUsers";
    }


    @PostMapping("/adminUsers")
    public String adminPageWithUsers(@RequestParam String gmail) {
        MyUser user = userRepository.findByEmail(gmail);
        user.setBlocked(!user.isBlocked());
        userRepository.save(user);
        return "redirect:/adminUsers";

    }


}
