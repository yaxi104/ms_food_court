package com.hexagonal.ms_foodcourt.infrastructure.configuration;

import com.hexagonal.ms_foodcourt.infrastructure.security.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_ADMIN;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_CLIENTE;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_EMPLEADO;
import static com.hexagonal.ms_foodcourt.domain.utils.Constants.ROLE_PROPIETARIO;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/configuration/**",
                                "/webjars/**",
                                "/favicon.ico",
                                "/swagger-ui/index.html"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/restaurant/admin").hasRole(ROLE_ADMIN)
                        .requestMatchers(HttpMethod.POST, "/api/v1/dish/owner").hasRole(ROLE_PROPIETARIO)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/dish/owner").hasRole(ROLE_PROPIETARIO)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/dish/toggle/status").hasRole(ROLE_PROPIETARIO)
                        .requestMatchers(HttpMethod.GET, "/api/v1/restaurant/all**").hasRole(ROLE_CLIENTE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/dish/all**").hasRole(ROLE_CLIENTE)
                        .requestMatchers(HttpMethod.POST, "/api/v1/order/customer").hasRole(ROLE_CLIENTE)
                        .requestMatchers(HttpMethod.GET, "/api/v1/order/all**").hasRole(ROLE_EMPLEADO)
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/order/assign/**").hasRole(ROLE_EMPLEADO)

                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Access denied\"}");
                        })
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}