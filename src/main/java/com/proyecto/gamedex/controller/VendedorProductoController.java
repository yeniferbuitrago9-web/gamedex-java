package com.proyecto.gamedex.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.UsuarioRepository;
import com.proyecto.gamedex.service.CategoriaService;
import com.proyecto.gamedex.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/vendedor/productos")
@RequiredArgsConstructor
public class VendedorProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;
    private final UsuarioRepository usuarioRepo;

    // Eliminamos el método listar(), porque ahora lo maneja VendedorController

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("mostrarBuscador", true);
        return "vendedor/productos_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        // Obtener el usuario logueado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Usuario usuario = usuarioRepo.findByEmail(email).orElse(null);

        producto.setUsuario(usuario); // asignamos el vendedor logueado
        productoService.crear(producto);
        return "redirect:/vendedor/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listar());
        model.addAttribute("mostrarBuscador", true);
        return "vendedor/productos_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute Producto producto) {
        productoService.actualizar(id, producto);
        return "redirect:/vendedor/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/vendedor/productos";
    }
}
