package edu.boun.yilmaz4.deniz.akitaBackend.model;

import javax.persistence.*;
import java.io.Serializable;


@Entity
public class RecurringEvent extends Event implements Serializable{

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event parentEvent;

    public RecurringEvent() {
    }

    public Event getParentEvent() {
        return parentEvent;
    }

    public void setParentEvent(Event parentEvent) {
        this.parentEvent = parentEvent;
    }
}
