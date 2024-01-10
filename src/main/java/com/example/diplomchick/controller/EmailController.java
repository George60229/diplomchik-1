package com.example.diplomchick.controller;

import com.example.diplomchick.service.EmailService;
import com.example.diplomchick.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class EmailController {
    private final Map<String, Integer> savedValue = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @Autowired
    private GPSService gpsService;

    public EmailController() {
    }

    @PostMapping("/checkAccess/{email}/{value}")
    public boolean checkValue(@PathVariable String email, @PathVariable int value) {


        if (savedValue.get(email) != null) {

            boolean result = savedValue.get(email) == value;
            savedValue.remove(email, value);
            return result;
        }
        return false;
    }

    @PostMapping("/sendEmail/{email}")
    public String sendEmail(@PathVariable String email) {
        Random random = new Random();
        int value = random.nextInt(0, 100);
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

        return gpsService.loadLocation();
    }
}
