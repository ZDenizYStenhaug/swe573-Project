package edu.boun.yilmaz4.deniz.akitaBackend;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Message;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.RepeatingType;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.GeoLocationRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MemberRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.MessageRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.OfferRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.service.MemberServiceImpl;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class OfferTests {
    @Autowired
    private MemberRepo memberRepo;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private OfferRepo offerRepo;
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private GeoLocationRepo geoLocationRepo;


    @Test
    void addOfferTest() throws JSONException, IOException {
        Offer offer = new Offer();
        offer.setName("random offer name");
        offer.setDescription("picking mushrooms");
        offer.setDate(LocalDateTime.now());
        offer.setCancellationDeadline(2);
        offer.setMaxNumOfParticipants(3);
        offer.setAddress("los angeles");
        offer.setRepeatingType(RepeatingType.NOT_REPEATING);
        Offer savedOffer = offerService.addOffer(offer);
        Assertions.assertNotNull(savedOffer);
        // delete offer
        offerRepo.delete(offer);
        geoLocationRepo.delete(offer.getGeolocation());
        Assertions.assertNull(offerService.findOfferById(offer.getId()));
    }

    @Test
    void gelAllClosebyNotClose() throws JSONException, IOException {
        Member member = createMember();
        Offer offer = new Offer();
        offer.setName("random offer name");
        offer.setDescription("picking mushrooms");
        offer.setDate(LocalDateTime.now());
        offer.setCancellationDeadline(2);
        offer.setMaxNumOfParticipants(3);
        offer.setAddress("los angeles");
        offer.setRepeatingType(RepeatingType.NOT_REPEATING);
        Offer savedOffer = offerService.addOffer(offer);
        List<Offer> selectedOffers = offerService.allClosebyOffers(member);
        Assertions.assertFalse(selectedOffers.stream().anyMatch(item -> savedOffer.getName().equals(item.getName())));
        // delete member and offer
        offerRepo.delete(offer);
        geoLocationRepo.delete(offer.getGeolocation());
        deleteMember(member);
    }

    @Test
    void getAllClosebyTestIsClose() throws JSONException, IOException {
        Member member = createMember();
        Offer offer = new Offer();
        offer.setName("mushroom picking");
        offer.setDescription("picking mushrooms");
        offer.setDate(LocalDateTime.now());
        offer.setCancellationDeadline(2);
        offer.setMaxNumOfParticipants(3);
        offer.setAddress("maçka parkı");
        offer.setRepeatingType(RepeatingType.NOT_REPEATING);
        Offer savedOffer = offerService.addOffer(offer);
        List<Offer> selectedOffers = offerService.allClosebyOffers(member);
        Assertions.assertTrue(selectedOffers.stream().anyMatch(item -> savedOffer.getName().equals(item.getName())));
        // delete member and offer
        offerRepo.delete(offer);
        geoLocationRepo.delete(offer.getGeolocation());
        deleteMember(member);
    }

    Member createMember() throws IOException, JSONException {
        Member member = new Member();
        member.setUsername("lemon");
        member.setDescription("i like lemons");
        member.setEmail("xyz@gmail.com");
        member.setPassword("lemon");
        member.setAddress("kadıköy");
        return memberService.register(member);
    }

    void deleteMember(Member member) {
        List<Message> messages = messageRepo.getAllForMember(member);
        for (Message m: messages) {
            messageRepo.delete(m);
        }
        memberRepo.delete(member);
        geoLocationRepo.delete(member.getGeolocation());
    }

}
