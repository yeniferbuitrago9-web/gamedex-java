package com.proyecto.gamedex.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vendedor")
public class VendedorController {

    @GetMapping("/home")
    public String homeVendedor() {
        return "vendedor/home";  // Vista: templates/vendedor/home.html
    }

    @GetMapping("/productos")
    public String productosVendedor() {
        return "vendedor/productos";  // Vista: templates/vendedor/productos.html
    }
}
