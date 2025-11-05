package com.senimen.services;

import com.senimen.models.Application;
import com.senimen.models.Event;
import com.senimen.models.User;
import com.senimen.repositories.ApplicationRepository;
import com.senimen.repositories.EventRepository;
import com.senimen.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminService {
    private final UserRepository userRepo;
    private final EventRepository eventRepo;
    private final ApplicationRepository appRepo;

    public AdminService(UserRepository userRepo, EventRepository eventRepo, ApplicationRepository appRepo) {
        this.userRepo = userRepo; this.eventRepo = eventRepo; this.appRepo = appRepo;
    }

    public Map<String,Object> stats(){
        Map<String,Object> s = new HashMap<>();
        List<User> users = userRepo.findAll();
        List<Event> events = eventRepo.findAll();
        List<Application> apps = appRepo.findAll();
        s.put("totalUsers", users.size());
        s.put("volunteersCount", users.stream().filter(u->"volunteer".equalsIgnoreCase(u.getRole())).count());
        s.put("totalEvents", events.size());
        s.put("totalApplications", apps.size());
        s.put("topCities", events.stream().collect(Collectors.groupingBy(e-> e.getLocation()!=null? e.getLocation().getCity():"", Collectors.counting())));
        return s;
    }

    public List<User> users(){ return userRepo.findAll(); }
    public User saveUser(User u){ return userRepo.save(u);}    
    public void deleteUser(String id){ userRepo.deleteById(id);}    
}
