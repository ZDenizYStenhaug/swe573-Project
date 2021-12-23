package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum EventStatus {
    OPEN_TO_REGISTRATION("open to registration"),
    PAST_EVENT("past event");

    public static EventStatus fromString(String value) {
        for (EventStatus a : EventStatus.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known event status");
    }

    private final String s;

    EventStatus (String s) {
        this.s = s;
    }

    public static EventStatus getDefault() {
        return EventStatus.OPEN_TO_REGISTRATION;
    }

    public String toString() {
        return s;
    }
}
