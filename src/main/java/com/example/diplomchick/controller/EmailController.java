package com.example.diplomchick.controller;


import com.example.diplomchick.dto.UserInfo;
import com.example.diplomchick.service.EmailService;
import com.example.diplomchick.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String sendEmail(@PathVariable String email, Model model) {
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

        UserInfo userInfo=gpsService.loadLocation();
        model.addAttribute(userInfo);
        return "map";
    }


}
