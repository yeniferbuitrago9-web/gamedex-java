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
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/vendedor")
@RequiredArgsConstructor
public class VendedorController {

    private final ProductoService productoService;
    private final UsuarioRepository usuarioRepo;

    @GetMapping("/home")
    public String homeVendedor(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);
        model.addAttribute("usuario", usuario);
        return "vendedor/home"; 
    }

    // 🔥 productos con filtros y rol
    @GetMapping("/productos")
    public String productos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        model.addAttribute("productos", productoService.buscarPorFiltros(
                nombre != null ? nombre : "",
                precioMin != null ? precioMin : 0.0,
                precioMax != null ? precioMax : Double.MAX_VALUE
        ));

        model.addAttribute("usuario", usuario);
        model.addAttribute("mostrarBuscador", true);
        model.addAttribute("rol", "vendedor");
        model.addAttribute("nombreBusqueda", nombre);
        model.addAttribute("precioMinBusqueda", precioMin);
        model.addAttribute("precioMaxBusqueda", precioMax);
        model.addAttribute("hayBusqueda", nombre != null || precioMin != null || precioMax != null);

        return "vendedor/productos";
    }
}
