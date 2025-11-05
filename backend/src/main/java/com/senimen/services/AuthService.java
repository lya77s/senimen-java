package com.senimen.services;

import com.senimen.models.User;
import com.senimen.repositories.UserRepository;
import com.senimen.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthService(UserRepository userRepo, PasswordEncoder encoder, JwtUtil jwt) {
        this.userRepo = userRepo; this.encoder = encoder; this.jwt = jwt;
    }

    public Map<String,Object> register(String name, String email, String password, String role){
        if (role == null || role.isBlank()) role = "volunteer";
        if (userRepo.existsByEmail(email)) throw new RuntimeException("Email exists");
        User u = new User();
        u.setName(name); u.setEmail(email); u.setPassword(encoder.encode(password)); u.setRole(role);
        userRepo.save(u);
        return toAuthResponse(u);
    }

    public Map<String,Object> login(String email, String password){
        Optional<User> ou = userRepo.findByEmail(email);
        if(ou.isEmpty() || !encoder.matches(password, ou.get().getPassword())) throw new RuntimeException("Unauthorized");
        return toAuthResponse(ou.get());
    }

    private Map<String,Object> toAuthResponse(User u){
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", u.getRole());
        String token = jwt.generateToken(u.getId(), claims);
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("id", u.getId()); userMap.put("name", u.getName()); userMap.put("email", u.getEmail()); userMap.put("role", u.getRole());
        Map<String,Object> resp = new HashMap<>();
        resp.put("user", userMap); resp.put("token", token);
        return resp;
    }
}
