package com.myproject.kbayryakov.notifications.dto;

import java.util.UUID;

public class UserInfoDto {
    private UUID userId;

    private String contactInfo;

    public UserInfoDto() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
}
