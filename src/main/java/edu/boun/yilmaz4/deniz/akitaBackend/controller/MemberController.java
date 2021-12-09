package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.SecurityService;
import edu.boun.yilmaz4.deniz.akitaBackend.web.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private MemberValidator memberValidator;

    @GetMapping("")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/welcome")
    public String welcome2() {
        return "welcome";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("member", new Member());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("member") Member member, BindingResult bindingResult) {
        memberValidator.validate(member, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        memberService.save(member);
        securityService.autoLogin(member.getUsername(), member.getPasswordConfirm());
        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }


}
