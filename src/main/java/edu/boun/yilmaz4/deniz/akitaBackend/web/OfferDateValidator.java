package edu.boun.yilmaz4.deniz.akitaBackend.web;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class OfferDateValidator implements Validator {

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
        // check if the member has any other offer for that date and time
        if (offerService.checkForUniqueTimestamp(member, offer)) {
            errors.rejectValue("date", "Duplicate.date");
        }
    }
}
