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
@RequestMapping("/producto")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;
    private final UsuarioService UsuarioService; // <--- agregar esto
    private final CarritoService CarritoService;

    // Lista todos los productos en la vista "productos.html"
    @GetMapping
    public String verProductos(Model model) {
        List<Producto> productos = productoService.listar();
        model.addAttribute("productos", productos);

        Integer idUsuario = 1; // temporal
        model.addAttribute("usuario", UsuarioService.obtenerUsuario(idUsuario));

        Carrito carrito = CarritoService.obtenerPorUsuario(idUsuario);
        if (carrito == null) {
            carrito = CarritoService.crearCarrito(idUsuario);
        }
        model.addAttribute("carrito", carrito);

        return "productos";
    }

    // Formulario para crear un producto
    @GetMapping("/nuevo")
    public String nuevoProducto(Model model) {
        model.addAttribute("producto", new Producto());
        return "producto-form"; // HTML con formulario
    }

    // Guardar producto (POST)
    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Producto producto) {
        productoService.crear(producto);
        return "redirect:/producto"; // redirige a la lista de productos
    }

    // Formulario para editar un producto
    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id);
        model.addAttribute("producto", producto);
        return "producto-form"; // mismo HTML de formulario
    }

    // Guardar cambios de edición
    @PostMapping("/actualizar/{id}")
    public String actualizarProducto(@PathVariable Integer id, @ModelAttribute("producto") Producto producto) {
        productoService.actualizar(id, producto);
        return "redirect:/producto";
    }

    // Eliminar producto
    @GetMapping("/eliminar/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        productoService.eliminar(id);
        return "redirect:/producto";
    }
}
