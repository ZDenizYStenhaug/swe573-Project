package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MessageRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.service.EventService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
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

    @Test
    void addEventTest() throws IOException {
        Member member = createMember();
        Event event = new Event();
        event.setName("dog meetup");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        Event registeredEvent = eventService.addEvent(event);
        Assertions.assertNotNull(registeredEvent);
        //delete
        eventRepo.delete(registeredEvent);
        Assertions.assertNull(eventRepo.findEventById(registeredEvent.getId()));
        deleteMember(member);
    }

    @Test
    void endEventTest() throws IOException {
        Member member = createMember();
        Event event = createEvent();
        eventService.endEvent(event);
        event = eventService.findEventById(event.getId());
        Assertions.assertEquals(EventStatus.PAST_EVENT, event.getStatus());
        deleteEvent(event);
        deleteMember(member);

    }

    Event createEvent() {
        Event event = new Event();
        event.setName("dog meetup");
        event.setDescription("lets meet with our dogs");
        event.setDuration(2);
        event.setRepeatingType(RepeatingType.NOT_REPEATING);
        event.setDate(LocalDateTime.now());
        return eventService.addEvent(event);
    }

    void deleteEvent(Event event) {
        eventRepo.delete(event);
    }


    Member createMember() throws IOException {
        Member member = new Member();
        member.setUsername("lemon");
        member.setDescription("i like lemons");
        member.setEmail("xyz@gmail.com");
        member.setPassword("lemon");
        return memberService.register(member);
    }

    void deleteMember(Member member) {
        List<Message> messages = messageRepo.getAllForMember(member);
        for (Message m: messages) {
            messageRepo.delete(m);
        }
        memberRepo.delete(member);
    }
}
