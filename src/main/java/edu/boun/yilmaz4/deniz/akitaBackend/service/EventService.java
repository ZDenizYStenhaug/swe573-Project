package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.web.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private EventRepo<Event> eventRepo;
    @Autowired
    private EventRepo<RecurringEvent> recurringEventRepo;

    @Transactional
    public Event addEvent(Event event) {
        logger.info("Saving event " + event);
        event.setStatus(EventStatus.getDefault());
        eventRepo.save(event);
        LocalDateTime date = event.getDate();
        date = getNextEventDate(event.getRepeatingType(), date);
        // check the repeating type. if the type is not notRepeating, save the following 4 occurences as recurring events.
        if (!event.getRepeatingType().equals(RepeatingType.NOT_REPEATING)){
            for (int i = 0; i < 4; i++){
                RecurringEvent recurringEvent = new RecurringEvent();
                recurringEvent.setDate(date);
                saveRecurringEvent(recurringEvent, event);
                eventRepo.save(recurringEvent);
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
        //the offers that the member has
        Set<Offer> allOffers = member.getOffers();
        for (Offer o : allOffers) {
            if (o.getDate().equals(event.getDate())) {
                logger.debug("Found another offer that has the same date and time.");
                return true;
            }
        }
        // the events that the member has
        Set<Event> allEvents = member.getEvents();
        for (Event e : allEvents) {
            if (e.getDate().equals(event.getDate())) {
                logger.debug("Found another event that has the same date and time.");
                return true;
            }
        }
        //TODO: the offers that the member has applied to
        //
        // the events that the member is attending
        Set<Event> eventsAttending = member.getRegisteredEvents();
        for (Event e : eventsAttending) {
            if (e.getDate().equals(event.getDate())) {
                logger.debug("Member of id " + member.getId() + " is attending another event of id " + e.getId());
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
        List<RecurringEvent> recurringEvents = getRecurringEventsOfEvent(event);
        List<LocalDateTime> dates = new ArrayList<>();
        for (RecurringEvent re : recurringEvents) {
            dates.add(re.getDate());
        }
        return dates;
    }

    @Transactional (readOnly = true)
    public List<RecurringEvent> getRecurringEventsOfEvent(Event event) {
        return recurringEventRepo.getRecurringEventsOfEvent(event);
    }

    @Transactional (readOnly = true)
    public RecurringEvent getRecurringEventByDate(LocalDateTime date, Event event) {
        return eventRepo.getRecurringEventByDate(date, event);
    }

    @Transactional
    public Event register(Event event, Member member) {
        Set<Member> participants = event.getParticipants();
        participants.add(member);
        event.setParticipants(participants);
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
        recurringEvent.setPhoto(event.getPhoto());
        recurringEvent.setEventTags(event.getEventTags());
        recurringEvent.setParentEvent(event);
        eventRepo.save(recurringEvent);
    }

    // TODO: write tests
    private LocalDateTime getNextEventDate(RepeatingType type, LocalDateTime date){
        switch (type) {
            case DAILY:
                return date.plusDays(1);
            case WEEKLY:
                return date.plusWeeks(1);
        }
        return date;
    }

}
