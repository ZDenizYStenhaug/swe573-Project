package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.config.FileUploadUtil;
import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.service.*;
import edu.boun.yilmaz4.deniz.akitaBackend.web.EventDateValidator;
import edu.boun.yilmaz4.deniz.akitaBackend.web.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(Routing.ROOT_EVENT)
public class EventController {
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private EventService eventService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private TagService tagService;
    @Autowired
    private EventValidator eventValidator;
    @Autowired
    private EventDateValidator dateValidator;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OfferService offerService;

    @GetMapping(Routing.URI_ADD)
    public String addEvent(Model model) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUser")) {
            return "login";
        }
        model.addAttribute("event", new Event());
        model.addAttribute("tags", tagService.getAllTags());
        Member member = memberService.findByUsername(username);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "add-event-form";
    }

    @PostMapping(Routing.URI_ADD)
    public String addEvent(@ModelAttribute("event") Event event,
                           @RequestParam("image")MultipartFile multipartFile,
                           BindingResult bindingResult,
                           Model model) throws IOException {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        eventValidator.validate(event, bindingResult);
        if(bindingResult.hasErrors()) {
            return "add-event-form";
        }
        Member member = memberService.findByUsername(username);
        event.setOrganizer(member);
        String fileName = "image";
        event.setPhoto(fileName);
        Event savedEvent = eventService.addEvent(event);
        String uploadDir = "event-photos/" + savedEvent.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        model.addAttribute("event", savedEvent);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "add-event-success";
    }

    @PostMapping(Routing.URI_EVENT_DELETE_REGISTRATION)
    public String deleteRegistration(Model model,
                                     @RequestParam("eventId") Long eventId) {
        String username = memberService.getCurrentUserLogin();
        Member member = memberService.findByUsername(username);
        Event event = eventService.findEventById(eventId);
        eventService.deleteOfferApplication(event, member);
        List<ScheduleItem> scheduledOffers = memberService.getScheduledOffers(member);
        List<ScheduleItem> scheduledEvents =  memberService.getScheduledEvents(member);
        model.addAttribute("member", member);
        model.addAttribute("interests", memberService.getInterestNames(member));
        model.addAttribute("talents", memberService.getTalentsNames(member));
        model.addAttribute("offers", offerService.findAllOffersByMember(member));
        model.addAttribute("events", eventService.findAllEventsByMember(member));
        model.addAttribute("scheduledOffers",scheduledOffers);
        model.addAttribute("scheduledEvents", scheduledEvents);
        model.addAttribute("ongoing", memberService.getOngoingActivity(scheduledOffers, scheduledEvents));
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "profile-page";
    }

    @GetMapping(Routing.URI_ALL)
    public String getAllEvents(Model model) {
        logger.info("-> {}", "getAllEvents");
        model.addAttribute("allEvents", eventService.allEvents());
        model.addAttribute("tags", tagService.getAllTags());
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        return "events";
    }

    @PostMapping(Routing.URI_VIEW)
    public String viewEvent(Model model,
                            @RequestParam("eventId") Long eventId) {
        logger.info("-> {}", "viewEvent");
        Event event = eventService.findEventById(eventId);
        model.addAttribute("event", event);
        model.addAttribute("dates", eventService.getDatesOfRecurringEvents(event));
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        EventRegistrationResponse response = new EventRegistrationResponse();
        response.setEventId(event.getId());
        model.addAttribute("response", response);
        return "view-event";
    }

    @PostMapping(Routing.URI_EVENT_REGISTER)
    public String register(Model model,
                           @RequestParam("eventId") Long eventId,
                           @ModelAttribute("response") EventRegistrationResponse response,
                           BindingResult bindingResult){
        logger.info("-> {}", "register");
        response.setUsername(memberService.getCurrentUserLogin());
        response.setEventId(eventId);
        if (response.getUsername().equals("anonymousUser")) {
            return "login";
        }
        Member member = memberService.findByUsername(response.getUsername());
        Event event = eventService.findEventById(response.getEventId());
        Event registeredEvent;
        if (!event.getDate().equals(response.getDate())) {
            event = eventService.getRecurringEventByDate(response.getDate(), event);
        }
        dateValidator.validate(event, bindingResult);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        if(bindingResult.hasErrors()) {
            return "event-registration-unsuccessful";
        }
        registeredEvent = eventService.register(event, member);
        model.addAttribute("registered_event", registeredEvent);
        return "event-registration-successful";
    }

}
