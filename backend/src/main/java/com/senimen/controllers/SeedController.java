package com.senimen.controllers;

import com.senimen.models.Application;
import com.senimen.models.Event;
import com.senimen.models.User;
import com.senimen.repositories.ApplicationRepository;
import com.senimen.repositories.EventRepository;
import com.senimen.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/seed")
public class SeedController {
    private final UserRepository userRepo; private final EventRepository eventRepo; private final ApplicationRepository appRepo; private final PasswordEncoder encoder;
    public SeedController(UserRepository u, EventRepository e, ApplicationRepository a, PasswordEncoder enc){ this.userRepo=u; this.eventRepo=e; this.appRepo=a; this.encoder=enc; }

    @PostMapping
    public String seed(){
        userRepo.deleteAll(); eventRepo.deleteAll(); appRepo.deleteAll();
        User vol = new User(); vol.setName("Vol"); vol.setEmail("vol@example.com"); vol.setPassword(encoder.encode("pass")); vol.setRole("volunteer"); vol.setInterests(List.of("environment","education")); userRepo.save(vol);
        User org = new User(); org.setName("Org"); org.setEmail("org@example.com"); org.setPassword(encoder.encode("pass")); org.setRole("organizer"); userRepo.save(org);
        User admin = new User(); admin.setName("Admin"); admin.setEmail("admin@example.com"); admin.setPassword(encoder.encode("pass")); admin.setRole("admin"); userRepo.save(admin);
        for(int i=1;i<=5;i++){
            Event e = new Event(); e.setTitle("Event "+i); e.setDescription("Helpful event "+i); e.setDate(Instant.now().plusSeconds(86400L*i));
            Event.Location loc = new Event.Location(); loc.setCity(i%2==0?"Almaty":"Astana"); loc.setAddress("Street "+i); e.setLocation(loc);
            e.setTags(List.of(i%2==0?"environment":"education","community")); e.setOrganizerId(org.getId()); e.setCapacity(20);
            eventRepo.save(e);
        }
        var events = eventRepo.findAll();
        for(int j=0;j<5;j++){
            Application a = new Application(); a.setVolunteerId(vol.getId()); a.setEventId(events.get(j%events.size()).getId()); appRepo.save(a);
        }
        return "Seeded";
    }
}
