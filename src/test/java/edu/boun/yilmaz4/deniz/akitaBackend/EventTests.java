package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.GeoLocationRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MessageRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.service.EventService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class EventTests {

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepo eventRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private GeoLocationRepo geoLocationRepo;

    @Test
    void addEventTest() throws IOException, JSONException {
        Event event = new Event();
        event.setName("dog meetup");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        event.setAddress("kadıköy");
        Event registeredEvent = eventService.addEvent(event);
        Assertions.assertNotNull(registeredEvent);
        //delete
        eventRepo.delete(registeredEvent);
        geoLocationRepo.delete(event.getGeolocation());
        Assertions.assertNull(eventRepo.findEventById(registeredEvent.getId()));
    }

    @Test
    void endEventTest() throws IOException, JSONException {
        Event event = createEvent();
        eventService.endEvent(event);
        event = eventRepo.findEventById(event.getId());
        Assertions.assertEquals(EventStatus.PAST_EVENT, event.getStatus());
        deleteEvent(event);
    }

    @Test
    void getClosebyEventsNotClose() throws JSONException, IOException {
        Member member = createMember();
        Event event = new Event();
        event.setName("random event name");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        event.setAddress("fosses la ville");
        Event savedEvent = eventService.addEvent(event);
        List<Event> selectedEvents = eventService.allClosebyEvents(member);
        Assertions.assertFalse(selectedEvents.stream().anyMatch(item -> savedEvent.getName().equals(item.getName())));
        // delete event and member
        eventRepo.delete(savedEvent);
        geoLocationRepo.delete(event.getGeolocation());
        deleteMember(member);
    }

    @Test
    void getClosebyEventsIsClose()throws JSONException, IOException {
        Member member = createMember();
        Event event = new Event();
        event.setName("random event mame");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        event.setAddress("kınalıada");
        Event savedEvent = eventService.addEvent(event);
        List<Event> selectedEvents = eventService.allClosebyEvents(member);
        Assertions.assertTrue(selectedEvents.stream().anyMatch(item -> savedEvent.getName().equals(item.getName())));
        // delete event and member
        eventRepo.delete(savedEvent);
        geoLocationRepo.delete(event.getGeolocation());
        deleteMember(member);
    }


    Event createEvent() throws IOException, JSONException {
        Event event = new Event();
        event.setName("dog meetup");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        event.setAddress("kadıköy");
        return eventService.addEvent(event);
    }

    void deleteEvent(Event event) {
        eventRepo.delete(event);
        geoLocationRepo.delete(event.getGeolocation());
    }


    Member createMember() throws IOException, JSONException {
        Member member = new Member();
        member.setUsername("lemon");
        member.setDescription("i like lemons");
        member.setEmail("xyz@gmail.com");
        member.setPassword("lemon");
        member.setAddress("kadıköy");
        return memberService.register(member);
    }

    void deleteMember(Member member) {
        List<Message> messages = messageRepo.getAllForMember(member);
        for (Message m: messages) {
            messageRepo.delete(m);
        }
        memberRepo.delete(member);
        geoLocationRepo.delete(member.getGeolocation());
    }
}
