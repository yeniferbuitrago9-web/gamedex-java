package com.proyecto.gamedex.config;

import com.proyecto.gamedex.model.*;
import com.proyecto.gamedex.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepo, RolRepository rolRepo, 
                               PasswordEncoder encoder) {
        return args -> {
            // 🔹 Verificamos si ya existen roles en la base de datos
            Rol admin = rolRepo.findByNombre("ADMINISTRADOR")
                    .orElseThrow(() -> new RuntimeException("No se encontró el rol ADMINISTRADOR en la base de datos"));

            Rol vendedor = rolRepo.findByNombre("VENDEDOR")
                    .orElseThrow(() -> new RuntimeException("No se encontró el rol VENDEDOR en la base de datos"));

            Rol comprador = rolRepo.findByNombre("COMPRADOR")
                    .orElseThrow(() -> new RuntimeException("No se encontró el rol COMPRADOR en la base de datos"));

            // 🔹 Creamos un usuario administrador si no existe
            if (usuarioRepo.findByEmail("admin@gamedex.com").isEmpty()) {
                Usuario adminUser = new Usuario();
                adminUser.setDocUsuario("1001");
                adminUser.setNombre("Administrador Gamedex");
                adminUser.setEmail("admin@gamedex.com");
                adminUser.setPassword(encoder.encode("12345"));
                adminUser.setRoles(Set.of(admin));
                usuarioRepo.save(adminUser);
            }

            // 🔹 Creamos un usuario vendedor si no existe
            if (usuarioRepo.findByEmail("vendedor@gamedex.com").isEmpty()) {
                Usuario vendedorUser = new Usuario();
                vendedorUser.setDocUsuario("1002");
                vendedorUser.setNombre("Vendedor Gamedex");
                vendedorUser.setEmail("vendedor@gamedex.com");
                vendedorUser.setPassword(encoder.encode("12345"));
                vendedorUser.setRoles(Set.of(vendedor));
                usuarioRepo.save(vendedorUser);
            }

            // 🔹 Creamos un usuario comprador si no existe
            if (usuarioRepo.findByEmail("comprador@gamedex.com").isEmpty()) {
                Usuario compradorUser = new Usuario();
                compradorUser.setDocUsuario("1003");
                compradorUser.setNombre("Comprador Gamedex");
                compradorUser.setEmail("comprador@gamedex.com");
                compradorUser.setPassword(encoder.encode("12345"));
                compradorUser.setRoles(Set.of(comprador));
                usuarioRepo.save(compradorUser);
            }

            System.out.println("✅ Datos iniciales cargados correctamente.");
        };
    }
}
