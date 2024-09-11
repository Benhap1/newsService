package com.example.newsservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        // Доступ к аутентификации и регистрации
                        .requestMatchers("/api/auth/**").permitAll()

                        // Доступ к пользователям
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ROLE_ADMIN") // Только администратор может получить список пользователей
                        .requestMatchers(HttpMethod.POST, "/api/users").hasAuthority("ROLE_ADMIN") // Только администратор может создать пользователей
                        .requestMatchers(HttpMethod.POST, "/api/users/assign-role").hasAuthority("ROLE_ADMIN") // Только администратор может назначать роли
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Получение пользователя по ID
                        .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Обновление пользователя по ID
                        .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER") // Удаление пользователя по ID

                        // Доступ к новостям
                        .requestMatchers(HttpMethod.GET, "/api/news/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Получение списка новостей и новостей по ID
                        .requestMatchers(HttpMethod.POST, "/api/news/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Создание новостей
                        .requestMatchers(HttpMethod.PUT, "/api/news/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Обновление новостей
                        .requestMatchers(HttpMethod.DELETE, "/api/news/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Удаление новостей

                        // Доступ к категориям
                        .requestMatchers(HttpMethod.GET, "/api/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Получение категорий и категории по ID
                        .requestMatchers(HttpMethod.POST, "/api/categories/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR") // Создание категории
                        .requestMatchers(HttpMethod.PUT, "/api/categories/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR") // Обновление категории
                        .requestMatchers(HttpMethod.DELETE, "/api/categories/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_MODERATOR") // Удаление категории

                        // Доступ к комментариям
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Получение комментариев
                        .requestMatchers(HttpMethod.POST, "/api/comments/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Создание комментариев
                        .requestMatchers(HttpMethod.PUT, "/api/comments/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Обновление комментариев
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/{id}").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MODERATOR") // Удаление комментариев

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
