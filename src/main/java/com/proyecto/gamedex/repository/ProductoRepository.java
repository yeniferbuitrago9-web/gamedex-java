package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gamedex.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

}
