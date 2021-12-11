package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum Badge {
    NEWCOMER("newcomer"),
    MENTOR("mentor"),
    SUPER_MENTOR("super_mentor"),
    GURU("guru");

    public static Badge fromString(String value) {
        for (Badge a : Badge.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known offer status");
    }

    private final String s;

    Badge(String s) {
        this.s = s;
    }

    public String toString() {
        return s;
    }
}
