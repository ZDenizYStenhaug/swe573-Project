package edu.boun.yilmaz4.deniz.akitaBackend.model;

import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,  updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member organizer;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private int duration;

    @Enumerated(EnumType.STRING)
    private RepeatingType repeatingType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private String photo;

    @ManyToMany
    @JoinTable(name = "event_tags",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> eventTags;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getOrganizer() {
        return organizer;
    }

    public void setOrganizer(Member organizer) {
        this.organizer = organizer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public RepeatingType getRepeatingType() {
        return repeatingType;
    }

    public void setRepeatingType(RepeatingType repeatingType) {
        this.repeatingType = repeatingType;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public Set<Tag> getEventTags() {
        return eventTags;
    }

    public void setEventTags(Set<Tag> eventTags) {
        this.eventTags = eventTags;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/event-photos/" + id + "/" + photo;
    }

}
