package com.example.diplomchick.controller;


import com.example.diplomchick.model.UserInfo;
import com.example.diplomchick.model.UserRepository;
import com.example.diplomchick.service.EmailService;
import com.example.diplomchick.service.GPSService;
import com.example.diplomchick.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class EmailController {
    private final Map<String, Integer> savedValue = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private GPSService gpsService;

    @Autowired
    private UserService userService;


    @PostMapping("/checkAccess/{email}/{value}")
    public String checkValue(@PathVariable String email, @PathVariable int value) {
        if (savedValue.get(email) != null) {
            boolean result = savedValue.get(email) == value;
            savedValue.remove(email, value);
            if (result) {
                return "success";
            }
        }
        return "error";
    }

    @PostMapping("/sendEmail/{email}")
    public String sendEmail(@PathVariable String email, Model model, @RequestParam String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ip = "";
        if (authentication != null && authentication.getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            ip = details.getRemoteAddress();
        }
        password = userService.getHashCode(password);
        if (userService.isAdmin(email, password)) {
            return "redirect:/admin";
        }
        if (!userService.checkCredential(email, password)) {
            return "wrong_credential_error";
        }
        if (userService.checkBlock(email)) {
            return "UserBlockError";
        }
        Random random = new Random();
        int value = random.nextInt(100000, 999999);
        String text = "\n" +
                "Welcome to Our Website!\n" +
                "\n" +
                "For your security, we kindly ask you to confirm the number or code you received.\n" +
                "\n" +
                "Please enter the number that was sent to you on the provided contact address or phone.\n" +
                "\n" +
                "Your number:" + value + "\n" +
                "\n" +
                "Thank you for your cooperation.";
        savedValue.put(email, value);
        emailService.sendEmail(email, "Confirm your login", text);
        model.addAttribute("email", email);
        UserInfo userInfo = gpsService.loadLocation(ip, email);
        model.addAttribute(userInfo);
        return "map";
    }


}
