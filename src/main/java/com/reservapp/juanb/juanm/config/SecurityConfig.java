package com.reservapp.juanb.juanm.config;

import com.reservapp.juanb.juanm.config.filters.JwtAuthFilter;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults; // <--- CAMBIO 2: Importar withDefaults

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(withDefaults()) // <--- CAMBIO 4: Habilitar CORS en la cadena de filtros de seguridad
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                
                // --- 0. PERMITIR PETICIONES PREFLIGHT (CORS) ---
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // <--- CAMBIO 5: Permitir todas las peticiones OPTIONS

                // --- 1. RUTAS PÚBLICAS (ACCESIBLES POR CUALQUIERA) ---
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/usuarios").permitAll()

                // --- 2. RUTAS PARA CLIENTES (Y TAMBIÉN PARA ADMINS) ---
                .requestMatchers(HttpMethod.PUT, "/reservas/**", "/usuarios/**", "/comentarios/**", "/pagos/**").hasAnyRole("CLIENTE", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/reservas", "/pagos", "/comentarios").hasAnyRole("CLIENTE", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PATCH, "/reservas/**/cancelar").hasAnyRole("CLIENTE", "ADMINISTRADOR")
                
                .requestMatchers(HttpMethod.GET, "/mesas", "/estados", "/tipos", "/metodos").authenticated()

                // --- 3. RUTAS EXCLUSIVAS PARA ADMINISTRADOR ---
                .anyRequest().hasRole("ADMINISTRADOR")
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
