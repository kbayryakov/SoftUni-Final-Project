package com.myproject.kbayryakov.notifications.dto;

import java.util.UUID;

public class NotificationRequestDto{
    private UUID userId;

    private String subject;

    private String body;

    public NotificationRequestDto() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
