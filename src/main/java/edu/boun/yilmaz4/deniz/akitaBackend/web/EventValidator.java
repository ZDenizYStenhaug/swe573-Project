package edu.boun.yilmaz4.deniz.akitaBackend.web;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class EventValidator implements Validator {

    public boolean supports(Class<?> aClass) {
        return Offer.class.equals(aClass);
    }


    @Override
    public void validate(Object o, Errors errors) {
        // TODO
    }
}
