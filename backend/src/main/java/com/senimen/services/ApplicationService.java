package com.senimen.services;

import com.senimen.models.Application;
import com.senimen.models.Certificate;
import com.senimen.models.Event;
import com.senimen.repositories.ApplicationRepository;
import com.senimen.repositories.CertificateRepository;
import com.senimen.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository appRepo;
    private final EventRepository eventRepo;
    private final CertificateService certificateService;

    public ApplicationService(ApplicationRepository appRepo, EventRepository eventRepo, CertificateService certificateService) {
        this.appRepo = appRepo; this.eventRepo = eventRepo; this.certificateService = certificateService;
    }

    public Application apply(String volunteerId, String eventId){
        Application a = new Application();
        a.setVolunteerId(volunteerId); a.setEventId(eventId); a.setStatus("pending");
        return appRepo.save(a);
    }

    public List<Application> byEvent(String eventId){ return appRepo.findByEventId(eventId);}    

    public Application updateStatus(String id, String status){
        Application a = appRepo.findById(id).orElseThrow();
        a.setStatus(status);
        a = appRepo.save(a);
        if ("accepted".equalsIgnoreCase(status)){
            Event e = eventRepo.findById(a.getEventId()).orElse(null);
            if(e!=null && !e.getVolunteers().contains(a.getVolunteerId())){ e.getVolunteers().add(a.getVolunteerId()); eventRepo.save(e);}            
        }
        if ("completed".equalsIgnoreCase(status)){
            certificateService.generateFor(a.getEventId(), a.getVolunteerId());
        }
        return a;
    }
}
