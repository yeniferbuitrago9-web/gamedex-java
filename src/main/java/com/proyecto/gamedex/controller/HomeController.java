package com.proyecto.gamedex.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {
    
      @GetMapping("/")
    public String landing() {
        return "landing"; 
    }

    @GetMapping("/login")
    public String login() {
        return "login"; 
    }

     // Página de acceso denegado
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied"; // templates/access_denied.html
    }

    

}
