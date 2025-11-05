package com.senimen.services;

import com.senimen.models.Event;
import com.senimen.repositories.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    private final EventRepository repo;
    public EventService(EventRepository repo){ this.repo = repo; }

    public List<Event> list(String city, Instant from, Instant to, List<String> tags){
        List<Event> all = repo.findAll();
        return all.stream()
                .filter(e -> city==null || city.isBlank() || (e.getLocation()!=null && city.equalsIgnoreCase(e.getLocation().getCity())))
                .filter(e -> from==null || (e.getDate()!=null && !e.getDate().isBefore(from)))
                .filter(e -> to==null || (e.getDate()!=null && !e.getDate().isAfter(to)))
                .filter(e -> tags==null || tags.isEmpty() || e.getTags().stream().anyMatch(tags::contains))
                .collect(Collectors.toList());
    }

    public Event create(Event e, String organizerId){ e.setOrganizerId(organizerId); return repo.save(e);}    
    public Optional<Event> get(String id){ return repo.findById(id);}    
    public void delete(String id){ repo.deleteById(id);}    
    public Event update(String id, Event in){
        return repo.findById(id).map(e->{
            e.setTitle(in.getTitle()); e.setDescription(in.getDescription()); e.setDate(in.getDate());
            e.setLocation(in.getLocation()); e.setTags(in.getTags()); e.setCapacity(in.getCapacity()); e.setStatus(in.getStatus());
            return repo.save(e);
        }).orElseThrow();
    }
}
