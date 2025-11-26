package com.proyecto.gamedex.repository;

import com.proyecto.gamedex.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
    List<CarritoItem> findByCarrito_IdCarrito(Integer idCarrito);
}
