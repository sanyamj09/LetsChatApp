package com.ChatApp.ChatAppSecurity.dto;



public class SignInResponse {
    private String username;
    private String accesstoken;
    private String refreshToken;

    public SignInResponse(String username, String accesstoken, String refreshToken) {
        this.username = username;
        this.accesstoken = accesstoken;
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
