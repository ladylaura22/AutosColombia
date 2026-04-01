package com.autoscolombia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CombinedUserDetailsService userDetailsService;
    private final LoginSuccessHandler loginSuccessHandler;
    private final LoginFailureHandler loginFailureHandler;

    public SecurityConfig(CombinedUserDetailsService userDetailsService,
                          LoginSuccessHandler loginSuccessHandler,
                          LoginFailureHandler loginFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.loginSuccessHandler = loginSuccessHandler;
        this.loginFailureHandler = loginFailureHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                // Recursos públicos
                .requestMatchers("/login", "/css/**", "/js/**", "/img/**", "/favicon.png", "/h2-console/**").permitAll()

                // Empleado y Admin: ver celdas y registrar ingreso/salida/buscar/parqueados
                .requestMatchers("/menu", "/ingreso", "/salida", "/buscar", "/parqueados").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers("/celdas").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers("/api/celdas").hasAnyRole("ADMIN", "EMPLEADO")
                .requestMatchers("/api/celdas/disponibles").hasAnyRole("ADMIN", "EMPLEADO")

                // Solo Admin: CRUD celdas, empleados y gestión de usuarios
                .requestMatchers("/api/celdas/**").hasRole("ADMIN")
                .requestMatchers("/empleados/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasRole("ADMIN")

                // Solo Admin: tarifas y reportes
                .requestMatchers("/tarifas/**", "/reportes/**").hasRole("ADMIN")

                // Anular pago: /pagos/{id}/anular (un solo segmento, patrón válido)
                .requestMatchers("/pagos/*/anular").hasRole("ADMIN")

                // Pagos en general: ADMIN y EMPLEADO
                .requestMatchers("/pagos/**").hasAnyRole("ADMIN", "EMPLEADO")

                // Resto: autenticado
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(loginSuccessHandler)
                .failureHandler(loginFailureHandler)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .headers(headers -> headers.frameOptions(fo -> fo.sameOrigin()))
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }
}