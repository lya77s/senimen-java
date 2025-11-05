package com.senimen.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document("events")
public class Event {
    @Id
    private String id;
    private String title;
    private String description;
    private Instant date; // ISO
    private Location location = new Location();
    private List<String> tags = new ArrayList<>();
    private String organizerId;
    private int capacity;
    private List<String> volunteers = new ArrayList<>();
    private String status = "active"; // active, completed

    public static class Location {
        private String city;
        private String address;
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public String getOrganizerId() { return organizerId; }
    public void setOrganizerId(String organizerId) { this.organizerId = organizerId; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public List<String> getVolunteers() { return volunteers; }
    public void setVolunteers(List<String> volunteers) { this.volunteers = volunteers; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
