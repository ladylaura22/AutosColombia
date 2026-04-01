import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/img/**", "/favicon.png").permitAll()
                .requestMatchers("/pagos/*/anular").hasRole("ADMIN")
                .requestMatchers("/tarifas/**").hasRole("ADMIN")
                .requestMatchers("/reportes/**").hasRole("ADMIN")
                .requestMatchers("/pagos/**").hasAnyRole("ADMIN","EMPLEADO")
                .anyRequest().authenticated()
            );
        return http.build();
    }
}