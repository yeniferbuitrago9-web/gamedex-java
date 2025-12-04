package com.proyecto.gamedex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import com.proyecto.gamedex.model.Usuario;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    @Query("""
            SELECT u FROM Usuario u
            JOIN u.roles r
            WHERE (:nombre IS NULL OR :nombre = '' OR u.nombre LIKE CONCAT('%', :nombre, '%'))
              AND (:email IS NULL OR :email = '' OR u.email LIKE CONCAT('%', :email, '%'))
              AND (:rolId IS NULL OR r.id = :rolId)
            """)
    List<Usuario> buscarUsuarios(
            @Param("nombre") String nombre,
            @Param("email") String email,
            @Param("docUsuario") String docUsuario,
            @Param("rolId") Integer rolId);

}
