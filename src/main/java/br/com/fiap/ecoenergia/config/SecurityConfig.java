package br.com.fiap.ecoenergia.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//TODO card 6 do trello: buscar usuários na tb_esg_usuario, usar BCrypt e definir rotas públicas e protegidas

// Configuração provisória de segurança; será completada no card 6
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desliga o CSRF, que não é usado em API REST
                .csrf(csrf -> csrf.disable())
                // Todas as rotas exigem login
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                // Login via HTTP Basic (usuário e senha no cabeçalho)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}