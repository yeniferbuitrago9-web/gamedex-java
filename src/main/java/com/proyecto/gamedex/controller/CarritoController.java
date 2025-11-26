package com.proyecto.gamedex.controller;

import com.proyecto.gamedex.model.Carrito;
import com.proyecto.gamedex.service.CarritoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/carrito")
@RequiredArgsConstructor
public class CarritoController {

    private final CarritoService carritoService;

    // Ver el carrito de un usuario
    @GetMapping("/usuario/{idUsuario}")
    public String verCarrito(@PathVariable Integer idUsuario, Model model) {
        Carrito carrito = carritoService.obtenerPorUsuario(idUsuario);
        if (carrito != null) {
            double total = carrito.getItems().stream()
                    .mapToDouble(item -> item.getProducto().getPrecio() * item.getCantidad())
                    .sum();
            model.addAttribute("carrito", carrito.getItems());
            model.addAttribute("usuario", carrito.getUsuario());
            model.addAttribute("carritoTotal", total);
        }
        return "comprador/carrito"; // nombre del HTML
    }

    // Crear carrito si no existe
    @GetMapping("/crear/{idUsuario}")
    public String crearCarrito(@PathVariable Integer idUsuario) {
        carritoService.crearCarrito(idUsuario);
        return "redirect:/carrito/usuario/" + idUsuario;
    }

    // Agregar producto al carrito
    @PostMapping("/agregar")
    public String agregarItem(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idProducto,
            @RequestParam Integer cantidad) {

        carritoService.agregarItemPorUsuario(idUsuario, idProducto, cantidad);
        return "redirect:/carrito/usuario/" + idUsuario;
    }

    // Actualizar cantidad de un item
    @PostMapping("/actualizar")
    public String actualizarCantidad(
            @RequestParam Integer idItem,
            @RequestParam Integer cantidad,
            @RequestParam Integer idUsuario) {

        carritoService.actualizarCantidad(idItem, cantidad);
        return "redirect:/carrito/usuario/" + idUsuario;
    }

    // Eliminar item usando PathVariable
    @PostMapping("/eliminar/{idItem}")
    public String eliminarItem(@PathVariable Integer idItem, @RequestParam Integer idUsuario) {
        carritoService.eliminarItem(idItem);
        return "redirect:/carrito/usuario/" + idUsuario;
    }

}
