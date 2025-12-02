package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.proyecto.gamedex.model.Producto;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // 🔥 BÚSQUEDA MULTICRITERIO 100% COMPATIBLE
    @Query(
        "SELECT p FROM Producto p " +
        "WHERE (:nombre IS NULL OR LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
        "AND (:precioMin IS NULL OR p.precio >= :precioMin) " +
        "AND (:precioMax IS NULL OR p.precio <= :precioMax)"
    )
    List<Producto> buscarPorFiltros(
            @Param("nombre") String nombre,
            @Param("precioMin") Double precioMin,
            @Param("precioMax") Double precioMax
    );
}
