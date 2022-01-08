package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.*;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Badge;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.EventStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
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

import java.io.IOException;
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
    @Autowired
    private GeoLocationImpService geoLocationService;

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepo.findMemberById(id);
    }

    @Override
    @Transactional
    public Member register(Member member) throws IOException {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole(Role.getDefault());
        member.setCredit(5);
        member.setLifetimeCredits(5);
        member.setBlockedCredits(0);
        member.setReputationPoints(10);
        member.setBadge(Badge.NEWCOMER);
        // geolocation
        member.setGeolocation(geoLocationService.getGeolocation(member.getAddress()));
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
        // check offers
        for (ScheduleItem so : scheduledOffers) {
            LocalDateTime beginning = so.getOffer().getDate();
            LocalDateTime end = so.getOffer().getDate().plusHours(so.getOffer().getDuration());
            // the offer needs to be ongoing (time-wise) but also must have participants and should not be a past offer.
            if (now.isAfter(beginning) && now.isBefore(end) && so.getOffer().getParticipants().size() > 0 && !so.getOffer().getStatus().equals(OfferStatus.PAST_OFFER)) {
                ongoing.setOngoingOffer(so.getOffer());
                return ongoing;
            }
        }
        // check events.
        for (ScheduleItem se : scheduleEvents) {
            LocalDateTime beginning = se.getEvent().getDate();
            LocalDateTime end = se.getEvent().getDate().plusHours(se.getEvent().getDuration());
            // the event needs to be ongoing (time-wise) but also must have participants and should not be a past offer.
            if (now.isAfter(beginning) && now.isBefore(end) && se.getEvent().getParticipants().size() > 0 && !se.getEvent().getStatus().equals(EventStatus.PAST_EVENT)) {
                ongoing.setOngoingEvent(se.getEvent());
                return ongoing;
            }
        }
        return ongoing;
    }

    public List<ScheduleItem> getScheduledOffers(Member member) {
        TreeMap<LocalDateTime, ScheduleItem> offers = new TreeMap<>();
        LocalDateTime now = LocalDateTime.now();
        // get the offers that the member is offering
        for (Offer offer : member.getOffers()) {
            if (offer.getDate().plusHours(offer.getDuration()).isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("owner");
            offers.put(offer.getDate(), si);
        }
        // get the offer that the member has applied
        for (Offer offer : member.getAppliedOffers()) {
            if (offer.getDate().plusHours(offer.getDuration()).isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("application pending");
            offers.put(offer.getDate(), si);
        }
        // get the offers that the member is participating
        for (Offer offer : member.getParticipatingOffers()) {
            if (offer.getDate().plusHours(offer.getDuration()).isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setOffer(offer);
            si.setStatus("application accepted");
            offers.put(offer.getDate(), si);
        }
        return new ArrayList<>( offers.values() );
    }

    public List<ScheduleItem> getScheduledEvents(Member member) {
        TreeMap<LocalDateTime, ScheduleItem> events = new TreeMap<>();
        LocalDateTime now = LocalDateTime.now();
        // get the events that the member is ogranizing
        for (Event event : member.getEvents()) {
            if (event.getDate().plusHours(event.getDuration()).isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setEvent(event);
            si.setStatus("owner");
            events.put(event.getDate(), si);
        }
        // get the event that the member is attending
        for (Event event : member.getRegisteredEvents()) {
            if (event.getDate().plusHours(event.getDuration()).isBefore(now))
                continue;
            ScheduleItem si = new ScheduleItem();
            si.setEvent(event);
            si.setStatus("participating");
            events.put(event.getDate(), si);
        }
        return new ArrayList<>( events.values() );
    }

    public Member updateMember(Member member) {
        return memberRepo.save(member);
    }

}
