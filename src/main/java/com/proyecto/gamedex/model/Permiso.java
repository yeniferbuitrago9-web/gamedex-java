package com.proyecto.gamedex.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permiso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;
}
