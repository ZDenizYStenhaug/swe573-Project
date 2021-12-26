package edu.boun.yilmaz4.deniz.akitaBackend.model;

import java.time.LocalDateTime;

public class EventRegistrationResponse {

    private Long eventId;

    private String username;

    private LocalDateTime date;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
