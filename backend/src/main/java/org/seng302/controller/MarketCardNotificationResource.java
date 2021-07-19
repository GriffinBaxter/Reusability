package org.seng302.controller;

import org.seng302.Authorization;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.User;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.outgoing.MarketCardNotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    /**
     * Retrieve all notifications for current login user
     *
     * @param sessionToken The token used to identify the user.
     * @return List of Notification for current login user.
     * @throws Exception Exception
     */
    @GetMapping("/users/notifications")
    @ResponseStatus(code = HttpStatus.OK, reason = "Notifications retrieving success.")
    public List<MarketCardNotificationPayload> retrieveAllNotifications(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken
    ) throws Exception {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        List<MarketCardNotificationPayload> marketCardNotificationPayloads = new ArrayList<>();
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(currentUser.getId());
        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            marketCardNotificationPayloads.add(marketCardNotification.toMarketCardNotificationPayload());
        }
        return marketCardNotificationPayloads;
    }
}
