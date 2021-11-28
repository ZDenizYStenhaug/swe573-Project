package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OfferRepo extends JpaRepository<Offer, Long> {
    void deleteOfferById(Long id);

    Optional<Offer> findOfferById(Long id);

}
