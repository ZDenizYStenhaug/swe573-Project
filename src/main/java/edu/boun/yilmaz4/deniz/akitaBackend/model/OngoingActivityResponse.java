package edu.boun.yilmaz4.deniz.akitaBackend.model;

public class OngoingActivityResponse {

    private Offer ongoingOffer;
    private Event ongoingEvent;

    public Offer getOngoingOffer() {
        return ongoingOffer;
    }

    public void setOngoingOffer(Offer ongoingOffer) {
        this.ongoingOffer = ongoingOffer;
    }

    public Event getOngoingEvent() {
        return ongoingEvent;
    }

    public void setOngoingEvent(Event ongoingEvent) {
        this.ongoingEvent = ongoingEvent;
    }
}
