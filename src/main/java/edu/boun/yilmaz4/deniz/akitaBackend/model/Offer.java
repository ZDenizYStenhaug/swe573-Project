package edu.boun.yilmaz4.deniz.akitaBackend.model;

import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Offer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false,  updatable = false)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    private String description;

    //TODO: one to many relationship with member class
    @Column(nullable = false, updatable = false)
    private Long offererId;

    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
    @Column(nullable = false, updatable = false)
    private Date time;

    @Column(nullable = false, updatable = false)
    private int duration;

    private int maxNumOfParticipants;

    @Column(nullable = false)
    private int cancellationDeadline;


    @Enumerated(EnumType.STRING)
    private RepeatingType repeatingType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferStatus status;


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

    public OfferStatus getStatus() {
        return status;
    }

    public void setStatus(OfferStatus status) {
        this.status = status;
    }

    public RepeatingType getRepeatingType() {
        return repeatingType;
    }

    public void setRepeatingType(RepeatingType repeatingType) {
        this.repeatingType = repeatingType;
    }
}
