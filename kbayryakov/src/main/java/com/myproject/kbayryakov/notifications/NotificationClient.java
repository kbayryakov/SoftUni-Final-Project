package com.myproject.kbayryakov.notifications;

import com.myproject.kbayryakov.notifications.dto.NotificationRequestDto;
import com.myproject.kbayryakov.notifications.dto.UserInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification", url = "http://localhost:8081/api/v1/notifications")
public interface NotificationClient {

    @PostMapping("/users")
    ResponseEntity<Void> sendUserData(@RequestBody UserInfoDto userInfoDto);

    @PostMapping
    ResponseEntity<Void> sendNotification(@RequestBody NotificationRequestDto notificationRequest);
}
