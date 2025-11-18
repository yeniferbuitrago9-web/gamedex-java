package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gamedex.model.Rol;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Integer> {
    Optional<Rol> findByNombre(String nombre);
}
