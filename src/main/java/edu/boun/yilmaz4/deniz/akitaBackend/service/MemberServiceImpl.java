package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.Role;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

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
        return memberRepo.save(member);
    }

    @Override
    public Member findByUsername(String username) throws UsernameNotFoundException {
        return memberRepo.findByUsername(username);
    }
}
