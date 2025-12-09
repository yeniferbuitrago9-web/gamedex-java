package com.proyecto.gamedex.repository;

import com.proyecto.gamedex.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {
    List<CarritoItem> findByCarrito_IdCarrito(Integer idCarrito);

    @Modifying

    @Query("DELETE FROM CarritoItem ci WHERE ci.producto.idProducto = :idProducto")
    void deleteByProductoId(@Param("idProducto") Integer idProducto);

    CarritoItem findByCarrito_IdCarritoAndProducto_IdProducto(Integer idCarrito, Integer idProducto);

    @Query("SELECT ci FROM CarritoItem ci WHERE ci.producto.idProducto = :idProducto")
    List<CarritoItem> findByProductoId(@Param("idProducto") Integer idProducto);

}
