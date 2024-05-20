package com.example.diplomchick.controller;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class MainController {
    @GetMapping("/")
    public String start() {
        return "index";
    }
}
