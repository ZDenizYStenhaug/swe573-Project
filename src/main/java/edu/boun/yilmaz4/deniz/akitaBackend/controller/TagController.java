package edu.boun.yilmaz4.deniz.akitaBackend.controller;


import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.validation.Valid;

@Controller
public class TagController implements WebMvcConfigurer {
    private static final Logger logger = LoggerFactory.getLogger(OfferController.class);

    @Autowired
    private TagService tagService;

    @GetMapping(Routing.URI_TAG_FORM)
    public String showTagForm(Model model) {
        Tag tag = new Tag();
        model.addAttribute("tag", tag);
        return "add-tag-form";
    }

    @PostMapping(Routing.URI_TAG_FORM)
    public String saveNewTag(@Valid Tag tag, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "add-tag-form";
        }
        logger.info("saving tag named " + tag.getName());
        tag = tagService.saveTag(tag);
        model.addAttribute("tag", tag);
        model.addAttribute("allTags", tagService.getAllTags());
        return "add-tag-success";
    }
}