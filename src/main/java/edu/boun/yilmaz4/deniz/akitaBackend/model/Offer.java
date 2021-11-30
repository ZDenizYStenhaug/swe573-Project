package edu.boun.yilmaz4.deniz.akitaBackend.model;

import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
    @Column(nullable = false, updatable = false)
    private String name;
    @Column(nullable = false, updatable = false)
    private String description;
    @Column(nullable = false, updatable = false)
    private Long offererId;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false)
    private Date time;
    private Point location;
    @Column(nullable = false, updatable = false)
    private int duration;
    private int maxNumOfParticipants;
    @Column(nullable = false, updatable = false)
    private int cancellationDeadline;
    @Column(nullable = false, updatable = false)
    private String status;

    public Offer(String name, String description, Long offererId, Date time, Point location, int duration, int maxNumOfParticipants, int cancellationDeadline, String status) {
        this.name = name;
        this.description = description;
        this.offererId = offererId;
        this.time = time;
        this.location = location;
        this.duration = duration;
        this.maxNumOfParticipants = maxNumOfParticipants;
        this.cancellationDeadline = cancellationDeadline;
        this.status = status;
    }

    public Offer() {
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

    public Long getOffererId() {
        return offererId;
    }

    public void setOffererId(Long offererId) {
        this.offererId = offererId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getMaxNumOfParticipants() {
        return maxNumOfParticipants;
    }

    public void setMaxNumOfParticipants(int maxNumOfParticipants) {
        this.maxNumOfParticipants = maxNumOfParticipants;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
