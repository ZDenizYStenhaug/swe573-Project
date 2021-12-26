package edu.boun.yilmaz4.deniz.akitaBackend.model;

import java.time.LocalDateTime;

public class OfferApplicatonResponse {

    private Long offerId;
    private String username;
    private LocalDateTime date;

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
