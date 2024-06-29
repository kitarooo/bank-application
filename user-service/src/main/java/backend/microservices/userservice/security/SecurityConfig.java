package backend.microservices.testproject.security;

import backend.microservices.testproject.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfiguration {
    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public String[] PERMIT_ALL = {
            "/api/v1/users/**",
            "/api/v1/news/all",
            "/api/v1/news/{id}",
            "/api/v1/product/{id}",
            "/api/v1/products/all",
            "/api/v1/orders/create",
            "/swagger*/**",
            "/swagger-ui/**",
            "/backend/swagger-ui.html",
            "/documentation/**",
            "/v3/api-docs/**"
    };

    public String[] ADMIN = {
            "/api/v1/news/createNews",
            "/api/v1/products/add",
            "/api/v1/orders/all",
            "/api/v1/order/{id}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ADMIN).hasRole("ADMIN")
                        .requestMatchers(PERMIT_ALL).permitAll().anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).build();
    }
}
