package com.proyecto.gamedex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.CarritoItemRepository;
import com.proyecto.gamedex.repository.ProductoRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CarritoItemRepository carritoItemRepository;

    public List<Producto> listar() {
        return productoRepository.findAll();
    }

    public Producto buscarPorId(Integer id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public Producto crear(Producto p) {
        return productoRepository.save(p);
    }

    public Producto actualizar(Integer id, Producto p) {
        Producto existente = buscarPorId(id);

        existente.setNombre(p.getNombre());
        existente.setDescripcion(p.getDescripcion());
        existente.setPrecio(p.getPrecio());
        existente.setCantidad(p.getCantidad());
        existente.setDiasGarantia(p.getDiasGarantia());
        existente.setCategoria(p.getCategoria());
        existente.setUsuario(p.getUsuario()); // ⚠️ importante, asignar usuario correcto

        return productoRepository.save(existente);
    }

    @Transactional
    public boolean eliminar(Integer id) {
        try {
            carritoItemRepository.deleteByProductoId(id); // limpia carrito
            Producto producto = productoRepository.findById(id).orElseThrow();
            productoRepository.delete(producto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Transactional
    public ResultadoEliminacion eliminarODesactivarPorVendedor(Integer id, Usuario vendedor) {
        Producto producto = productoRepository.findByIdProductoAndUsuario(id, vendedor);
        if (producto == null)
            return ResultadoEliminacion.NO_EXISTE_O_NO_ES_TUYO;

        // Verificar si el producto está en algún carrito
        boolean enCarrito = carritoItemRepository.findByProductoId(id).size() > 0;

        if (enCarrito) {
            // No eliminar, solo desactivar
            producto.setActivo(false);
            productoRepository.save(producto);
            return ResultadoEliminacion.DESACTIVADO;
        }

        // Si no está en carrito, eliminar normalmente
        carritoItemRepository.deleteByProductoId(id); // por si acaso
        productoRepository.delete(producto);
        return ResultadoEliminacion.EXITO;
    }

    // 🔥 Método actual para filtros generales
    public List<Producto> buscarPorFiltros(String nombre, Double min, Double max) {
        return productoRepository.buscarPorFiltros(nombre, min, max);
    }

    // 🔥 Nuevo método: buscar productos de un vendedor específico con filtros
    public List<Producto> buscarPorVendedor(Usuario usuario, String nombre, Double min, Double max) {
        return productoRepository.findByUsuarioAndNombreContainingIgnoreCaseAndPrecioBetween(
                usuario,
                nombre != null ? nombre : "",
                min != null ? min : 0.0,
                max != null ? max : Double.MAX_VALUE);
    }
}
