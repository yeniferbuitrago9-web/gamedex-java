package com.proyecto.gamedex.controller;

import com.proyecto.gamedex.model.Rol;
import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.RolRepository;
import com.proyecto.gamedex.service.UsuarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UsuarioService usuarioService;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    // Landing page
    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    // Login
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // Access denied
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access_denied";
    }

    // Mostrar formulario de registro
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro"; // templates/registro.html
    }

    // Procesar registro
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario,
            @RequestParam String tipoRol) {

        // Validar rol permitido
        if (!tipoRol.equalsIgnoreCase("COMPRADOR") && !tipoRol.equalsIgnoreCase("VENDEDOR")) {
            throw new RuntimeException("Rol no permitido");
        }

        // Buscar rol en la BD
        Rol rol = rolRepository.findByNombre(tipoRol.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        usuario.getRoles().add(rol);

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Guardar usuario
        usuarioService.crear(usuario);

        // Redirigir con parámetro de éxito
        return "redirect:/login?success";
    }

    @GetMapping("/inicio")
    public String redirigirSegunRol(Authentication authentication) {
        if (authentication == null) {
            return "redirect:/login";
        }

        for (GrantedAuthority ga : authentication.getAuthorities()) {
            String role = ga.getAuthority();
            if ("ROLE_ADMINISTRADOR".equals(role))
                return "redirect:/admin/home";
            if ("ROLE_VENDEDOR".equals(role))
                return "redirect:/vendedor/home";
            if ("ROLE_COMPRADOR".equals(role))
                return "redirect:/comprador/home";
        }

        return "redirect:/login?error=no-role";
    }

}
