package edu.boun.yilmaz4.deniz.akitaBackend.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String username) {
        super("A member by username " + username + " is not found.");
    }

}
