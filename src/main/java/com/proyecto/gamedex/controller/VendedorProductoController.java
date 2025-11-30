package com.proyecto.gamedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;

import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.service.CategoriaService;
import com.proyecto.gamedex.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/vendedor/productos")
@RequiredArgsConstructor

public class VendedorProductoController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", productoService.listar());
        return "vendedor/productos"; // vista dentro de templates/vendedor/
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar()); // <--
        return "vendedor/productos_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.crear(producto);
        return "redirect:/vendedor/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listar());
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
