package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.KeywordNotification;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.User;
import org.seng302.model.repository.KeywordNotificationRepository;
import org.seng302.model.repository.MarketCardNotificationRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.view.outgoing.MarketCardNotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
public class NotificationResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @Autowired
    private KeywordNotificationRepository keywordNotificationRepository;

    public NotificationResource(UserRepository userRepository, MarketCardNotificationRepository marketCardNotificationRepository, KeywordNotificationRepository keywordNotificationRepository) {
        this.userRepository = userRepository;
        this.marketCardNotificationRepository = marketCardNotificationRepository;
        this.keywordNotificationRepository = keywordNotificationRepository;
    }

    private static final Logger logger = LogManager.getLogger(MarketplaceCardResource.class.getName());

    /**
     * Retrieve all notifications for the current user.
     *
     * @param sessionToken The token used to identify the user.
     * @return List<Object> The list of notifications for the current user.
     * @throws Exception Exception
     */
    @GetMapping("/users/notifications")
    public List<Object> retrieveAllNotifications(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken
    ) throws Exception {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);
        logger.debug("User (Id: {}) received.", currentUser.getId());

        List<Object> notificationPayloads = new ArrayList<>();
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(currentUser.getId());
        List<KeywordNotification> keywordNotifications = new ArrayList<>();
        if (Authorization.isGAAorDGAA(currentUser)) {
            keywordNotifications = keywordNotificationRepository.findAll();
        }

        for (MarketCardNotification marketCardNotification : marketCardNotifications) {
            logger.debug("Market Card Notification (Id: {}) received.", marketCardNotification.getId());
            notificationPayloads.add(marketCardNotification.toMarketCardNotificationPayload());
        }
        for (KeywordNotification keywordNotification : keywordNotifications) {
            logger.debug("Keyword Notification (Id: {}) received.", keywordNotification.getId());
            notificationPayloads.add(keywordNotification.toKeywordNotificationPayload());
        }
        return notificationPayloads;
    }
}
