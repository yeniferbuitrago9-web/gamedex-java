package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.proyecto.gamedex.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
}
