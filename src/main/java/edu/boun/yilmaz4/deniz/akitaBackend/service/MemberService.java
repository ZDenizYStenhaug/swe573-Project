package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.exception.MemberNotFoundException;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;

public interface MemberService {

    Member findByUsername(String email) throws MemberNotFoundException;

    Member register(Member member);
}
