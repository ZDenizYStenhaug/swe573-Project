package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private EventRepo eventRepo;

    @Transactional
    public Event addEvent(Event event) {
        event.setStatus(EventStatus.getDefault());
        logger.info("Saving event " + event);
        return eventRepo.save(event);
    }

    @Transactional (readOnly = true)
    public List<Event> allEvents() {
        logger.info("Getting all events");
        return eventRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Event findEventById(Long id) {
        logger.info("Getting the event by id " + id);
        return eventRepo.findEventById(id);
    }

    @Transactional (readOnly = true)
    public boolean checkForUniqueTimestamp(Member member, Event event) {
        logger.info("checking if there's another event or offer that has the same date and time as the new event.");
        Set<Offer> allOffers = member.getOffers();
        for (Offer o : allOffers) {
            if (o.getDate().equals(event.getDate())) {
                logger.debug("Found another offer that has the same date and time.");
                return true;
            }
        }
        Set<Event> allEvents = member.getEvents();
        for (Event e : allEvents) {
            if (e.getDate().equals(event.getDate())) {
                logger.debug("Found another event that has the same date and time.");
                return true;
            }
        }
        return false;
    }
}
