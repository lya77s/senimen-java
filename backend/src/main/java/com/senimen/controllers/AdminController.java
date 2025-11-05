package com.senimen.controllers;

import com.senimen.models.User;
import com.senimen.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.senimen.models.Event;
import com.senimen.repositories.EventRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService admin;
    private final EventRepository eventRepo;
    public AdminController(AdminService admin, EventRepository eventRepo){ this.admin = admin; this.eventRepo = eventRepo; }

    @GetMapping("/stats")
    public ResponseEntity<?> stats(Authentication auth){
        if (!isAdmin(auth)) return ResponseEntity.status(403).build();
        return ResponseEntity.ok(admin.stats());
    }

    @GetMapping("/users")
    public ResponseEntity<?> users(Authentication auth){ if (!isAdmin(auth)) return ResponseEntity.status(403).build(); return ResponseEntity.ok(admin.users()); }

    @PostMapping("/users")
    public ResponseEntity<?> save(@RequestBody User u, Authentication auth){ if (!isAdmin(auth)) return ResponseEntity.status(403).build(); return ResponseEntity.ok(admin.saveUser(u)); }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Authentication auth){ if (!isAdmin(auth)) return ResponseEntity.status(403).build(); admin.deleteUser(id); return ResponseEntity.noContent().build(); }

    @GetMapping("/events")
    public ResponseEntity<?> allEvents(Authentication auth){ if (!isAdmin(auth)) return ResponseEntity.status(403).build(); List<Event> list = eventRepo.findAll(); return ResponseEntity.ok(list); }

    @DeleteMapping("/events/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable String id, Authentication auth){ if (!isAdmin(auth)) return ResponseEntity.status(403).build(); eventRepo.deleteById(id); return ResponseEntity.noContent().build(); }

    private boolean isAdmin(Authentication auth){
        if (auth == null) return false;
        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        return roles.contains("ROLE_ADMIN");
    }
}
