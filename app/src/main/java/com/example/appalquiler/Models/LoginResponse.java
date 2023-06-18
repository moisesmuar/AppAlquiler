package com.example.appalquiler.Models;

public class LoginResponse {
    private Usuario user;
    private String message;

    public LoginResponse() {

    }
    public LoginResponse(Usuario user, String message) {
        this.user = user;
        this.message = message;
    }

    public Usuario getUser() {
        return user;
    }

    public void setUser(Usuario user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
