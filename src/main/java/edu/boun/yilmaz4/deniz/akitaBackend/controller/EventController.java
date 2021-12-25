package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.config.FileUploadUtil;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.service.EventService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MessageService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
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
    private MessageService messageService;

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
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        return "view-event";
    }


}
