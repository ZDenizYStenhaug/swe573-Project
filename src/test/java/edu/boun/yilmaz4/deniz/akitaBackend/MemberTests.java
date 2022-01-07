package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MessageRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MemberTests {

    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MessageRepo messageRepo;


    @Test
    void registrationTest() {
        Member member = new Member();
        member.setUsername("lemon");
        member.setDescription("i like lemons");
        member.setEmail("random@gmail.com");
        member.setPassword("lemon");
        Member registeredMember = memberService.register(member);
        // check if member exists
        Assertions.assertNotNull(registeredMember);
        // check if welcome message was sent
        List<Message> messages = messageRepo.getAllForMember(registeredMember);
        Assertions.assertEquals(1, messages.size());
        deleteAllMessages(messages);
        memberRepo.delete(registeredMember);
        Assertions.assertNull(memberRepo.findMemberById(registeredMember.getId()));
    }

    void deleteAllMessages(List<Message> messages) {
        for (Message m: messages) {
            messageRepo.delete(m);
        }

    }
}
