package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Tag;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Badge;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Role;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepo memberRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public Optional<Member> findMemberById(Long id) {
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
        return memberRepo.save(member);
    }

    @Override
    public Member findByUsername(String username) throws UsernameNotFoundException {
        return memberRepo.findByUsername(username);
    }

    public String getCurrentUserLogin() {
        org.springframework.security.core.context.SecurityContext securityContext = SecurityContextHolder.getContext();
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
}
