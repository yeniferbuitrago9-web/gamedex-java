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

    // Crear carrito
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

    // 👉 Agregar item al carrito (CORRECTO: suma si existe)
    public Carrito agregarItem(Integer idCarrito, Integer idProducto, Integer cantidad) {

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no existe"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));

        // 🔹 VERIFICAR SI EL PRODUCTO ESTÁ ACTIVO
        if (producto.getActivo() != null && !producto.getActivo()) {
            throw new RuntimeException("No se puede agregar un producto desactivado al carrito");
        }

        // 1️⃣ Verificar si ya existe el item
        CarritoItem itemExistente = itemRepository
                .findByCarrito_IdCarritoAndProducto_IdProducto(idCarrito, idProducto);

        if (itemExistente != null) {
            // 2️⃣ Si ya está en el carrito, aumentar cantidad
            itemExistente.setCantidad(itemExistente.getCantidad() + cantidad);
            itemRepository.save(itemExistente);
        } else {
            // 3️⃣ Si no existe, crearlo
            CarritoItem nuevo = new CarritoItem();
            nuevo.setCarrito(carrito);
            nuevo.setProducto(producto);
            nuevo.setCantidad(cantidad);
            itemRepository.save(nuevo);
        }

        // Actualizar cantidad total del carrito
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

    // Obtener carrito
    public Carrito obtenerPorUsuario(Integer idUsuario) {
        return carritoRepository.findByUsuario_IdUsuario(idUsuario)
                .orElse(null);
    }

    // Agregar item desde idUsuario (auto crea carrito)
    public void agregarItemPorUsuario(Integer idUsuario, Integer idProducto, Integer cantidad) {
        Carrito carrito = crearCarrito(idUsuario);

        // Reutilizar la misma lógica
        agregarItem(carrito.getIdCarrito(), idProducto, cantidad);
    }
}
