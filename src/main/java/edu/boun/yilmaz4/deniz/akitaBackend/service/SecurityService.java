package edu.boun.yilmaz4.deniz.akitaBackend.service;

public interface SecurityService {
    boolean isAuthenticated();
    void autoLogin(String username, String password);
}
