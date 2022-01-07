package edu.boun.yilmaz4.deniz.akitaBackend.model;

import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@DiscriminatorColumn(name = "offer_type")
public class Offer implements Serializable {

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
    private Member offerer;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private int duration;

    private int maxNumOfParticipants;

    @Column(nullable = false)
    private int cancellationDeadline;

    @Enumerated(EnumType.STRING)
    private RepeatingType repeatingType;

    @OneToMany(mappedBy = "parentOffer")
    private Set<RecurringOffer> recurringOffers = new HashSet<>();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    private int endOfferRequests;

    private String photo;

    @ManyToMany
    @JoinTable(name = "offer_tags",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> offerTags;

    @ManyToMany
    @JoinTable(name = "offer_applicants",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns =  @JoinColumn(name = "member_id"))
    private List<Member> applicants;

    @ManyToMany
    @JoinTable(name = "offer_participants",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id"))
    private List<Member> participants;

    public Offer() {

    }

    public Offer(Offer offerToCopy) {
        this.name = offerToCopy.getName();
        this.description = offerToCopy.getDescription();
        this.offerer = offerToCopy.getOfferer();
        this.date = offerToCopy.getDate();
        this.duration = offerToCopy.getDuration();
        this.maxNumOfParticipants = offerToCopy.getMaxNumOfParticipants();
        this.cancellationDeadline = offerToCopy.getCancellationDeadline();
        this.repeatingType = offerToCopy.getRepeatingType();
        this.recurringOffers = offerToCopy.getRecurringOffers();
        this.status = offerToCopy.getStatus();
        this.offerTags =offerToCopy.getOfferTags();
        this.applicants = offerToCopy.getApplicants();
        this.participants = offerToCopy.getParticipants();
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

    public Member getOfferer() {
        return offerer;
    }

    public void setOfferer(Member offererId) {
        this.offerer = offererId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime time) {
        this.date = time;
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

    public Set<RecurringOffer> getRecurringOffers() {
        return recurringOffers;
    }

    public void setRecurringOffers(Set<RecurringOffer> recurringOffers) {
        this.recurringOffers = recurringOffers;
    }

    @Transient
    public String getPhotosImagePath() {
        if (photo == null || id == null) return null;

        return "/offer-photos/" + id + "/" + photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<Tag> getOfferTags() {
        return offerTags;
    }

    public void setOfferTags(Set<Tag> offerTags) {
        this.offerTags = offerTags;
    }

    public List<Member> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Member> applicants) {
        this.applicants = applicants;
    }

    public List<Member> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Member> participants) {
        this.participants = participants;
    }

    public int getEndOfferRequests() {
        return endOfferRequests;
    }

    public void setEndOfferRequests(int endOfferRequests) {
        this.endOfferRequests = endOfferRequests;
    }
}