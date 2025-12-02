package com.proyecto.gamedex.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.proyecto.gamedex.model.Producto;
import com.proyecto.gamedex.repository.ProductoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

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
        existente.setUsuario(p.getUsuario());

        return productoRepository.save(existente);
    }

    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    // 🔥 AGREGADO: método para filtrar productos
    public List<Producto> buscarPorFiltros(String nombre, Double min, Double max) {
        return productoRepository.buscarPorFiltros(nombre, min, max);
    }
}
