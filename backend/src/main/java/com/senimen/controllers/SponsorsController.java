package com.senimen.controllers;

import com.senimen.models.SponsorApplication;
import com.senimen.repositories.SponsorApplicationRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SponsorsController {
    private final SponsorApplicationRepository repo;
    public SponsorsController(SponsorApplicationRepository repo){ this.repo = repo; }

    @PostMapping("/sponsors/apply")
    public SponsorApplication apply(@RequestBody SponsorApplication s){ return repo.save(s);}    
}
