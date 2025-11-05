package com.senimen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document("users")
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String role; // volunteer, organizer, admin
    private List<String> interests = new ArrayList<>();
    private List<String> joinedEvents = new ArrayList<>();
    private Stats stats = new Stats();
    private OrganizationProfile organizationProfile;

    public static class Stats {
        private int hours;
        private int eventsCount;
        public int getHours() { return hours; }
        public void setHours(int hours) { this.hours = hours; }
        public int getEventsCount() { return eventsCount; }
        public void setEventsCount(int eventsCount) { this.eventsCount = eventsCount; }
    }

    public static class OrganizationProfile {
        private String name;
        private String description;
        private String contacts;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getContacts() { return contacts; }
        public void setContacts(String contacts) { this.contacts = contacts; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public List<String> getInterests() { return interests; }
    public void setInterests(List<String> interests) { this.interests = interests; }
    public List<String> getJoinedEvents() { return joinedEvents; }
    public void setJoinedEvents(List<String> joinedEvents) { this.joinedEvents = joinedEvents; }
    public Stats getStats() { return stats; }
    public void setStats(Stats stats) { this.stats = stats; }
    public OrganizationProfile getOrganizationProfile() { return organizationProfile; }
    public void setOrganizationProfile(OrganizationProfile organizationProfile) { this.organizationProfile = organizationProfile; }
}
