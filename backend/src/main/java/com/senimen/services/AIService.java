package com.senimen.services;

import com.senimen.models.Event;
import com.senimen.models.User;
import com.senimen.repositories.EventRepository;
import com.senimen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AIService {
    private final UserRepository userRepo; private final EventRepository eventRepo;
    public AIService(UserRepository userRepo, EventRepository eventRepo){ this.userRepo=userRepo; this.eventRepo=eventRepo; }

    public List<Event> recommend(String userId){
        User u = userRepo.findById(userId).orElseThrow();
        Set<String> interests = u.getInterests().stream().map(String::toLowerCase).collect(Collectors.toSet());
        Instant now = Instant.now();
        return eventRepo.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .sorted(Comparator.comparing((Event e) -> score(e, interests, now)).reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    private double score(Event e, Set<String> interests, Instant now){
        double match = e.getTags()==null?0: e.getTags().stream().map(String::toLowerCase).filter(interests::contains).count();
        double dateBoost = (e.getDate()==null)?0: (e.getDate().isAfter(now)?1.0:0.5);
        return match*2 + dateBoost;
    }
}
