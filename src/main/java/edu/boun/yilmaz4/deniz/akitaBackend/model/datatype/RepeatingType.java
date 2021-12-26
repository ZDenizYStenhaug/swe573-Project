package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum RepeatingType {
    NOT_REPEATING("not repeating"),
    DAILY("daily"),
    WEEKLY("weekly");

    public static RepeatingType fromString(String value) {
        for (RepeatingType a : RepeatingType.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known repeating type");
    }

    private final String s;

    RepeatingType(String s) {
        this.s = s;
    }

    public RepeatingType getDefault() {
        return RepeatingType.NOT_REPEATING;
    }

    public String toString() {
        return s;
    }
}
