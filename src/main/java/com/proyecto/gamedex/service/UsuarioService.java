package com.proyecto.gamedex.service;

import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    // Crear
    public Usuario crear(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Listar todos
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    // Obtener por ID
    public Usuario obtenerUsuario(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    // Buscar por email
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Actualizar
    public Usuario actualizar(Integer id, Usuario usuarioActualizado) {
        Usuario u = obtenerUsuario(id);

        u.setNombre(usuarioActualizado.getNombre());
        u.setEmail(usuarioActualizado.getEmail());
        u.setTelefono(usuarioActualizado.getTelefono());
        u.setUpdatedAt(LocalDateTime.now());

        return usuarioRepository.save(u);
    }

    // Eliminar
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }
}
