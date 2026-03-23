package com.autoscolombia.security;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.domain.Empleado;
import com.autoscolombia.repository.AdministradorRepository;
import com.autoscolombia.repository.EmpleadoRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * UserDetailsService unificado que permite login de:
 * - Administrador (por nombreUsuario) => ROLE_ADMIN
 * - Empleado (por email)              => ROLE_EMPLEADO
 *
 * Se intenta primero por Administrador; si no se encuentra, se busca Empleado.
 */
@Service
public class CombinedUserDetailsService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;
    private final EmpleadoRepository empleadoRepository;

    public CombinedUserDetailsService(AdministradorRepository administradorRepository,
                                      EmpleadoRepository empleadoRepository) {
        this.administradorRepository = administradorRepository;
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Intentar cargar como Administrador (por nombreUsuario)
        Optional<Administrador> adminOpt = administradorRepository.findByNombreUsuario(username);
        if (adminOpt.isPresent()) {
            Administrador admin = adminOpt.get();
            return new User(
                    admin.getNombreUsuario(),
                    admin.getContrasenaHash(),
                    List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
            );
        }

        // 2. Intentar cargar como Empleado (por email)
        Empleado empleado = empleadoRepository.findByEmail(username.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Verificar bloqueo
        if (empleado.getBloqueoHasta() != null && empleado.getBloqueoHasta().isAfter(LocalDateTime.now())) {
            throw new UsernameNotFoundException("Cuenta bloqueada temporalmente. Intente más tarde.");
        }

        return new User(
                empleado.getEmail(),
                empleado.getContrasenaHash(),
                List.of(new SimpleGrantedAuthority("ROLE_EMPLEADO"))
        );
    }
}
