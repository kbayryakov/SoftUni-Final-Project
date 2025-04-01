package com.myproject.kbayryakov.notifications.service;

import com.myproject.kbayryakov.notifications.NotificationClient;
import com.myproject.kbayryakov.notifications.dto.NotificationRequestDto;
import com.myproject.kbayryakov.notifications.dto.UserInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationClient notificationClient;

    @Autowired
    public NotificationService(NotificationClient notificationClient) {
        this.notificationClient = notificationClient;
    }

    public void saveUser(UUID userId, String contactInfo){
        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(userId);
        userInfoDto.setContactInfo(contactInfo);

        try {
            ResponseEntity<Void> response = this.notificationClient.sendUserData(userInfoDto);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.printf("Feign call failed. Can't save user with id = %s.%n", userId.toString());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void sendNotification(UUID userId, String subject, String body) {
        NotificationRequestDto notificationRequest = new NotificationRequestDto();
        notificationRequest.setUserId(userId);
        notificationRequest.setSubject(subject);
        notificationRequest.setBody(body);

        try {
            ResponseEntity<Void> response = notificationClient.sendNotification(notificationRequest);
            if (!response.getStatusCode().is2xxSuccessful()) {
                System.out.printf("Feign call failed. Can't send email to user with id = %s.%n",
                        userId.toString());
            }
        } catch (Exception e) {
            System.out.printf("Can't send email to user with id = %s due to Internal Server Error.%n",
                    userId.toString());
        }
    }
}
