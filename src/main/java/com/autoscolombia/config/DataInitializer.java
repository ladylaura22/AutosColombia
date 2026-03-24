package com.autoscolombia.config;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.domain.Celda;
import com.autoscolombia.domain.Empleado;
import com.autoscolombia.domain.EstadoCelda;
import com.autoscolombia.domain.TipoCelda;
import com.autoscolombia.repository.AdministradorRepository;
import com.autoscolombia.repository.CeldaRepository;
import com.autoscolombia.repository.EmpleadoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(AdministradorRepository adminRepo,
                               EmpleadoRepository empleadoRepo,
                               CeldaRepository celdaRepo,
                               PasswordEncoder encoder) {
        return args -> {
            // Admin por defecto
            adminRepo.findByNombreUsuario("Laura").orElseGet(() -> {
                Administrador a = new Administrador();
                a.setNombreUsuario("Laura");
                a.setContrasenaHash(encoder.encode("admin123"));
                a.setNombreCompleto("Laura Administradora");
                a.setDocumento("1000000000");
                a.setEmail("admin@autoscolombia.com");
                a.setTelefono("3009876543");
                return adminRepo.save(a);
            });

            // Empleado demo
            if (!empleadoRepo.existsByEmail("empleado@autoscolombia.com")) {
                Empleado e = new Empleado();
                e.setNombreCompleto("Empleado Demo");
                e.setDocumento("1000000001");
                e.setEmail("empleado@autoscolombia.com");
                e.setTelefono("3001234567");
                e.setContrasenaHash(encoder.encode("empleado123"));
                empleadoRepo.save(e);
            }

            // Celdas de ejemplo
            if (celdaRepo.count() == 0) {
                crearCelda(celdaRepo, "A-101", TipoCelda.AUTO, "Piso 1");
                crearCelda(celdaRepo, "A-102", TipoCelda.AUTO, "Piso 1");
                crearCelda(celdaRepo, "A-103", TipoCelda.AUTO, "Piso 1");
                crearCelda(celdaRepo, "M-101", TipoCelda.MOTO, "Piso 1");
                crearCelda(celdaRepo, "M-102", TipoCelda.MOTO, "Piso 1");
                crearCelda(celdaRepo, "B-201", TipoCelda.AUTO, "Piso 2");
                crearCelda(celdaRepo, "B-202", TipoCelda.AUTO, "Piso 2");
            }
        };
    }

    private void crearCelda(CeldaRepository repo, String codigo, TipoCelda tipo, String ubicacion) {
        Celda c = new Celda();
        c.setCodigo(codigo);
        c.setTipo(tipo);
        c.setUbicacion(ubicacion);
        c.setEstado(EstadoCelda.DISPONIBLE);
        repo.save(c);
    }
}
