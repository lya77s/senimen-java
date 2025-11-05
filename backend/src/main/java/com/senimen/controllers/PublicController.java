package com.senimen.controllers;

import com.senimen.models.SponsorApplication;
import com.senimen.repositories.SponsorApplicationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class PublicController {
    private final SponsorApplicationRepository repo;
    public PublicController(SponsorApplicationRepository repo){ this.repo = repo; }

    @PostMapping("/sponsors/apply")
    public SponsorApplication apply(@RequestBody SponsorApplication s){ return repo.save(s);}    

    // Alias to satisfy requirement: /api/sponsors/apply
    @PostMapping(path = "/api/sponsors/apply")
    public SponsorApplication applyAlias(@RequestBody SponsorApplication s){ return apply(s);}    
}
