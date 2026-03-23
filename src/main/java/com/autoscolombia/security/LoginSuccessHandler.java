package com.autoscolombia.security;

import com.autoscolombia.domain.Empleado;
import com.autoscolombia.repository.EmpleadoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
        implements AuthenticationSuccessHandler {

    private final EmpleadoRepository empleadoRepository;

    public LoginSuccessHandler(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
        setDefaultTargetUrl("/menu");
        setAlwaysUseDefaultTargetUrl(true);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // Si el usuario autenticado es EMPLEADO, actualizar ultimoLogin y resetear intentos
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_EMPLEADO"))) {
            String email = authentication.getName();
            Optional<Empleado> opt = empleadoRepository.findByEmail(email);
            opt.ifPresent(e -> {
                e.setUltimoLogin(LocalDateTime.now());
                e.setIntentosFallidos(0);
                e.setBloqueoHasta(null);
                empleadoRepository.save(e);
            });
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
