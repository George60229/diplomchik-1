package com.example.diplomchick.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Вернуть имя HTML-шаблона для страницы входа
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register"; // Вернуть имя HTML-шаблона для страницы регистрации
    }

    // Другие методы для обработки входа и регистрации пользователей
}
