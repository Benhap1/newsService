package com.example.newsservice.config;

import com.example.newsservice.model.User;
import com.example.newsservice.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        User admin = new User();
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.setPassword(passwordEncoder.encode("admin")); // Захэшированный пароль
        admin.setRoles(new HashSet<>(Set.of("ROLE_ADMIN"))); // Роли в виде строк
        userRepository.findByUsername(admin.getUsername())
                .orElseGet(() -> userRepository.save(admin)); // Проверка на существование

        User user = new User();
        user.setUsername("user");
        user.setEmail("user@example.com");
        user.setPassword(passwordEncoder.encode("password")); // Захэшированный пароль
        user.setRoles(new HashSet<>(Set.of("ROLE_USER"))); // Роли в виде строк
        userRepository.findByUsername(user.getUsername())
                .orElseGet(() -> userRepository.save(user));

        User moderator = new User();
        moderator.setUsername("moderator");
        moderator.setEmail("moderator@example.com");
        moderator.setPassword(passwordEncoder.encode("password")); // Захэшированный пароль
        moderator.setRoles(new HashSet<>(Set.of("ROLE_MODERATOR"))); // Роли в виде строк
        userRepository.findByUsername(moderator.getUsername())
                .orElseGet(() -> userRepository.save(moderator));
    }
}