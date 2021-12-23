package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.exception.OfferNotFoundException;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.datatype.OfferStatus;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.OfferRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class OfferService {
    private static final Logger logger = LoggerFactory.getLogger(OfferService.class);

    @Autowired
    private OfferRepo offerRepo;

    @Transactional
    public Offer addOffer(Offer offer) {
        offer.setStatus(OfferStatus.OPEN_TO_APPLICATIONS);
        logger.info("saving offer " + offer);
        return offerRepo.save(offer);
    }

    @Transactional
    public List<Offer> allOffers() {
        logger.info("getting all offers");
        return offerRepo.findAll();
    }

    @Transactional
    public void deleteOffer(Long id) {
        offerRepo.deleteOfferById(id);
    }

    @Transactional
    public Offer findOfferById(Long id) {
        return offerRepo.findOfferById(id).orElseThrow(() -> new OfferNotFoundException("offer by id " + id + "was not found."));
    }

    @Transactional
    public Offer updateOffer(Offer offer) {
        return offerRepo.save(offer);
    }

    @Transactional
    public boolean checkForUniqueTimestamp(Member member, Offer offer) {
        Set<Offer> allOffers = member.getOffers();
        for (Offer o : allOffers) {
            if (o.getDate().equals(offer.getDate())) {
                return true;
            }
        }
        return false;
    }
}
