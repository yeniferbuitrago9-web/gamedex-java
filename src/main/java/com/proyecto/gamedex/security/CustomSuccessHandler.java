package com.proyecto.gamedex.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import com.proyecto.gamedex.model.Usuario;
import com.proyecto.gamedex.service.UsuarioService;
import org.springframework.stereotype.Component;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UsuarioService usuarioService;

    public CustomSuccessHandler(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException {

        // Obtener rol
        String rol = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(r -> r.equals("ROLE_ADMINISTRADOR") || r.equals("ROLE_VENDEDOR") || r.equals("ROLE_COMPRADOR"))
                .findFirst()
                .orElse(null);

        if (rol == null) {
            response.sendRedirect("/login?error");
            return;
        }

        // Obtener usuario
        String username = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(username);
        if (usuarioOpt.isEmpty()) {
            response.sendRedirect("/login?error");
            return;
        }

        // ⚠ Aquí usamos el nombre real del ID: idUsuario
        Integer idUsuario = usuarioOpt.get().getIdUsuario();

        // Redirigir según rol
        switch (rol) {
            case "ROLE_ADMINISTRADOR":
                response.sendRedirect("/admin/home");
                break;
            case "ROLE_VENDEDOR":
                response.sendRedirect("/vendedor/home");
                break;
            case "ROLE_COMPRADOR":
                response.sendRedirect("/comprador/home/" + idUsuario);
                break;
            default:
                response.sendRedirect("/login?error");
        }
    }
}
