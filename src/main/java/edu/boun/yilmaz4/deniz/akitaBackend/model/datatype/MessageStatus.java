package edu.boun.yilmaz4.deniz.akitaBackend.model.datatype;

public enum MessageStatus {

    READ("read"),
    UNREAD("unread"),
    DELETED("deleted");

    public static MessageStatus fromString(String value) {
        for (MessageStatus a : MessageStatus.values()) {
            if(a.s.equals(value) || a.name().equals(value) || value.startsWith(a.s)) {
                return a;
            }
        }
        throw new IllegalArgumentException(value + " is not a known message status");

    }

    private final String s;

    MessageStatus(String s) {
        this.s = s;
    }

    public static MessageStatus getDefault() {
        return MessageStatus.UNREAD;
    }

    public String toString() {
        return s;
    }

}
