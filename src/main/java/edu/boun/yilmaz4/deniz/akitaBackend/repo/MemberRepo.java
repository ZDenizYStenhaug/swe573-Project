package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepo extends JpaRepository<Member,Long> {


    Member findMemberById(Long id);

    Member findByUsername(String username);
}
