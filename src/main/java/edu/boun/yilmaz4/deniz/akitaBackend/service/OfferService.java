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
    private OfferRepo offerRepo;
    @Autowired
    private MessageService messageService;

    @Transactional
    public Offer addOffer(Offer offer) {
        logger.info("saving offer " + offer);
        offer.setStatus(OfferStatus.OPEN_TO_APPLICATIONS);
        // save the following 4 repeating offers if there's any
        if (!offer.getRepeatingType().equals(RepeatingType.NOT_REPEATING)) {
            LocalDateTime date = offer.getDate();
            date = getNextOfferDate(offer.getRepeatingType(), date);
            for (int i = 0; i < 4; i++) {
                RecurringOffer ro = new RecurringOffer();
                ro.setDate(date);
                saveRecurringOffer(ro, offer);
                offerRepo.save(ro);
                date = getNextOfferDate(offer.getRepeatingType(), date);
            }
        }
        return offerRepo.save(offer);
    }

    @Transactional (readOnly = true)
    public List<Offer> allOffers() {
        logger.info("getting all offers");
        return offerRepo.findAll();
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

    public List<LocalDateTime> getDatesOfRecurringOffers(Offer offer) {
        Set<RecurringOffer> recurringOffers = offer.getRecurringOffers();
        List<LocalDateTime> dates = new ArrayList<>();
        dates.add(offer.getDate());
        for (RecurringOffer ro : recurringOffers) {
            dates.add(ro.getDate());
        }
        Collections.sort(dates);
        return dates;
    }

    @Transactional(readOnly = true)
    public RecurringOffer getRecurringOfferByDate(LocalDateTime date, Offer offer) {
        return offerRepo.findRecurringOfferByDate(date, offer);
    }

    @Transactional
    public Offer apply(Offer offer, Member member) {
        Set<Member> applicants = offer.getApplicants();
        applicants.add(member);
        offer.setApplicants(applicants);
        // send message to the offerer
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = member.getUsername() + " applied to your offer " + offer.getName() + " organized on " + offer.getDate().format(formatter);
        messageService.sendMessage(offer.getOfferer(), text);
        return updateOffer(offer);
    }

    private void saveRecurringOffer(RecurringOffer ro, Offer offer) {
        ro.setName(offer.getName());
        ro.setDescription(offer.getDescription());
        ro.setStatus(OfferStatus.getDefault());
        ro.setDuration(offer.getDuration());
        ro.setMaxNumOfParticipants(offer.getMaxNumOfParticipants());
        ro.setCancellationDeadline(offer.getCancellationDeadline());
        ro.setRepeatingType(offer.getRepeatingType());
        ro.setPhoto(offer.getPhotosImagePath());
        ro.setOfferTags(offer.getOfferTags());
        offerRepo.save(ro);
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
