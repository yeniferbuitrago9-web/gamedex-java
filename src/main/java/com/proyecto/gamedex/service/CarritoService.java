package com.proyecto.gamedex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyecto.gamedex.model.*;
import com.proyecto.gamedex.repository.*;

@Service
@RequiredArgsConstructor
public class CarritoService {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository itemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    // Crear un carrito o devolver el existente
    public Carrito crearCarrito(Integer idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no existe"));

        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElseGet(() -> {
                    Carrito nuevo = new Carrito();
                    nuevo.setUsuario(usuario);
                    nuevo.setCantidad(0);
                    return carritoRepository.save(nuevo);
                });
    }

    // Agregar items al carrito
    public Carrito agregarItem(Integer idCarrito, Integer idProducto, Integer cantidad) {

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no existe"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

        CarritoItem item = new CarritoItem();
        item.setCarrito(carrito);
        item.setProducto(producto);
        item.setCantidad(cantidad);

        itemRepository.save(item);

        carrito.setCantidad(carrito.getCantidad() + cantidad);
        return carritoRepository.save(carrito);
    }

    // Actualizar cantidad de un item
    public CarritoItem actualizarCantidad(Integer idItem, Integer cantidad) {
        CarritoItem item = itemRepository.findById(idItem)
                .orElseThrow(() -> new RuntimeException("Item no existe"));

        item.setCantidad(cantidad);
        return itemRepository.save(item);
    }

    // Eliminar item
    public void eliminarItem(Integer idItem) {
        itemRepository.deleteById(idItem);
    }

    // Obtener carrito por usuario
    public Carrito obtenerPorUsuario(Integer idUsuario) {
        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElse(null);
    }
    public void agregarItemPorUsuario(Integer idUsuario, Integer idProducto, Integer cantidad) {
    Carrito carrito = crearCarrito(idUsuario); // obtiene o crea
    Producto producto = productoRepository.findById(idProducto)
            .orElseThrow(() -> new RuntimeException("Producto no existe"));

    CarritoItem item = new CarritoItem();
    item.setCarrito(carrito);
    item.setProducto(producto);
    item.setCantidad(cantidad);
    itemRepository.save(item);

    carrito.setCantidad(carrito.getCantidad() + cantidad);
    carritoRepository.save(carrito);
}

}
