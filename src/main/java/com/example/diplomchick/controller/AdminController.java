package com.example.diplomchick.controller;


import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.model.UserInfo;
import com.example.diplomchick.repository.UserRepository;
import com.example.diplomchick.repository.SecretKeyRepository;
import com.example.diplomchick.repository.UserInfoRepository;
import com.example.diplomchick.service.EmailService;
import com.example.diplomchick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("check")
public class AdminController {

    private final UserInfoRepository userInfoRepository;
    private final UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    SecretKeyRepository secretKeyRepository;

    @Autowired
    public AdminController(UserInfoRepository userInfoRepository, UserRepository userRepository) {
        this.userInfoRepository = userInfoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        String check = (String) model.getAttribute("check");
        if (!"done".equals(check)) {
            return "YouHaveNoAccess";
        }
        List<UserInfo> users = userInfoRepository.findAll();
        model.addAttribute("users", users);

        return "adminPage";
    }

    @GetMapping("/adminUsers")
    public String adminPageWithUsers(Model model) {
        String check = (String) model.getAttribute("check");
        if (!"done".equals(check)) {
            return "YouHaveNoAccess";
        }
        List<MyUser> users = userRepository.findAll();
        users = users.stream()
                .filter(user -> !user.getEmail().equals("pashnev.mega@gmail.com"))
                .collect(Collectors.toList());
        model.addAttribute("users", users);
        return "adminPageWithUsers";
    }


    @PostMapping("/adminUsers")
    public String adminPageWithUsers(@RequestParam String gmail, @RequestParam String secretKey) {
        if (!secretKey.equals(secretKeyRepository.findByUsername(gmail).getToken())) {
            return "YouHaveNoAccess";
        }
        MyUser user = userRepository.findByEmail(gmail);
        String text = "";

        if (user.isBlocked()) {
            text = """

                    WARNING!!!

                    We have blocked your access temporary

                    Please contact admin to fix it

                    Thank you for your cooperation.""";
        } else {
            text = """

                    Congratulations!!!

                    We have returned your access

                    Thank you for your cooperation.""";
        }
        userService.updateUser(user, user.isBlocked(), "Admin decision");
        emailService.sendEmail(gmail, "check status", text);
        return "redirect:/adminUsers";

    }

    @GetMapping("/logout")
    public String logout(Model model, SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam String gmail, @RequestParam String secretKey) {
        if (!secretKey.equals(secretKeyRepository.findByUsername(gmail).getToken())) {
            return "YouHaveNoAccess";
        }
        userService.deleteUser(gmail);
        return "redirect:/adminUsers";
    }


}
