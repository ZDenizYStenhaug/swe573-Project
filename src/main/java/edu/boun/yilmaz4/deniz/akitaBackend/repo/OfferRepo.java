package edu.boun.yilmaz4.deniz.akitaBackend.repo;

import edu.boun.yilmaz4.deniz.akitaBackend.model.Member;
import edu.boun.yilmaz4.deniz.akitaBackend.model.Offer;
import edu.boun.yilmaz4.deniz.akitaBackend.model.RecurringOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OfferRepo <T extends Offer> extends JpaRepository<T, Long>  {
    void deleteOfferById(Long id);
    Offer findOfferById(Long id);

    @Query("Select o from Offer o where offer_type = 'Offer' and (o.status = 'OPEN_TO_APPLICATIONS' or o.status = 'CLOSED_TO_APPLICATIONS')")
    List<Offer> findAllOffers();

    @Query("Select o from Offer o where offer_type = 'Offer' and o.offerer = :member")
    List<Offer> findAllOffersByMember(@Param("member") Member member);

    @Query("Select ro from RecurringOffer ro where ro.parentOffer = :offer AND ro.date = :date")
    RecurringOffer findRecurringOfferByDate(@Param("date")LocalDateTime date, @Param("offer") Offer offer);

}
