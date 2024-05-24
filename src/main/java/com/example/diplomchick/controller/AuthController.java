package com.example.diplomchick.controller;

import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.service.EmailService;
import com.example.diplomchick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    EmailService emailService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") MyUser user) {
        if (userService.isExist(user.getEmail())) {
            return "user_exist_error";
        }
        user.setBlocked(true);
        userService.createUser(user);
        String text =
                "Thank you for registering with us!\n" +
                        "\n" +
                        "Your account has been successfully created. However, please note that your registration is currently pending approval by our team.\n" +
                        "\n" +
                        "Please be patient as we review your application. We aim to process all requests promptly, but it may take some time depending on the volume of applications we receive.\n" +
                        "\n" +
                        "Once your registration has been approved, you will receive a confirmation email with further instructions on how to access your account.\n" +
                        "\n" +
                        "Thank you for choosing our platform. If you have any questions or concerns, feel free to contact us.\n" +
                        "\n" +
                        "Best regards, " + user.getUsername();
        emailService.sendEmail(user.getEmail(), "Welcome to the system", text);
        return "login";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam String email, @RequestParam String newPassword) {
        userService.changePassword(email, newPassword);
        return "login";
    }

}
