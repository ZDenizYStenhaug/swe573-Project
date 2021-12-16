package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.validation.Valid;
import java.sql.Date;

@Controller
@RequestMapping()
public class OfferController{

    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private TagService tagService;

    @GetMapping(Routing.URI_OFFER_ADD)
    public String showOfferForm(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        model.addAttribute("offer", new Offer());
        return "add-offer-form";
    }

    @PostMapping(Routing.URI_OFFER_ADD)
    public String addNewOffer(@Valid Offer offer, BindingResult bindingResult, Model model) {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        if(bindingResult.hasErrors()) {
            return "add-offer-form";
        }
        // TODO: move business logic to the service class
        Member member = memberService.findByUsername(username);
        offer.setOffererId(member.getId());
        offer = offerService.addOffer(offer);
        model.addAttribute("offer", offer);
        model.addAttribute("offers", offerService.allOffers());
        return "add-offer-success";
    }

    @GetMapping(Routing.URI_OFFER_ALL)
    public String getAllOffers(Model model) {
        logger.info("-> {}", "getAllOffers");
        model.addAttribute("allOffers", offerService.allOffers());
        model.addAttribute("tags", tagService.getAllTags());
        return "offers";
    }

    @GetMapping("/deneme")
    public String deneme() {
        return "deneme";
    }
}
