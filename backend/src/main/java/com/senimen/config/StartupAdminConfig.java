package com.senimen.config;

import com.senimen.models.User;
import com.senimen.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class StartupAdminConfig implements CommandLineRunner {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Value("${app.admin.email:admin@senimen.local}")
    private String adminEmail;
    @Value("${app.admin.password:admin123}")
    private String adminPassword;

    public StartupAdminConfig(UserRepository users, PasswordEncoder encoder){ this.users = users; this.encoder = encoder; }

    @Override
    public void run(String... args) {
        users.findByEmail(adminEmail).orElseGet(() -> {
            User u = new User();
            u.setName("Admin");
            u.setEmail(adminEmail);
            u.setPassword(encoder.encode(adminPassword));
            u.setRole("admin");
            return users.save(u);
        });
    }
}
