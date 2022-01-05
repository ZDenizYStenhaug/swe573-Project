package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.config.FileUploadUtil;
import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MessageService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import edu.boun.yilmaz4.deniz.akitaBackend.web.OfferApplicationValidator;
import edu.boun.yilmaz4.deniz.akitaBackend.web.OfferValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping(Routing.ROOT_OFFER)
public class OfferController{
    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private OfferService offerService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private TagService tagService;
    @Autowired
    private OfferValidator offerValidator;
    @Autowired
    private OfferApplicationValidator applicationValidator;
    @Autowired
    private MessageService messageService;

    @GetMapping(Routing.URI_ADD)
    public String addOffer(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        model.addAttribute("offer", new Offer());
        model.addAttribute("tags", tagService.getAllTags());
        Member member = memberService.findByUsername(username);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "add-offer-form";
    }

    @PostMapping(Routing.URI_ADD)
    public String addOffer(@ModelAttribute("offer") Offer offer,
                           @RequestParam("image") MultipartFile multipartFile,
                           BindingResult bindingResult,
                           Model model) throws IOException {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        offerValidator.validate(offer, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add-offer-form";
        }
        Member member = memberService.findByUsername(username);
        offer.setOfferer(member);
        String fileName = "image";
        offer.setPhoto(fileName);
        Offer savedOffer = offerService.addOffer(offer);
        String uploadDir = "offer-photos/" + savedOffer.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        model.addAttribute("offer", savedOffer);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "add-offer-success";
    }

    @GetMapping(Routing.URI_ALL)
    public String getAllOffers(Model model) {
        logger.info("-> {}", "getAllOffers");
        String username = memberService.getCurrentUserLogin();
        model.addAttribute("allOffers", offerService.allOffers());
        model.addAttribute("tags", tagService.getAllTags());
        if(!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        return "offers";
    }

    @PostMapping(Routing.URI_VIEW)
    public String viewOffer(Model model,
                            @RequestParam("offerId") Long offerId) {
        logger.info("-> {}", "viewOffer");
        Offer offer = offerService.findOfferById(offerId);
        if (!offer.getRepeatingType().equals(RepeatingType.NOT_REPEATING)) {
            List<LocalDateTime> dates = offerService.getDatesOfRecurringOffers(offer);
            offer = offerService.getTheFollowingOffer(dates, offer);
        }

        //
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        OfferApplicatonResponse response = new OfferApplicatonResponse();
        model.addAttribute("offer", offer);
        model.addAttribute("response", response);
        model.addAttribute("offerManagementResponse", new OfferManagementResponse());
        model.addAttribute("dates", offerService.getDatesForOpenToApplicationOffers(offer));
        return "view-offer";
    }

    @PostMapping(Routing.URI_MANAGE)
    public String manageOffer(Model model,
                              @ModelAttribute("offerManagementResponse") OfferManagementResponse offerManagementResponse) {
        logger.info("-> {}", "manageOffer");
        String username = memberService.getCurrentUserLogin();
        Offer parentOffer = offerService.findOfferById(offerManagementResponse.getParentOfferId());
        Offer selectedOffer;
        if (parentOffer.getDate().equals(offerManagementResponse.getSelectedDate()) || offerManagementResponse.getSelectedDate() == null) {
            selectedOffer = new Offer(parentOffer);
        } else {
            selectedOffer = offerService.getRecurringOfferByDate(offerManagementResponse.getSelectedDate(), parentOffer);
        }
        model.addAttribute("offer", parentOffer);
        model.addAttribute("selectedOffer", selectedOffer);
        model.addAttribute("dates", offerService.getDatesOfRecurringOffers(parentOffer));
        model.addAttribute("isCancellationDatePassed", selectedOffer.getDate().plusDays(selectedOffer.getCancellationDeadline()).isAfter(LocalDateTime.now()));
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(memberService.findByUsername(username))));
        return "manage-offer";
    }

    @PostMapping(Routing.URI_OFFER_APPLY)
    public String apply(Model model,
                        @RequestParam("offerId") Long offerId,
                        @ModelAttribute("response") OfferApplicatonResponse response,
                        BindingResult bindingResult) {
        logger.info("-> {}", "apply");
        response.setUsername(memberService.getCurrentUserLogin());
        response.setOfferId(offerId);
        if (response.getUsername().equals("anonymousUser")) {
            return "login";
        }
        Member member = memberService.findByUsername(response.getUsername());
        Offer parent = offerService.findOfferById(response.getOfferId());
        Offer appliedOffer = offerService.getRecurringOfferByDate(response.getDate(), parent);
        applicationValidator.validate(appliedOffer, bindingResult);
        if(bindingResult.hasErrors()) {
            return "offer-application-unsuccessful";
        }
        appliedOffer = offerService.apply(appliedOffer, member);
        model.addAttribute("applied_offer", appliedOffer);
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "offer-application-successful";
    }

    @PostMapping(Routing.URI_OFFER_ACCEPT_APPLICATION)
    public String acceptApplication(Model model,
                             @ModelAttribute("offerManagementResponse") OfferManagementResponse offerManagementResponse) {
        Offer parentOffer = offerService.findOfferById(offerManagementResponse.getParentOfferId());
        Offer selectedOffer = offerService.findOfferById(offerManagementResponse.getSelectedOfferId());
        selectedOffer = offerService.acceptApplication(selectedOffer, offerManagementResponse.getApplicantMemberId());
        String username = memberService.getCurrentUserLogin();
        model.addAttribute("offer", parentOffer);
        model.addAttribute("selectedOffer", selectedOffer);
        model.addAttribute("dates", offerService.getDatesOfRecurringOffers(parentOffer));
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(memberService.findByUsername(username))));
        return "manage-offer";
    }


}
