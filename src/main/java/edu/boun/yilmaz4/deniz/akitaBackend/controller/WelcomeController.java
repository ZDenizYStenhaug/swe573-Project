package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.model.SearchResponse;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MessageService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TagService tagService;

    @GetMapping()
    public String welcome(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        SearchResponse searchResponse = new SearchResponse();
        model.addAttribute("searchResponse", searchResponse);
        model.addAttribute("allOffers", offerService.allOffers(searchResponse));
        model.addAttribute("tags", tagService.getAllTags());
        return "offers";
    }

    @GetMapping(Routing.URI_WELCOME)
    public String welcome2(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        SearchResponse searchResponse = new SearchResponse();
        model.addAttribute("searchResponse", searchResponse);
        model.addAttribute("allOffers", offerService.allOffers(searchResponse));
        model.addAttribute("tags", tagService.getAllTags());
        return "offers";
    }
}
