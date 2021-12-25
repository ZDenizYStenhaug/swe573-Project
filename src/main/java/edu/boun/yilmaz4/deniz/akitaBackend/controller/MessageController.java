package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Routing;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(Routing.ROOT_MESSAGE)
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private MemberServiceImpl memberService;

    @GetMapping(Routing.URI_ALL)
    public String getAllMessages(Model model) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUser")) {
            return "login";
        }
        Member member = memberService.findByUsername(username);
        model.addAttribute("unreadMessages", messageService.getAllUnreadMessages(member));
        model.addAttribute("readMessages", messageService.getAllReadMessages(member));
        model.addAttribute("messageCount", String.valueOf(messageService.checkForUnreadMessage(member)));
        return "messages";
    }

    @PostMapping(Routing.URI_MESSAGE_READ)
    public String markMessageAsRead(Model model,
                                    @RequestParam(value = "messageId") Long messageId) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUser")) {
            return "login";
        }
        messageService.markAsRead(messageId);
        Member member = memberService.findByUsername(username);
        model.addAttribute("unreadMessages", messageService.getAllUnreadMessages(member));
        model.addAttribute("readMessages", messageService.getAllReadMessages(member));
        return "messages";
    }

    @PostMapping(Routing.URI_MESSAGE_UNREAD)
    public String markMessageAsUnread(Model model,
                                    @RequestParam(value = "messageId") Long messageId) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUser")) {
            return "login";
        }
        messageService.markAsUnread(messageId);
        Member member = memberService.findByUsername(username);
        model.addAttribute("unreadMessages", messageService.getAllUnreadMessages(member));
        model.addAttribute("readMessages", messageService.getAllReadMessages(member));
        return "messages";
    }

    @PostMapping(Routing.URI_DELETE)
    public String deleteMessage(Model model,
                                @RequestParam(value = "messageId") Long messageId) {
        String username = memberService.getCurrentUserLogin();
        if(username.equals("anonymousUser")) {
            return "login";
        }
        messageService.delete(messageId);
        Member member = memberService.findByUsername(username);
        model.addAttribute("unreadMessages", messageService.getAllUnreadMessages(member));
        model.addAttribute("readMessages", messageService.getAllReadMessages(member));
        return "messages";
    }
}
