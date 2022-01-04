package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Badge;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Role;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MessageService messageService;

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepo.findMemberById(id);
    }

    @Override
    @Transactional
    public Member register(Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole(Role.getDefault());
        member.setCredit(5);
        member.setReputationPoints(5);
        member.setBadge(Badge.NEWCOMER);
        member = memberRepo.save(member);
        // send welcome message
        String text = "Welcome to Akita!";
        messageService.sendMessage(member, text);
        return member;
    }

    @Override
    public Member findByUsername(String username) throws UsernameNotFoundException {
        return memberRepo.findByUsername(username);
    }

    public String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String login = null;
        if (authentication != null)
            if (authentication.getPrincipal() instanceof UserDetails)
                login = ((UserDetails) authentication.getPrincipal()).getUsername();
            else if (authentication.getPrincipal() instanceof String)
                login = (String) authentication.getPrincipal();
        return login;
    }

    public List<String> getInterestNames(Member member) {
        List<String> interests = new ArrayList<>();
        for (Tag tag : member.getInterests()) {
            interests.add(tag.getName());
        }
        return interests;
    }

    public List<String> getTalentsNames(Member member) {
        List<String> talents = new ArrayList<>();
        for (Tag tag : member.getTalents()) {
            talents.add(tag.getName());
        }
        return talents;
    }

    public OngoingActivityResponse getOngoingActivity (List<ScheduleItem> scheduledOffers, List<ScheduleItem> scheduleEvents) {
        OngoingActivityResponse ongoing = new OngoingActivityResponse();
        LocalDateTime now = LocalDateTime.now();
        for (ScheduleItem so : scheduledOffers) {
            LocalDateTime beginning = so.getOffer().getDate();
            LocalDateTime end = so.getOffer().getDate().plusHours(so.getOffer().getDuration());
            if (now.isAfter(beginning) && now.isBefore(end)) {
                ongoing.setOngoingOffer(so.getOffer());
                return ongoing;
            }
        }
        for (ScheduleItem se : scheduleEvents) {
            LocalDateTime beginning = se.getEvent().getDate();
            LocalDateTime end = se.getEvent().getDate().plusHours(se.getEvent().getDuration());
            if (now.isAfter(beginning) && now.isBefore(end)) {
                ongoing.setOngoingEvent(se.getEvent());
                return ongoing;
            }
        }
        return ongoing;
    }

    public List<ScheduleItem> getScheduledOffers(Member member) {
        TreeMap<LocalDateTime, ScheduleItem> offers = new TreeMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (Offer offer : member.getOffers()) {
            if (offer.getDate().isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("owner");
            offers.put(offer.getDate(), si);
        }
        for (Offer offer : member.getAppliedOffers()) {
            if (offer.getDate().isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("application pending");
            offers.put(offer.getDate(), si);
        }
        for (Offer offer : member.getParticipatingOffers()) {
            if (offer.getDate().isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("application accepted");
            offers.put(offer.getDate(), si);
        }
        List<ScheduleItem> foo = new ArrayList<>( offers.values() );
        return foo;
    }

    public List<ScheduleItem> getScheduledEvents(Member member) {
        TreeMap<LocalDateTime, ScheduleItem> events = new TreeMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (Event event : member.getEvents()) {
            if (event.getDate().isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setEvent(event);
            si.setStatus("owner");
            events.put(event.getDate(), si);
        }
        for (Event event : member.getRegisteredEvents()) {
            if (event.getDate().isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setEvent(event);
            si.setStatus("participating");
            events.put(event.getDate(), si);
        }
        List<ScheduleItem> foo = new ArrayList<>( events.values() );
        return foo;
    }
}
