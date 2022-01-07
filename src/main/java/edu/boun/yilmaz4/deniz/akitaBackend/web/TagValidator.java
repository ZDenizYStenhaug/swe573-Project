package edu.boun.yilmaz4.deniz.akitaBackend.web;


import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class TagValidator implements Validator {
    @Autowired
    private TagService tagService;

    public boolean supports(Class<?> aClass) {
        return Offer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Tag tag = (Tag) o;
        // check if the tag name is unique
        if (tagService.findByName(tag.getName()) != null) {
            errors.rejectValue("name", "Duplicate.tag.name");
        }
    }
}
