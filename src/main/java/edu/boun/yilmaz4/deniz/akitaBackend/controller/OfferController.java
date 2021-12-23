package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.config.FileUploadUtil;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public String addOffer(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        model.addAttribute("offer", new Offer());
        model.addAttribute("tags", tagService.getAllTags());
        return "add-offer-form";
    }

    @PostMapping(Routing.URI_OFFER_ADD)
    public String addOffer(@ModelAttribute("offer") Offer offer,
                           @RequestParam("image") MultipartFile multipartFile,
                           BindingResult bindingResult,
                           Model model) throws IOException {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        if(bindingResult.hasErrors()) {
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
        return "add-offer-success";
    }

    @GetMapping(Routing.URI_OFFER_ALL)
    public String getAllOffers(Model model) {
        logger.info("-> {}", "getAllOffers");
        model.addAttribute("allOffers", offerService.allOffers());
        model.addAttribute("tags", tagService.getAllTags());
        return "offers";
    }
}
