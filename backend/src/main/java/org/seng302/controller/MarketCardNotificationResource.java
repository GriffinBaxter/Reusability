package org.seng302.controller;

import org.seng302.Authorization;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.User;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class MarketCardNotificationResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    //TODO: Test
    @GetMapping("/notification")
    public List<String> retrieveAllNotifications(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken
    ) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        List<String> notificationStrings = new ArrayList<>();
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(currentUser.getId());
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            notificationStrings.add(marketCardNotification.toString());
        }
        return notificationStrings;
    }
}
