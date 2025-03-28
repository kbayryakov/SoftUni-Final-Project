package com.myproject.kbayryakov.services.dto;

import jakarta.validation.constraints.NotNull;

public class LoginUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;

    public LoginUserDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
