package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
@RequestMapping(Routing.ROOT_FEEDBACK)
public class FeedbackController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private EventService eventService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private MessageService messageService;

    @PostMapping(Routing.URI_ADD_OFFER_FEEDBACK)
    public String addOfferFeedback(Model model,
                                   @ModelAttribute("offerFeedback") OfferFeedback offerFeedback,
                                   @RequestParam("offerId") Long offerId) {
        Offer offer = offerService.findOfferById(offerId);
        offerFeedback.setOffer(offer);
        offerFeedback.setDate(LocalDateTime.now());
        offerFeedback.setReceiver(offer.getOfferer());
        Member member = memberService.findByUsername(memberService.getCurrentUserLogin());
        offerFeedback.setGiver(member);
        feedbackService.saveOfferFeedback(offerFeedback);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "welcome";
    }

    @PostMapping(Routing.URI_ADD_EVENT_FEEDBACK)
    public String addEventFeedback(Model model,
                                   @ModelAttribute("eventFeedback") EventFeedback eventFeedback,
                                   @RequestParam("eventId") Long eventId) {
        Event event = eventService.findEventById(eventId);
        eventFeedback.setEvent(event);
        eventFeedback.setDate(LocalDateTime.now());
        eventFeedback.setReceiver(event.getOrganizer());
        Member member = memberService.findByUsername(memberService.getCurrentUserLogin());
        eventFeedback.setGiver(member);
        feedbackService.saveEventFeedback(eventFeedback);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "welcome";
    }

}
