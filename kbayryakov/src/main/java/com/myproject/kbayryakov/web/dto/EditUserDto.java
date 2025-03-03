package com.myproject.kbayryakov.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EditUserDto {
    @NotNull
    @Size(min = 3, max = 10)
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Size(min = 6, max = 20)
    private String newPassword;
    @NotNull
    @Size(min = 6, max = 20)
    private String confirmNewPassword;
    @NotNull
    private String email;

    public EditUserDto() {
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
