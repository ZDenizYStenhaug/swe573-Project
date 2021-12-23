package edu.boun.yilmaz4.deniz.akitaBackend.web;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class OfferValidator implements Validator {

    @Autowired
    private OfferService offerService;
    @Autowired
    private MemberServiceImpl memberService;

    public boolean supports(Class<?> aClass) {
        return Offer.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Offer offer = (Offer) o;
        Member member = memberService.findByUsername(memberService.getCurrentUserLogin());
        // username must be filled
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty");
        // description must be filled
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "NotEmpty");
        // date must be filled
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "date", "NotEmpty");
        // check if the member has any other offer for that date and time
        // TODO: check for events too
        if (offerService.checkForUniqueTimestamp(member, offer)){
            errors.rejectValue("date", "Duplicate.offerForm.date");
        }
        // the offer must have at least 2 tags
        if (offer.getOfferTags().size() < 2) {
            errors.rejectValue("offerTags", "Number.offerForm.tags");
        }
        // maximum number of participants must be at least 1
        if (offer.getMaxNumOfParticipants() < 1) {
            errors.rejectValue("maxNumOfParticipants", "Number.offerForm.maxNumOfParticipants");
        }
    }

}
