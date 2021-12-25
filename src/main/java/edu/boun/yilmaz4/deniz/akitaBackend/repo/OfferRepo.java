package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    void deleteOfferById(Long id);

    Offer findOfferById(Long id);

}
