package br.com.fiap.recrutamento_justo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuração de segurança da aplicação.
 * Define:
 * - Política de sessão STATELESS (sem estado, usando JWT)
 * - Endpoints públicos e protegidos
 * - Controle de autorização baseado em roles (CANDIDATO, RH, ADMIN)
 * - Criptografia de senhas com BCrypt
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;

    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        // Endpoints públicos - Raiz e Health
                        .requestMatchers("/", "/health", "/actuator/**").permitAll()

                        // Autenticação - público
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registro").permitAll()

                        // Swagger/OpenAPI - público
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()

                        // Vagas - leitura pública, criação apenas RH/ADMIN
                        .requestMatchers(HttpMethod.GET, "/vagas/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/vagas").hasAnyRole("RH", "ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/vagas/**").hasAnyRole("RH", "ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vagas/**").hasAnyRole("RH", "ADMIN")

                        // Candidatos - apenas próprio candidato ou RH/ADMIN
                        .requestMatchers(HttpMethod.POST, "/candidatos").hasRole("CANDIDATO")
                        .requestMatchers("/candidatos/**").authenticated()

                        // Aplicações - candidato pode aplicar, RH pode gerenciar
                        .requestMatchers(HttpMethod.POST, "/aplicacoes").hasRole("CANDIDATO")
                        .requestMatchers("/aplicacoes/**").authenticated()

                        // Desafios - RH cria, candidato responde
                        .requestMatchers(HttpMethod.POST, "/desafios").hasAnyRole("RH", "ADMIN")
                        .requestMatchers("/desafios/**").authenticated()

                        // Demais endpoints requerem autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
