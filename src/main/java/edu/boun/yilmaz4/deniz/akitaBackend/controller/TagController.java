package edu.boun.yilmaz4.deniz.akitaBackend.controller;


import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MessageService;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import edu.boun.yilmaz4.deniz.akitaBackend.web.TagValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
@RequestMapping(Routing.ROOT_TAG)
public class TagController implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private TagService tagService;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TagValidator tagValidator;

    @GetMapping(Routing.URI_ADD)
    public String showTagForm(Model model) {
        Tag tag = new Tag();
        model.addAttribute("tag", tag);
        return "add-tag-form";
    }

    @PostMapping(Routing.URI_ADD)
    public String saveNewTag(@Valid Tag tag, BindingResult bindingResult, Model model) {
        tagValidator.validate(tag, bindingResult);
        if (bindingResult.hasErrors()) {
            return "add-tag-form";
        }
        logger.info("saving tag named " + tag.getName());
        tag = tagService.saveTag(tag);
        model.addAttribute("tag", tag);
        model.addAttribute("allTags", tagService.getAllTags());
        String username = memberService.getCurrentUserLogin();
        if (!username.equals("anonymousUser")) {
            Member member = memberService.findByUsername(username);
            model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        }
        return "add-tag-success";
    }
}