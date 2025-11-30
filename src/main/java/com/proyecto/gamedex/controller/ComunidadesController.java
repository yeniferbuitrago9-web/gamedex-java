package com.proyecto.gamedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comunidades")
public class ComunidadesController {

    @GetMapping("/home")
    public String homeComunidades() {
        return "comunidades/home"; 
    }
}
