package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Event;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.service.EventService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import edu.boun.yilmaz4.deniz.akitaBackend.web.EventValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/event")
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

    @GetMapping(Routing.URI_EVENT_ADD)
    public String addEvent(Model model) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUer")) {
            return "login";
        }
        model.addAttribute("event", new Event());
        model.addAttribute("tags", tagService.getAllTags());
        return "add-event-form";
    }


}
