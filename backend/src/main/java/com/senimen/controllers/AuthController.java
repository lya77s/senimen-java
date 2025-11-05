package com.senimen.controllers;

import com.senimen.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    public AuthController(AuthService authService){ this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String,Object> body){
        try{
            String name = (String) body.get("name");
            String email = (String) body.get("email");
            String password = (String) body.get("password");
            String role = body.get("role")!=null? body.get("role").toString(): null;
            return ResponseEntity.ok(authService.register(name,email,password,role));
        }catch(Exception e){ return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,Object> body){
        try{
            String email = (String) body.get("email");
            String password = (String) body.get("password");
            return ResponseEntity.ok(authService.login(email,password));
        }catch(Exception e){ return ResponseEntity.status(401).body(Map.of("error","Unauthorized")); }
    }
}
