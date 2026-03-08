package com.autoscolombia.config;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.repository.AdministradorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initAdmin(AdministradorRepository repo, PasswordEncoder encoder) {
        return args -> {
            repo.findByNombreUsuario("Laura").orElseGet(() -> {
                Administrador a = new Administrador();
                a.setNombreUsuario("Laura");
                a.setContrasenaHash(encoder.encode("admin123"));
                return repo.save(a);
            });
        };
    }
}