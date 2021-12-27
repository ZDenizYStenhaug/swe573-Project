package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private EventRepo<Event> eventRepo;

    @Transactional
    public Event addEvent(Event event) {
        logger.info("Saving event " + event);
        logger.info("Saving event " + event);
        event.setStatus(EventStatus.getDefault());
        // save the following 4 repeating events if there's any
        if (!event.getRepeatingType().equals(RepeatingType.NOT_REPEATING)) {
            LocalDateTime date = event.getDate();
            date = getNextEventDate(event.getRepeatingType(), date);
            for (int i = 0; i < 4; i++) {
                RecurringEvent recurringEvent = new RecurringEvent();
                recurringEvent.setDate(date);
                saveRecurringEvent(recurringEvent, event);
                date = getNextEventDate(event.getRepeatingType(), date);
            }
        }
        return eventRepo.save(event);
    }

    @Transactional (readOnly = true)
    public List<Event> allEvents() {
        logger.info("Getting all events");
        return eventRepo.findAllEvents();
    }

    @Transactional (readOnly = true)
    public boolean checkForUniqueTimestamp(Member member, Event event) {
        logger.info("checking if the member has or attends another event or offer that has the same date and time as the new event.");
        // check all the offers they are applied/participating/offering
        Set<Offer> offers = member.getOffers();
        offers.addAll(member.getAppliedOffers());
        offers.addAll(member.getParticipatingOffers());
        for (Offer o : offers) {
            if (o.getDate().equals(event.getDate())) {
                logger.debug("Found another offer that has the same date and time.");
                return true;
            }
        }
        // check all the events they are participating/organizing
        Set<Event> events = member.getEvents();
        events.addAll(member.getRegisteredEvents());
        for (Event e : events) {
            if (e.getDate().equals(event.getDate())) {
                logger.debug("Found another event that has the same date and time.");
                return true;
            }
        }
        return false;
    }

    @Transactional (readOnly = true)
    public Event findEventById(Long id) {
        logger.info("Getting the event by id " + id);
        return eventRepo.findEventById(id);
    }

    public List<LocalDateTime> getDatesOfRecurringEvents(Event event) {
        Set<RecurringEvent> recurringEvents = event.getRecurringEvents();
        List<LocalDateTime> dates = new ArrayList<>();
        for (RecurringEvent re : recurringEvents) {
            dates.add(re.getDate());
        }
        return dates;
    }

    @Transactional (readOnly = true)
    public RecurringEvent getRecurringEventByDate(LocalDateTime date, Event event) {
        return eventRepo.findRecurringEventByDate(date, event);
    }

    @Transactional
    public Event register(Event event, Member member) {
        Set<Member> participants = event.getParticipants();
        participants.add(member);
        event.setParticipants(participants);
        // TODO: send message to the owner of the event.
        return updateEvent(event);
    }

    @Transactional
    public Event updateEvent(Event event) {
        logger.info("saving event " + event);
        return eventRepo.save(event);
    }

    private void saveRecurringEvent(RecurringEvent recurringEvent, Event event) {
        recurringEvent.setName(event.getName());
        recurringEvent.setDescription(event.getDescription());
        recurringEvent.setStatus(EventStatus.getDefault());
        recurringEvent.setOrganizer(event.getOrganizer());
        recurringEvent.setDuration(event.getDuration());
        recurringEvent.setRepeatingType(event.getRepeatingType());
        recurringEvent.setPhoto(event.getPhotosImagePath());
        recurringEvent.setEventTags(event.getEventTags());
        recurringEvent.setParentEvent(event);
        eventRepo.save(recurringEvent);
    }

    // TODO: write tests
    public LocalDateTime getNextEventDate(RepeatingType type, LocalDateTime date){
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
