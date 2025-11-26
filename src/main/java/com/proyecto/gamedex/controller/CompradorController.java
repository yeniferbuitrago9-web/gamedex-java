package com.proyecto.gamedex.controller;

import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.UsuarioRepository;
import com.proyecto.gamedex.service.ProductoService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comprador")
@RequiredArgsConstructor
public class CompradorController {

    private final ProductoService productoService; // inyectamos el servicio
    private final UsuarioRepository usuarioRepo; // inyectamos el repositorio

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email del usuario logueado
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);
        model.addAttribute("usuario", usuario);
        return "comprador/home";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        // Obtener el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // email del usuario logueado

        // Buscar el Usuario en la base de datos
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        model.addAttribute("productos", productoService.listar());
        model.addAttribute("usuario", usuario); // ahora Thymeleaf puede usar usuario.idUsuario
        return "comprador/productos";
    }
}
