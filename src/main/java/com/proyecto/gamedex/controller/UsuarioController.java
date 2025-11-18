package com.proyecto.gamedex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.proyecto.gamedex.repository.UsuarioRepository;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("mensaje", "Bienvenido a GameDex");
        return "index";
    }

    @GetMapping("/login")
    public String mostrarLogin() {
        return "login"; // carga el archivo login.html desde templates
    }

    @GetMapping("/usuarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public String listar(Model model) {
    model.addAttribute("usuarios", repo.findAll());
    return "usuarios";
}
}
