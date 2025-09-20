package com.hexagonal.ms_foodcourt.domain.model.response;

public class TokenResponse {
    private String token;

    public TokenResponse(String token) {
        this.token = token;
    }

    public TokenResponse() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
