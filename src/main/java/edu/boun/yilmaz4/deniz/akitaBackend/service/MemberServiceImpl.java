package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.exception.MemberNotFoundException;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Member register(Member member) {
        return memberRepo.save(member);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findMemberById(Long id) {
        return memberRepo.findMemberById(id);
    }

    @Override
    public Member save(Member member) {
        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRoles(new HashSet<>(roleRepo.findAll()));
        return memberRepo.save(member);
    }

    @Override
    public Member findByUsername(String username) throws UsernameNotFoundException {
        return memberRepo.findByUsername(username);
    }
}
