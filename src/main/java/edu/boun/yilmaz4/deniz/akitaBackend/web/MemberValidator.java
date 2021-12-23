package edu.boun.yilmaz4.deniz.akitaBackend.web;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class MemberValidator implements Validator {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean supports(Class<?> aClass) {
        return Member.class.equals(aClass);
    }


    @Override
    public void validate(Object o, Errors errors) {
        Member member = (Member) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty");
        if (member.getUsername().length() < 6 || member.getUsername().length() > 32) {
            errors.rejectValue("username", "Size.userForm.username");
        }
        if (memberService.findByUsername(member.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty");
        if (member.getPassword().length() < 8 || member.getPassword().length() > 32) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!member.getPasswordConfirm().equals(member.getPassword())) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }

        //TODO: the user must chose at least 2 tags
    }
}
