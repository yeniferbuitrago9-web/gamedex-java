package com.proyecto.gamedex.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Buscar un rol válido según prioridad
        String rol = authentication.getAuthorities().stream()
                     .map(a -> a.getAuthority())
                     .filter(r -> r.equals("ROLE_ADMINISTRADOR") || r.equals("ROLE_VENDEDOR") || r.equals("ROLE_COMPRADOR"))
                     .findFirst()
                     .orElse(null);

        if (rol == null) {
            response.sendRedirect("/login?error");
            return;
        }

        // Redirigir según rol
        switch (rol) {
            case "ROLE_ADMINISTRADOR":
                response.sendRedirect("/admin/home");
                break;
            case "ROLE_VENDEDOR":
                response.sendRedirect("/vendedor/home");
                break;
            case "ROLE_COMPRADOR":
                response.sendRedirect("/comprador/home");
                break;
            default:
                response.sendRedirect("/login?error");
        }
    }
}
