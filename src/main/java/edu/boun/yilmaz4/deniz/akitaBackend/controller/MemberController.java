package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.config.FileUploadUtil;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberDetailsServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.SecurityService;
import edu.boun.yilmaz4.deniz.akitaBackend.web.MemberValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class MemberController {

    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private SecurityService securityService;
    @Autowired
    private MemberValidator memberValidator;
    @Autowired
    private MemberDetailsServiceImpl memberDetailsService;

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
    public String registration(@ModelAttribute("member") Member member,
                               @RequestParam("image") MultipartFile multipartFile, BindingResult bindingResult) throws IOException {
        memberValidator.validate(member, bindingResult);
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        member.setPhoto(fileName);
        Member savedMember = memberService.register(member);
        securityService.autoLogin(member.getUsername(), member.getPasswordConfirm());
        String uploadDir = "user-photos/" + savedMember.getId();
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return "redirect:/welcome";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/welcome";
        }
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");
        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/welcome";
    }

    @GetMapping("/profile")
    public String showProfilePage(Model model) {
        String username = memberService.getCurrentUserLogin();
        if (username.equals("anonymousUser")) {
            return "login";
        }
        Member member = memberService.findByUsername(username);
        model.addAttribute("member", member);
        return "profile-page";
    }
}
