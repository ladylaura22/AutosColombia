package com.autoscolombia.security;

import com.autoscolombia.domain.Empleado;
import com.autoscolombia.repository.EmpleadoRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private static final int MAX_INTENTOS = 3;
    private static final int MINUTOS_BLOQUEO = 30;

    private final EmpleadoRepository empleadoRepository;

    public LoginFailureHandler(EmpleadoRepository empleadoRepository) {
        super("/login?error");
        this.empleadoRepository = empleadoRepository;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        if (username != null) {
            Optional<Empleado> opt = empleadoRepository.findByEmail(username.trim().toLowerCase());
            opt.ifPresent(e -> {
                int intentos = e.getIntentosFallidos() + 1;
                e.setIntentosFallidos(intentos);
                if (intentos >= MAX_INTENTOS) {
                    e.setBloqueoHasta(LocalDateTime.now().plusMinutes(MINUTOS_BLOQUEO));
                }
                empleadoRepository.save(e);
            });
        }
        super.onAuthenticationFailure(request, response, exception);
    }
}
