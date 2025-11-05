package com.senimen.controllers;

import com.senimen.models.Event;
import com.senimen.services.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventService service;
    public EventController(EventService service){ this.service = service; }

    @GetMapping
    public List<Event> list(@RequestParam(required = false) String city,
                            @RequestParam(required = false) String dateFrom,
                            @RequestParam(required = false) String dateTo,
                            @RequestParam(required = false) String tags){
        Instant from = dateFrom!=null && !dateFrom.isBlank()? Instant.parse(dateFrom+"T00:00:00Z"): null;
        Instant to = dateTo!=null && !dateTo.isBlank()? Instant.parse(dateTo+"T23:59:59Z"): null;
        List<String> tagsList = (tags==null||tags.isBlank())? null: Arrays.asList(tags.split(","));
        return service.list(city, from, to, tagsList);
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Event e, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        boolean isOrganizer = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains("ROLE_ORGANIZER");
        boolean isAdmin = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()).contains("ROLE_ADMIN");
        if (!isOrganizer && !isAdmin) return ResponseEntity.status(403).body(Map.of("error","Only organizer/admin can create events"));
        String userId = (String) auth.getPrincipal();
        return ResponseEntity.ok(service.create(e, userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Event e, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        var roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        var existing = service.get(id).orElse(null);
        if(existing==null) return ResponseEntity.notFound().build();
        String userId = (String) auth.getPrincipal();
        boolean owner = userId.equals(existing.getOrganizerId());
        if (!(owner || roles.contains("ROLE_ADMIN"))) return ResponseEntity.status(403).body(Map.of("error","Not allowed"));
        return ResponseEntity.ok(service.update(id, e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        var roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        var existing = service.get(id).orElse(null);
        if(existing==null) return ResponseEntity.notFound().build();
        String userId = (String) auth.getPrincipal();
        boolean owner = userId.equals(existing.getOrganizerId());
        if (!(owner || roles.contains("ROLE_ADMIN"))) return ResponseEntity.status(403).body(Map.of("error","Not allowed"));
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
