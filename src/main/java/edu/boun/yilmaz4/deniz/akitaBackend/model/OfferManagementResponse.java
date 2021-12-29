package edu.boun.yilmaz4.deniz.akitaBackend.model;

import java.time.LocalDateTime;
import java.util.List;

public class OfferManagementResponse {

    private Long parentOfferId;
    private Long selectedOfferId;
    private List<LocalDateTime> dates;
    private LocalDateTime selectedDate;
    private List<Member> applicants;
    private List<Member> participants;

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

    public List<LocalDateTime> getDates() {
        return dates;
    }

    public void setDates(List<LocalDateTime> dates) {
        this.dates = dates;
    }

    public LocalDateTime getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(LocalDateTime selectedDate) {
        this.selectedDate = selectedDate;
    }

    public List<Member> getApplicants() {
        return applicants;
    }

    public void setApplicants(List<Member> applicants) {
        this.applicants = applicants;
    }

    public List<Member> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Member> participants) {
        this.participants = participants;
    }
}
