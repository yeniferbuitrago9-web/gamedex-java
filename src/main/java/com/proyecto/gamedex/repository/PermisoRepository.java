package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gamedex.model.Permiso;

public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
}
