package com.proyecto.gamedex.controller;

import java.util.Optional;
import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.RolRepository;
import com.proyecto.gamedex.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private RolRepository rolRepo;

    // ----- CRUD USUARIOS -----
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepo.findAll()); // <--- aquí
        return "admin/usuario_form";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Usuario usuario, Model model) {
        boolean esEdicion = usuario.getIdUsuario() != null;

        // Verificar email duplicado solo si pertenece a otro usuario
        Optional<Usuario> existente = repo.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            Usuario u = existente.get();
            if (!esEdicion || !u.getIdUsuario().equals(usuario.getIdUsuario())) {
                model.addAttribute("usuario", usuario);
                model.addAttribute("roles", rolRepo.findAll());
                model.addAttribute("error", "El email ya está en uso");
                return "admin/usuario_form"; // <-- retornar formulario con mensaje
            }
        }

        if (esEdicion) {
            // Actualizar usuario existente
            Usuario u = repo.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            u.setDocUsuario(usuario.getDocUsuario());
            u.setTelefono(usuario.getTelefono());
            u.setRoles(usuario.getRoles());

            // Solo actualizar password si se ingresó
            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                u.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            }

            repo.save(u);
        } else {
            // Crear nuevo usuario
            usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            repo.save(usuario);
        }

        return "redirect:/usuarios/lista";
    }

    @GetMapping("/lista")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        return "admin/usuarios";
    }

    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String editar(@PathVariable Integer id, Model model) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        u.setPassword("");
        model.addAttribute("usuario", u);
        model.addAttribute("roles", rolRepo.findAll()); // <--- aquí también
        return "admin/usuario_form";
    }

    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String eliminar(@PathVariable Integer id) {
        repo.deleteById(id);
        return "redirect:/usuarios/lista";
    }

    // ----- PERFIL DEL USUARIO -----
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        usuario.setPassword("");
        model.addAttribute("usuario", usuario);

        // Redirigir según rol
        String rol = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");

        switch (rol) {
            case "ROLE_ADMINISTRADOR":
                return "admin/usuario_form";
            case "ROLE_VENDEDOR":
                return "vendedor/perfil";
            case "ROLE_COMPRADOR":
                return "comprador/perfil";
            default:
                return "error";
        }
    }

    @PostMapping("/perfil/guardar")
    public String guardarPerfil(@ModelAttribute Usuario usuario, Authentication auth) {
        String email = auth.getName();
        Usuario actual = repo.findByEmail(email).orElseThrow();
        actual.setNombre(usuario.getNombre());
        actual.setTelefono(usuario.getTelefono());
        if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
            actual.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
        }
        repo.save(actual);

        // Redirigir según rol
        String rol = auth.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(r -> r.equals("ROLE_ADMINISTRADOR") || r.equals("ROLE_VENDEDOR") || r.equals("ROLE_COMPRADOR"))
                .findFirst()
                .orElse(null);

        if (rol == null)
            return "redirect:/auth/login?error";

        switch (rol) {
            case "ROLE_ADMINISTRADOR":
                return "redirect:/admin/home?actualizado";
            case "ROLE_VENDEDOR":
                return "redirect:/vendedor/home?actualizado";
            case "ROLE_COMPRADOR":
                return "redirect:/comprador/home?actualizado";
            default:
                return "redirect:/auth/login?error";
        }
    }
}
