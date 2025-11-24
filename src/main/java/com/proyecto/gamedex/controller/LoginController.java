package com.proyecto.gamedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Página de login
    @GetMapping("/login")
    public String login() {
        return "login"; // templates/login.html
    }

    // Página de acceso denegado
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied"; // templates/access_denied.html
    }
}
