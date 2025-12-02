package com.proyecto.gamedex.controller;

import com.proyecto.gamedex.model.Carrito;
import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.service.CarritoService;
import com.proyecto.gamedex.service.ProductoService;
import com.proyecto.gamedex.service.UsuarioService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/producto")  // <--- OJO: nada de /admin aquí
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final UsuarioService UsuarioService;
    private final CarritoService CarritoService;

    // Página pública de productos
    @GetMapping
    public String verProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) Double precioMin,
            @RequestParam(required = false) Double precioMax,
            Model model
    ) {

        // Filtro seguro
        List<Producto> productos;

        if (nombre != null || precioMin != null || precioMax != null) {
            productos = productoService.buscarPorFiltros(nombre, precioMin, precioMax);
        } else {
            productos = productoService.listar();
        }

        model.addAttribute("productos", productos);

        // Datos del usuario (temporal)
        Integer idUsuario = 1;
        model.addAttribute("usuario", UsuarioService.obtenerUsuario(idUsuario));

        // Carrito
        Carrito carrito = CarritoService.obtenerPorUsuario(idUsuario);
        if (carrito == null) {
            carrito = CarritoService.crearCarrito(idUsuario);
        }
        model.addAttribute("carrito", carrito);

        return "productos";
    }

    // Crear producto (solo si deseas modo público)
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto-form";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoService.crear(producto);
        return "redirect:/producto";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "producto-form";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute("producto") Producto producto) {
        productoService.actualizar(id, producto);
        return "redirect:/producto";
    }

    // IMPORTANTE:
    // YA NO USAMOS /admin/productos/eliminar EN ESTE CONTROLADOR
    // para evitar conflicto con el AdminController
    @GetMapping("/eliminar-publico/{id}")
    public String eliminarProductoPublico(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/producto";
    }
}
