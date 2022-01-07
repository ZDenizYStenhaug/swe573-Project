package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.model.EventFeedback;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.OfferFeedback;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.EventFeedbackRepo;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.OfferFeedbackRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    @Autowired
    private OfferFeedbackRepo offerFeedbackRepo;
    @Autowired
    private EventFeedbackRepo eventFeedbackRepo;
    @Autowired
    private MemberServiceImpl memberService;
    @Autowired
    private MessageService messageService;

    @Transactional
    public void saveOfferFeedback(OfferFeedback feedback) {
        offerFeedbackRepo.save(feedback);
        // add the rating to the offerer's reputation points
        Member receiver = feedback.getReceiver();
        receiver.setReputationPoints(receiver.getReputationPoints() + feedback.getRating());
        memberService.updateMember(receiver);
        // add 1 reputation point to the giver
        Member giver = feedback.getGiver();
        giver.setReputationPoints(giver.getReputationPoints() + 1);
        memberService.updateMember(giver);
    }

    @Transactional
    public void saveEventFeedback(EventFeedback feedback) {
        eventFeedbackRepo.save(feedback);
        // add the rating to the offerer's reputation points
        Member receiver = feedback.getReceiver();
        receiver.setReputationPoints(receiver.getReputationPoints() + feedback.getRating());
        memberService.updateMember(receiver);
        // add 1 reputation point to the giver
        Member giver = feedback.getGiver();
        giver.setReputationPoints(giver.getReputationPoints() + 1);
        memberService.updateMember(giver);
    }
}
