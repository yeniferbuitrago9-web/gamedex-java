package com.proyecto.gamedex.service;

import com.proyecto.gamedex.model.Carrito;
import com.proyecto.gamedex.model.CarritoItem;
import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.repository.CarritoItemRepository;
import com.proyecto.gamedex.repository.CarritoRepository;
import com.proyecto.gamedex.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CarritoItemService {

    private final CarritoRepository carritoRepository;
    private final ProductoRepository productoRepository;
    private final CarritoItemRepository carritoItemRepository;

    @Transactional
    public void agregarProductoAlCarrito(Integer idCarrito, Integer idProducto) {

        Carrito carrito = carritoRepository.findById(idCarrito)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // ✅ Verificar que el producto esté activo
        if (producto.getActivo() != null && !producto.getActivo()) {
            throw new RuntimeException("No se puede agregar un producto desactivado al carrito");
        }

        // 1️⃣ Verificar si ya existe item con ese producto
        CarritoItem itemExistente = carritoItemRepository.findByCarrito_IdCarritoAndProducto_IdProducto(idCarrito,
                idProducto);

        if (itemExistente != null) {
            // 2️⃣ Si existe, aumentar cantidad
            itemExistente.setCantidad(itemExistente.getCantidad() + 1);
            carritoItemRepository.save(itemExistente);

        } else {
            // 3️⃣ Si no existe, crear nuevo
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(1);
            carritoItemRepository.save(nuevoItem);
        }
    }
}
