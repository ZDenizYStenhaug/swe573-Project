package edu.boun.yilmaz4.deniz.akitaBackend;

import static org.assertj.core.api.Assertions.assertThat;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback()
public class MemberRepoTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private MemberRepo memberRepo;

    @Test
    public void testCreateUser() {
        Member member = new Member();
        member.setEmail("ravikumar@gmail.com");
        member.setPassword("ravi2020");
        member.setUsername("melon");

        Member savedUser = memberRepo.save(member);

        Member existUser = testEntityManager.find(Member.class, savedUser.getId());

        assertThat(member.getEmail()).isEqualTo(existUser.getEmail());
    }

}
