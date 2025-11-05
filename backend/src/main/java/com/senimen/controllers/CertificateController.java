package com.senimen.controllers;

import com.senimen.models.Certificate;
import com.senimen.repositories.CertificateRepository;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/certificates")
public class CertificateController {
    private final CertificateRepository repo;
    public CertificateController(CertificateRepository repo){ this.repo = repo; }

    @GetMapping("/my")
    public ResponseEntity<?> myCertificates(Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        String userId = (String) auth.getPrincipal();
        List<Certificate> list = repo.findByVolunteerId(userId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<?> download(@PathVariable String id, Authentication auth){
        if (auth == null) return ResponseEntity.status(401).build();
        Certificate c = repo.findById(id).orElseThrow();
        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        String userId = (String) auth.getPrincipal();
        if (!(roles.contains("ROLE_ADMIN") || userId.equals(c.getVolunteerId()))) return ResponseEntity.status(403).build();
        File f = new File(c.getPdfPath());
        if(!f.exists()) return ResponseEntity.notFound().build();
        FileSystemResource fs = new FileSystemResource(f);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+f.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(fs);
    }
}
