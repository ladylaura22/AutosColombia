package com.autoscolombia.security;

import com.autoscolombia.domain.Administrador;
import com.autoscolombia.repository.AdministradorRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;

import java.util.List;

// Note: funcionalidad absorbida por CombinedUserDetailsService.
// Se mantiene como referencia pero ya no se registra como bean de Spring.
public class AdminUserDetailsService implements UserDetailsService {

    private final AdministradorRepository administradorRepository;

    public AdminUserDetailsService(AdministradorRepository administradorRepository) {
        this.administradorRepository = administradorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Administrador admin = administradorRepository.findByNombreUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return new User(
                admin.getNombreUsuario(),
                admin.getContrasenaHash(),
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
    }
}
