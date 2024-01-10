package com.example.diplomchick.controller;

import com.example.diplomchick.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class EmailController {
    private final Map<String,Integer> savedValue=new HashMap<>();

    @Autowired
    private EmailService emailService;

    public EmailController() {
    }

    @PostMapping("/checkAccess/{email}/{value}")
    public boolean checkValue(@PathVariable String email,@PathVariable int value){


        if(savedValue.get(email)!=null){

            boolean result=savedValue.get(email)==value;
            savedValue.remove(email,value);
            return result;
        }
        return false;
    }

    @PostMapping("/sendEmail/{email}")
    public String sendEmail(@PathVariable String email) {
        Random random=new Random();
        int value=random.nextInt(0,100);
        savedValue.put(email,value);

        emailService.sendEmail(email,Integer.toString(value),"My Email");
        return "Email sent successfully!";
    }
}
