package com.proyecto.gamedex.controller;

import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.UsuarioRepository;
import com.proyecto.gamedex.service.ProductoService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comprador")
@RequiredArgsConstructor
public class CompradorController {

    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepo;

    @GetMapping("/home/{idUsuario}")
    public String home(@PathVariable("idUsuario") Integer idUsuario, Model model) {
        Usuario usuario = usuarioRepo.findById(idUsuario).orElse(null);
        model.addAttribute("usuario", usuario);

        // 🔥 ACTIVAR BUSCADOR
        model.addAttribute("mostrarBuscador", true);

        return "comprador/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        model.addAttribute("usuario", usuario);

        // 🔥 ACTIVAR BUSCADOR
        model.addAttribute("mostrarBuscador", true);

        return "comprador/home";
    }

    @GetMapping("/productos")
public String productos(
        @RequestParam(required = false) String nombre,
        @RequestParam(required = false) Double precioMin,
        @RequestParam(required = false) Double precioMax,
        Model model) {

    // Obtener el usuario actual
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();
    Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

    // 🔥 FILTRAR PRODUCTOS SEGÚN PARÁMETROS
    model.addAttribute("productos", productoService.buscarPorFiltros(
            nombre != null ? nombre : "",
            precioMin != null ? precioMin : 0.0,
            precioMax != null ? precioMax : Double.MAX_VALUE
    ));

    model.addAttribute("usuario", usuario);
    model.addAttribute("mostrarBuscador", true);

    return "comprador/productos";
}
}
