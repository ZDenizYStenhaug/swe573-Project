package edu.boun.yilmaz4.deniz.akitaBackend.service;

import edu.boun.yilmaz4.deniz.akitaBackend.exception.OfferNotFoundException;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.repo.OfferRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OfferService {

    private final OfferRepo offerRepo;
    private final GeoLocationRepo geoLocationRepo;
    @Autowired
    public OfferService(OfferRepo offerRepo, GeoLocationRepo geoLocationRepo) {
        this.offerRepo = offerRepo;
        this.geoLocationRepo = geoLocationRepo;
    }

    @Transactional
    public Offer addOffer(Offer offer) {
        return offerRepo.save(offer);
    }

    @Transactional
    public List<Offer> allOffers() {
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

//    @Transactional
//    public Optional<GeoLocation> getOfferLocation(Offer offer) {
//        return geoLocationRepo.findGeoLocationById(offer.getLocationId());
//    }
//    Geolocation

}
