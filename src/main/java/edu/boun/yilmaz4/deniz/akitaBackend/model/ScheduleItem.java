package edu.boun.yilmaz4.deniz.akitaBackend.model;


public class ScheduleItem {

    private String status;
    private Offer offer;
    private Event event;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}
