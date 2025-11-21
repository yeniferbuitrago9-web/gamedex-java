package com.proyecto.gamedex.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    private BigDecimal precio;

    private Integer cantidad;

    @Column(name = "dias_garantia")
    private Integer diasGarantia;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
