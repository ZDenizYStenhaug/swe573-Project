package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.OfferRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private OfferRepo<Offer> offerRepo;
    @Autowired
    private MessageService messageService;
    @Autowired
    private MemberServiceImpl memberService;

    @Transactional
    public Offer acceptApplication(Offer offer, Long memberId) {
        Member applicant = memberService.findMemberById(memberId);
        // remove member from applicants
        List<Member> applicants = offer.getApplicants();
        applicants.remove(applicant);
        offer.setApplicants(applicants);
        // save member to participants
        List<Member> participants = offer.getParticipants();
        participants.add(applicant);
        offer.setParticipants(participants);
        sendAcceptanceMessage(applicant, offer);
        // send message to other applicants that the quota for this offer is full
        if (offer.getParticipants().size() >= offer.getMaxNumOfParticipants()) {
            declineRemainingApplications(offer);
        }
        return offerRepo.save(offer);
    }

    @Transactional
    public Offer declineApplication(Offer offer, Long memberId) {
        Member applicant = memberService.findMemberById(memberId);
        // remove member from applicants
        List<Member> applicants = offer.getApplicants();
        applicants.remove(applicant);
        offer.setApplicants(applicants);
        // update member's number of refusals
        applicant.setNumOfRefusals(applicant.getNumOfRefusals() + 1);
        applicant.setBlockedCredits(applicant.getBlockedCredits() - offer.getDuration());
        memberService.updateMember(applicant);
        sendRefusalMessage(applicant, offer);
        return offerRepo.save(offer);
    }

    @Transactional
    public void declineRemainingApplications(Offer offer) {
        List<Member> applicants = offer.getApplicants();
        for(Member applicant : applicants) {
            applicant.setBlockedCredits(applicant.getBlockedCredits() - offer.getDuration());
            memberService.updateMember(applicant);
            sendQuotaMessage(applicant, offer);
        }
    }

    @Transactional
    public Offer addOffer (Offer offer) {
        logger.info("saving offer " + offer);
        offer.setStatus(OfferStatus.OPEN_TO_APPLICATIONS);
        // save the following 4 repeating offers if there's any
        if (!offer.getRepeatingType().equals(RepeatingType.NOT_REPEATING)) {
            LocalDateTime date = offer.getDate();
            for (int i = 0; i < 5; i++) {
                RecurringOffer ro = new RecurringOffer();
                ro.setDate(date);
                saveRecurringOffer(ro, offer);
                date = getNextOfferDate(offer.getRepeatingType(), date);
            }
        }
        return offerRepo.save(offer);
    }

    @Transactional (readOnly = true)
    public List<Offer> allOffers() {
        logger.info("getting all offers");
        return offerRepo.findAllOffers();
    }

    @Transactional
    public Offer apply(Offer offer, Member member) {
        // add the member to applicants
        List<Member> applicants = offer.getApplicants();
        applicants.add(member);
        offer.setApplicants(applicants);
        // block member's credits
        member.setBlockedCredits(member.getBlockedCredits() + offer.getDuration());
        memberService.updateMember(member);
        // send message to the offerer
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = member.getUsername() + " applied to your offer " + offer.getName() + " organized on " + offer.getDate().format(formatter);
        messageService.sendMessage(offer.getOfferer(), text);
        return updateOffer(offer);
    }

    @Transactional(readOnly = true)
    public List<Offer> findAllOffersByMember(Member member) {
        return offerRepo.findAllOffersByMember(member);
    }

    @Transactional (readOnly = true)
    public boolean checkForUniqueTimestamp(Member member, Offer offer) {
        logger.info("checking if there's another event or offer that has the same date and time as the new offer.");
        // check all the offers they are applied/participating/offering
        Set<Offer> offers = member.getOffers();
        offers.addAll(member.getAppliedOffers());
        offers.addAll(member.getParticipatingOffers());
        for (Offer o : offers) {
            if (o.getDate().equals(offer.getDate())) {
                logger.debug("Found another offer that has the same date and time.");
                return true;
            }
        }
        // check all the events they are participating/organizing
        Set<Event> events = member.getEvents();
        events.addAll(member.getRegisteredEvents());
        for (Event e : events) {
            if (e.getDate().equals(offer.getDate())) {
                logger.debug("Found another event that has the same date and time.");
                return true;
            }
        }
        return false;
    }

    @Transactional(readOnly = true)
    public Offer findOfferById(Long id) {
        logger.info("getting the offer by id " + id);
        return offerRepo.findOfferById(id);
    }

    // only gets the offers whose dates are after the current time
    public List<LocalDateTime> getDatesOfRecurringOffers(Offer offer) {
        Set<RecurringOffer> recurringOffers = offer.getRecurringOffers();
        List<LocalDateTime> dates = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (RecurringOffer ro : recurringOffers) {
            if(!ro.getDate().isBefore(now)){
                dates.add(ro.getDate());
            }
        }
        Collections.sort(dates);
        return dates;
    }

    public List<LocalDateTime> getDatesForOpenToApplicationOffers(Offer parent) {
        List<LocalDateTime> dates = new ArrayList<>();
        if(parent.getRepeatingType().equals(RepeatingType.NOT_REPEATING)) {
            dates.add(parent.getDate());
            return dates;
        }
        Set<RecurringOffer> recurringOffers = parent.getRecurringOffers();
        for(RecurringOffer ro : recurringOffers) {
            if(ro.getStatus().equals(OfferStatus.OPEN_TO_APPLICATIONS))
                dates.add(ro.getDate());
        }
        Collections.sort(dates);
        return dates;
    }

    @Transactional
    public Offer getTheFollowingOffer(List<LocalDateTime> dates, Offer parent) {
        LocalDateTime followingOfferDate = dates.get(0);
        parent.setDate(followingOfferDate);
        // update the parent status as well if neccessary
        if(LocalDateTime.now().plusDays(parent.getCancellationDeadline()).isAfter(parent.getDate())) {
            parent.setStatus(OfferStatus.CLOSED_TO_APPLICATIONS);
        }
        offerRepo.save(parent);
        // update the status of the other recurring offers.
        updateStatusOfRecurringOffers(parent);

        return parent;
    }

    @Transactional
    public void updateStatusOfRecurringOffers (Offer parent) {
        // set the past offers' status to PAST_OFFER
        // set the status of the offers whose cancellation date is past to CLOSED_TO_APPLICATIONS
        LocalDateTime now = LocalDateTime.now();
        Set<RecurringOffer> recurringOffers = parent.getRecurringOffers();
        for (RecurringOffer ro : recurringOffers) {
            if (ro.getDate().isBefore(now)) {
                ro.setStatus(OfferStatus.PAST_OFFER);
            } else if (now.plusDays(ro.getCancellationDeadline()).isAfter(ro.getDate())) {
                ro.setStatus(OfferStatus.CLOSED_TO_APPLICATIONS);
            }
        offerRepo.save(ro);
        }
    }

    @Transactional(readOnly = true)
    public RecurringOffer getRecurringOfferByDate(LocalDateTime date, Offer offer) {
        return offerRepo.findRecurringOfferByDate(date, offer);
    }

    private void saveRecurringOffer(RecurringOffer ro, Offer offer) {
        ro.setName(offer.getName());
        ro.setDescription(offer.getDescription());
        ro.setOfferer(offer.getOfferer());
        ro.setStatus(OfferStatus.getDefault());
        ro.setDuration(offer.getDuration());
        ro.setMaxNumOfParticipants(offer.getMaxNumOfParticipants());
        ro.setCancellationDeadline(offer.getCancellationDeadline());
        ro.setPhoto(offer.getPhotosImagePath());
        ro.setOfferTags(offer.getOfferTags());
        ro.setParentOffer(offer);
        offerRepo.save(ro);
    }

    public void sendAcceptanceMessage(Member applicant, Offer offer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = "Your application for the offer named " + offer.getName() + " on " + offer.getDate().format(formatter) + " has been accepted!";
        messageService.sendMessage(applicant, text);
    }

    public void sendQuotaMessage(Member member, Offer offer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = "Your application for the offer named " + offer.getName() + " on " + offer.getDate().format(formatter) + " has been declined for quota reasons.";
        messageService.sendMessage(member, text);
    }

    private void sendRefusalMessage(Member receiver, Offer offer) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = "Your application for the offer named " + offer.getName() + " on " + offer.getDate().format(formatter) + " has been declined.";
        messageService.sendMessage(receiver, text);
    }

    @Transactional
    public Offer updateOffer(Offer offer) {
        return offerRepo.save(offer);
    }

    public LocalDateTime getNextOfferDate(RepeatingType type, LocalDateTime date) {
        switch (type) {
            case DAILY:
                return date.plusDays(1);
            case WEEKLY:
                return date.plusWeeks(1);
            case MONTHLY:
                return date.plusMonths(1);
        }
        return date;
    }
}
