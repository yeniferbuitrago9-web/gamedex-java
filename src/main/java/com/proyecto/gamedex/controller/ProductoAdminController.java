package com.proyecto.gamedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.service.CategoriaService;
import com.proyecto.gamedex.service.ProductoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/productos")
@RequiredArgsConstructor
public class ProductoAdminController {

    private final ProductoService productoService;
    private final CategoriaService categoriaService;

    // ⬅️ MODIFICADO: ahora también maneja búsqueda
    @GetMapping
    public String listar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model
    ) {

        // Si NO hay búsqueda → listar normal
        if ((nombre == null || nombre.isEmpty()) && precioMin == null && precioMax == null) {
            model.addAttribute("productos", productoService.listar());
        } else {
            // Si hay filtros → buscar
            model.addAttribute("productos",
                    productoService.buscarPorFiltros(nombre, precioMin, precioMax));
        }

        // Mantener valores en inputs
        model.addAttribute("nombreBusqueda", nombre);
        model.addAttribute("precioMinBusqueda", precioMin);
        model.addAttribute("precioMaxBusqueda", precioMax);

        // Mostrar botón "limpiar"
        boolean hayBusqueda = (nombre != null && !nombre.isEmpty())
                || precioMin != null
                || precioMax != null;
        model.addAttribute("hayBusqueda", hayBusqueda);

        return "admin/productos";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaService.listar());
        return "admin/productos_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Producto producto) {
        productoService.crear(producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaService.listar());
        return "admin/productos_form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizar(@PathVariable Integer id, @ModelAttribute Producto producto) {
        productoService.actualizar(id, producto);
        return "redirect:/admin/productos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/admin/productos";
    }
}
