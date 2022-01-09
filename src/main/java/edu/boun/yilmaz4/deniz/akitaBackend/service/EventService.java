package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private EventRepo<Event> eventRepo;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GeoLocationService geoLocationService;


    @Transactional
    public Event addEvent(Event event) throws IOException, JSONException {
        logger.info("Saving event " + event);
        event.setStatus(EventStatus.getDefault());
        event.setGeolocation(geoLocationService.saveGeoLocation(event.getAddress()));
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
    public List<Event> allEvents(SearchResponse searchResponse) {
        logger.info("Getting all events");
        List<Event> events =  eventRepo.findAllEvents();
        Set<Tag> tags = searchResponse.getSelectedTags();
        if (tags == null) {
            return events;
        }
        List<Event> selectedEvents = new ArrayList<>();
        for (Event e : events) {
            Set<Tag> eventTags = e.getEventTags();
            for(Tag eventTag : eventTags) {
                if(tags.contains(eventTag)) {
                    selectedEvents.add(e);
                }
            }
        }
        return selectedEvents;
    }

    @Transactional(readOnly = true)
    public List<Event> allClosebyEvents(Member member) {
        logger.info("getting all closeby offers for member " + member.getUsername());
        List<Event> events = eventRepo.findAllEvents();
        List<Event> selectedEvents = new ArrayList<>();
        // check
        for (Event e : events) {
            GeoLocation memberLoc = member.getGeolocation();
            GeoLocation eventLoc = e.getGeolocation();
            double latDiff = Math.abs(memberLoc.getLatitude() - eventLoc.getLatitude());
            double lngDiff = Math.abs(memberLoc.getLongitude() - eventLoc.getLongitude());
            if(latDiff <= 0.4 && lngDiff <= 0.4) {
                selectedEvents.add(e);
            }
        }
        return selectedEvents;
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

    @Transactional
    public void deleteOfferApplication(Event event, Member member) {
        // update the offer's applicants
        Set<Member> participants = event.getParticipants();
        participants.remove(member);
        event.setParticipants(participants);
        updateEvent(event);
        // send message the offerer
        sendRegistrationDeletedMessage(event, member);
    }

    @Transactional
    public void endEvent(Event event) {
        event.setStatus(EventStatus.PAST_EVENT);
        eventRepo.save(event);
        if(event.getClass().equals(RecurringEvent.class)) {
            Event parent = ((RecurringEvent) event).getParentEvent();
            addRecurringEvent(parent);
        }
    }

    public void addRecurringEvent(Event parent) {
        LocalDateTime latestDate = getLatestDate(getDatesOfRecurringEvents(parent));
        RecurringEvent recurringEvent = new RecurringEvent();
        recurringEvent.setParentEvent(parent);
        recurringEvent.setName(parent.getName());
        recurringEvent.setDescription(parent.getDescription());
        recurringEvent.setName(parent.getName());
        recurringEvent.setDescription(parent.getDescription());
        recurringEvent.setOrganizer(parent.getOrganizer());
        recurringEvent.setStatus(EventStatus.getDefault());
        recurringEvent.setDuration(parent.getDuration());
        if (parent.getRepeatingType().equals(RepeatingType.DAILY)) {
            recurringEvent.setDate(latestDate.plusDays(1));
        } else if (parent.getRepeatingType().equals(RepeatingType.WEEKLY)) {
            recurringEvent.setDate(latestDate.plusWeeks(1));
        } else if(parent.getRepeatingType().equals(RepeatingType.MONTHLY)) {
            recurringEvent.setDate(latestDate.plusMonths(1));
        }
        eventRepo.save(recurringEvent);
    }

    public LocalDateTime getLatestDate(List<LocalDateTime> dates) {
        LocalDateTime latest = LocalDateTime.now();
        for (LocalDateTime date : dates) {
            if (date.isAfter(latest)) {
                latest = date;
            }
        }
        return latest;
    }

    @Transactional(readOnly = true)
    public List<Event> findAllEventsByMember(Member member) {
        return eventRepo.findAllEventsByMember(member);
    }

    @Transactional (readOnly = true)
    public Event findEventById(Long id) {
        logger.info("Getting the event by id " + id);
        return eventRepo.findEventById(id);
    }

    public List<LocalDateTime> getDatesOfRecurringEvents(Event event) {
        Set<RecurringEvent> recurringEvents = event.getRecurringEvents();
        List<LocalDateTime> dates = new ArrayList<>();
        dates.add(event.getDate());
        for (RecurringEvent re : recurringEvents) {
            dates.add(re.getDate());
        }
        Collections.sort(dates);
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
        // send message to the organizer of the event.
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String message = member.getUsername() + " registered to your event " + event.getName() + " organized on " + event.getDate().format(formatter);
        messageService.sendMessage(event.getOrganizer(), message);
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
    public LocalDateTime getNextEventDate(RepeatingType type, LocalDateTime date) {
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

    public void sendRegistrationDeletedMessage(Event event, Member member) {
        Member receiver = event.getOrganizer();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
        String text = member.getUsername() + " dropped out of your event named " + event.getName() + " on " + event.getDate().format(formatter) + ".";
        messageService.sendMessage(receiver, text);
    }
}
