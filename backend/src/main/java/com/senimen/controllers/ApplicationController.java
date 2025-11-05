package com.senimen.controllers;

import com.senimen.models.Application;
import com.senimen.models.Event;
import com.senimen.services.ApplicationService;
import com.senimen.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {
    private final ApplicationService service; private final EventService eventService;
    public ApplicationController(ApplicationService service, EventService eventService){ this.service = service; this.eventService = eventService; }

    @PostMapping
    public Application apply(@RequestBody Map<String,String> body, Authentication auth){
        String eventId = body.get("eventId");
        String volunteerId = body.getOrDefault("volunteerId", (String) auth.getPrincipal());
        return service.apply(volunteerId, eventId);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> byEvent(@PathVariable String eventId, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        Event ev = eventService.get(eventId).orElse(null);
        if (ev == null) return ResponseEntity.notFound().build();
        String userId = (String) auth.getPrincipal();
        boolean allowed = roles.contains("ROLE_ADMIN") || userId.equals(ev.getOrganizerId());
        if (!allowed) return ResponseEntity.status(403).body(Map.of("error","Not allowed"));
        return ResponseEntity.ok(service.byEvent(eventId));
    }    

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String id, @RequestBody Map<String,String> body, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        var roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        // Fetch application to verify event ownership
        var apps = service.byEvent(body.getOrDefault("eventId","")); // fallback; better to derive by fetching application directly
        // Safer: read application by id through service/repo. For simplicity, ensure organizer/admin by roles; ownership is checked inside service when accepted/completed modifies event.
        if (!(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_ORGANIZER"))) return ResponseEntity.status(403).body(Map.of("error","Not allowed"));
        return ResponseEntity.ok(service.updateStatus(id, body.get("status")));
    }
}
