package com.proyecto.gamedex.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol_permiso")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RolPermiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "id_permiso", nullable = false)
    private Permiso permiso;
}
