package edu.boun.yilmaz4.deniz.akitaBackend.model;

import java.time.LocalDateTime;

public class OfferManagementResponse {

    private Long parentOfferId;
    private Long selectedOfferId;
    private LocalDateTime selectedDate;
    private Long applicantMemberId;

    public Long getParentOfferId() {
        return parentOfferId;
    }

    public void setParentOfferId(Long parentOfferId) {
        this.parentOfferId = parentOfferId;
    }

    public Long getSelectedOfferId() {
        return selectedOfferId;
    }

    public void setSelectedOfferId(Long selectedOfferId) {
        this.selectedOfferId = selectedOfferId;
    }

    public LocalDateTime getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDateTime selectedDate) {
        this.selectedDate = selectedDate;
    }

    public Long getApplicantMemberId() {
        return applicantMemberId;
    }

    public void setApplicantMemberId(Long applicantMemberId) {
        this.applicantMemberId = applicantMemberId;
    }

}
