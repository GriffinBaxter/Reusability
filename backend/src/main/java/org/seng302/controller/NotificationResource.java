package org.seng302.controller;

import org.seng302.Authorization;
import org.seng302.model.Notification;
import org.seng302.model.User;
import org.seng302.model.repository.NotificationRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class NotificationResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    //TODO: Test
    @GetMapping("/notification")
    public List<String> retrieveAllNotifications(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken
    ) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        List<String> notificationStrings = new ArrayList<>();
        List<Notification> notifications = notificationRepository.findAllByReceiverId(currentUser.getId());
        for (Notification notification: notifications) {
            notificationStrings.add(notification.expiryFormTOString());
        }
        return notificationStrings;
    }
}
