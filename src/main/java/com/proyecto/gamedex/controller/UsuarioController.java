package com.proyecto.gamedex.controller;

import java.util.Optional;
import java.util.List;

import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.repository.RolRepository;
import com.proyecto.gamedex.repository.UsuarioRepository;
import com.proyecto.gamedex.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;

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

    @Autowired
    private ReporteService reporteService;

    // ------------------- CREAR USUARIO -------------------
    @GetMapping("/nuevo")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String nuevo(Model model) {
        model.addAttribute("usuario", new Usuario());
        model.addAttribute("roles", rolRepo.findAll());
        return "admin/usuario_form";
    }

    // ------------------- GUARDAR USUARIO -------------------
    @PostMapping("/guardar")
    public String guardar(
            @ModelAttribute Usuario usuario,
            @RequestParam("idRol") Integer idRol,
            Model model) {

        boolean esEdicion = usuario.getIdUsuario() != null;

        // Validar email duplicado
        Optional<Usuario> existente = repo.findByEmail(usuario.getEmail());
        if (existente.isPresent()) {
            Usuario u = existente.get();
            if (!esEdicion || !u.getIdUsuario().equals(usuario.getIdUsuario())) {

                model.addAttribute("usuario", usuario);
                model.addAttribute("roles", rolRepo.findAll());
                model.addAttribute("error", "El email ya está en uso");

                return "admin/usuario_form";
            }
        }

        // 👉 ASIGNAR EL ROL SELECCIONADO
        usuario.getRoles().clear();
        usuario.getRoles().add(rolRepo.findById(idRol).orElseThrow());

        if (esEdicion) {
            Usuario u = repo.findById(usuario.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            u.setNombre(usuario.getNombre());
            u.setEmail(usuario.getEmail());
            u.setDocUsuario(usuario.getDocUsuario());
            u.setTelefono(usuario.getTelefono());
            u.setRoles(usuario.getRoles());

            if (usuario.getPassword() != null && !usuario.getPassword().isBlank()) {
                u.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            }

            repo.save(u);

        } else {
            usuario.setPassword(new BCryptPasswordEncoder().encode(usuario.getPassword()));
            repo.save(usuario);
        }

        return "redirect:/usuarios/lista";
    }

    // ------------------- LISTAR -------------------
    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("usuarios", repo.findAll());
        model.addAttribute("roles", rolRepo.findAll());
        return "admin/usuarios";
    }

    // ------------------- EDITAR -------------------
    @GetMapping("/editar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String editar(@PathVariable Integer id, Model model) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        u.setPassword(""); // importante: para no mostrar el hash

        model.addAttribute("usuario", u);
        model.addAttribute("roles", rolRepo.findAll());

        return "admin/usuario_form";
    }

    // ------------------- ELIMINAR -------------------
    @GetMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public String eliminar(@PathVariable Integer id) {
        repo.deleteById(id);
        return "redirect:/usuarios/lista";
    }

    // ------------------- PERFIL DEL USUARIO LOGEADO -------------------
    @GetMapping("/perfil")
    public String perfil(Model model, Authentication auth) {
        String email = auth.getName();
        Usuario usuario = repo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        usuario.setPassword("");

        model.addAttribute("usuario", usuario);

        String rol = auth.getAuthorities()
                .stream()
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

    // ------------------- GUARDAR PERFIL -------------------
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

        String rol = auth.getAuthorities()
                .stream()
                .map(a -> a.getAuthority())
                .findFirst()
                .orElse("");

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

    // ------------------- BUSCAR -------------------
    @GetMapping("/buscar")
    public String buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String docUsuario,
            @RequestParam(required = false) Integer rolId,
            Model model) {

        List<Usuario> resultados = repo.buscarUsuarios(nombre, email, docUsuario, rolId);

        model.addAttribute("usuarios", resultados);
        model.addAttribute("roles", rolRepo.findAll());

        return "admin/usuarios";
    }

    // =====================================================
    // 🟦 EXPORTAR A PDF
    // =====================================================
    @GetMapping("/export/pdf")
    public void exportarPdf(HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.pdf");

        reporteService.exportarUsuariosPdf(repo.findAll(), response.getOutputStream());
    }

    // =====================================================
    // 🟩 EXPORTAR A EXCEL
    // =====================================================
    @GetMapping("/export/excel")
    public void exportarExcel(HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=usuarios.xlsx");

        var workbook = reporteService.exportarUsuariosExcel(repo.findAll());
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    

}
