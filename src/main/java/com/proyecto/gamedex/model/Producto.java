package com.proyecto.gamedex.model;

import jakarta.persistence.*;
feature
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "producto")
@Data
@AllArgsConstructor
@NoArgsConstructor
import lombok.Data;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "productos") main
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto feature
    private Integer idProducto;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer id;

    @Column(name = "usuario_id")
    private Integer usuarioId;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    private String nombre;

    @Column(columnDefinition = "text")
    private String descripcion;

    private BigDecimal precio;
 main
    private Integer cantidad;

    @Column(name = "dias_garantia")
    private Integer diasGarantia;
 feature
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt; main
}
