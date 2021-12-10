package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum OfferStatus {
    OPEN_TO_APPLICATIONS("open to applications"),
    CLOSED_TO_APPLICATIONS("closed to applications"),
    PAST_OFFER("past offer");

    public static OfferStatus fromString(String value) {
        for (OfferStatus a : OfferStatus.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known offer status");
    }

    private final String s;

    OfferStatus(String s) {
        this.s = s;
    }

    public Role getDefault() {
        return Role.USER;
    }

    public String toString() {
        return s;
    }
}
