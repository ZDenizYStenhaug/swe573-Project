package edu.boun.yilmaz4.deniz.akitaBackend.controller;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Constants;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.service.OfferService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.URI_OFFER)
public class OfferController {
    private final OfferService offerService;
    @Autowired
    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping(Constants.URI_ALL_OFFERS)
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerService.allOffers();
        return new ResponseEntity<>(offers, HttpStatus.OK);
    }

    @GetMapping(Constants.URI_FIND_OFFER + Constants.URI_ID)
    public ResponseEntity<Offer> findOffer(@PathVariable("id") Long id) {
        Offer offer = offerService.findOfferById(id);
        return new ResponseEntity<>(offer, HttpStatus.OK);
    }

    @PostMapping(Constants.URI_ADD_OFFER)
    public ResponseEntity<Offer> addOffer(@RequestBody Offer offer) {
        Offer newOffer = offerService.addOffer(offer);
        return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
    }

    @PutMapping(Constants.URI_UPDATE_OFFER)
    public ResponseEntity<Offer> updateOffer(@RequestBody Offer offer) {
        Offer updatedOffer = offerService.updateOffer(offer);
        return new ResponseEntity<>(updatedOffer, HttpStatus.OK);
    }

    @DeleteMapping(Constants.URI_DELETE + Constants.URI_ID)
    public ResponseEntity<?> deleteOffer(@PathVariable("id") Long id) {
        offerService.deleteOffer(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
