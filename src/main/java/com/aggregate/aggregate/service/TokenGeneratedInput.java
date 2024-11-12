package com.aggregate.aggregate.service;

public class TokenGeneratedInput {
    private String email;

    public TokenGeneratedInput(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
