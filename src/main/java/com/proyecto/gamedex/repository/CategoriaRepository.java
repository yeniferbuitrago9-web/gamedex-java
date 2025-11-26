// Repositorio
package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gamedex.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
