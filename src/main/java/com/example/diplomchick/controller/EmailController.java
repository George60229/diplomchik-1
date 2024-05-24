package com.example.diplomchick.controller;


import com.example.diplomchick.model.MyUser;
import com.example.diplomchick.model.UserInfo;
import com.example.diplomchick.security.HashProvider;
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
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@SessionAttributes("check")
public class EmailController {
    private final Map<String, Integer> savedValue = new HashMap<>();

    private final Map<String, Integer> authorization = new HashMap<>();

    private final Map<String, Long> blockTimes = new HashMap<>();


    @Autowired
    private EmailService emailService;

    @Autowired
    private GPSService gpsService;

    @Autowired
    private UserService userService;

    @Autowired
    private HashProvider hashProvider;


    @PostMapping("/checkAccess/{email}/{value}")
    public String checkValue(@PathVariable String email, @PathVariable int value, Model model) {
        if (userService.checkBlock(email)) {
            return "WeBlockForLocationError";
        }
        if (savedValue.get(email) != null) {
            boolean result = savedValue.get(email) == value;
            savedValue.remove(email, value);
            if (userService.isAdmin(email)) {
                model.addAttribute("check", "done");
                return "redirect:/admin";
            }
            if (result) {
                model.addAttribute("email", email);
                return "success";
            }
        }
        return "error";
    }

    @PostMapping("/sendEmail/{email}")
    public String sendEmail(@PathVariable String email, Model model, @RequestParam String password) {
        if (!authorization.containsKey(email)) {
            authorization.put(email, 0);

        }
        if (authorization.get(email) > 2) {
            blockTimes.put(email, System.currentTimeMillis());
            authorization.remove(email);
            return "YouAreTemporaryBlocked";
        }
        if (blockTimes.containsKey(email) && System.currentTimeMillis() - blockTimes.get(email) < 1 * 60 * 1000) {
            return "YouAreTemporaryBlocked";
        } else {
            blockTimes.remove(email);
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String ip = "";

        if (authentication != null && authentication.getDetails() instanceof WebAuthenticationDetails) {
            WebAuthenticationDetails details = (WebAuthenticationDetails) authentication.getDetails();
            ip = details.getRemoteAddress();
        }
        String text = "";
        UserInfo userInfo = gpsService.loadLocation(ip, email);
        MyUser myUser = userService.getByEmail(email);
        if (myUser == null) {
            return "user_is_not_exist";
        }
        password = hashProvider.getPasswordWithSalt(password, myUser.getSalt());
        if (!userService.checkCredential(email, password)) {
            text = "\n" +
                    "Welcome to Our Website!\n" +
                    "\n" +
                    "For your security, we kindly ask you to inform about the attempt of login\n" +
                    "Your country :" + userInfo.getCountry() + "\n" +
                    "Your city :" + userInfo.getCity() + "\n" +
                    "Your IP :" + userInfo.getIp() + "\n" +
                    "\n" +
                    "Thank you for your cooperation.";
            emailService.sendEmail(email, "Wrong Login Attempt", text);
            authorization.put(email, authorization.get(email) + 1);
            return "wrong_credential_error";

        }
        Random random = new Random();
        int value = random.nextInt(100000, 999999);
        savedValue.put(email, value);
        model.addAttribute("email", email);
        model.addAttribute(userInfo);

        if (userService.checkBlock(email)) {
            text = "\n" +
                    "Welcome to Our Website!\n" +
                    "\n" +
                    "For your security, we kindly ask you about info, that you are blocked and its impossible to connect\n" +
                    "Your country :" + userInfo.getCountry() + "\n" +
                    "Your city :" + userInfo.getCity() + "\n" +
                    "Your IP :" + userInfo.getIp() + "\n" +
                    "\n" +
                    "Thank you for your cooperation.";
            emailService.sendEmail(email, "You are blocked, you can't enter", text);
            authorization.put(email, authorization.get(email) + 1);
            return "UserBlockError";
        }
        if (userService.isNewLocation(userInfo.getCountry(), email)) {
            MyUser user = userService.getByEmail(email);
            user.setBlocked(true);
            userService.updateUser(user, user.isBlocked(), "Location");
            text = """

                    WARNING!!!

                    We have blocked your access temporary because of your new location

                    Please contact admin to fix it

                    Thank you for your cooperation.""";
            emailService.sendEmail(user.getEmail(), "You are blocked!!!", text);
        }
        text = "\n" +
                "Welcome to Our Website!\n" +
                "\n" +
                "For your security, we will give you personal number here\n" +
                "Your country :" + userInfo.getCountry() + "\n" +
                "Your city :" + userInfo.getCity() + "\n" +
                "Your IP :" + userInfo.getIp() + "\n" +
                "Your Number :" + value + "\n" +
                "\n" +
                "Thank you for your cooperation.";
        emailService.sendEmail(email, "Confirm your login", text);


        return "map";
    }


}
