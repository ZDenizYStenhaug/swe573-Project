package edu.boun.yilmaz4.deniz.akitaBackend.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class RecurringOffer extends Offer implements Serializable {
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer parentOffer;

    public RecurringOffer() {}

    public Offer getParentOffer() {
        return parentOffer;
    }

    public void setParentOffer(Offer parentOffer) {
        this.parentOffer = parentOffer;
    }
}
