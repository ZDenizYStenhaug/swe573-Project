package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum Role {
    ADMIN("admin"),
    USER("user");

    public static Role fromString(String value) {
        for (Role a : Role.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known role");
    }

    private final String s;

    Role(String s) {
        this.s = s;
    }

    public static Role getDefault() {
        return Role.USER;
    }

    public String toString() {
        return s;
    }
}
