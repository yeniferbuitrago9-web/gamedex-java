package com.proyecto.gamedex.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/comunidades")
public class ComunidadesController {

    @GetMapping("/home")
    public String homeComunidades(Model model, Authentication auth) {

        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);

        return "comunidades/home";
    }

    @GetMapping("/zelda")
    public String comunidadZelda(Model model, Authentication auth) {

        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);

        return "comunidades/zelda";
    }

    @GetMapping("/hollow")
    public String comunidadHollow(Model model, Authentication auth) {

        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);

        return "comunidades/hollow";
    }

    @GetMapping("/pokemon")
    public String comunidadPokemon(Model model, Authentication auth) {

        String rol = auth.getAuthorities().iterator().next().getAuthority();
        model.addAttribute("rol", rol);

        return "comunidades/pokemon";
    }
}
