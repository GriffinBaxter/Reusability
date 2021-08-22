package org.seng302.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.view.outgoing.SoldListingNotificationPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class NotificationResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @Autowired
    private KeywordNotificationRepository keywordNotificationRepository;

    @Autowired
    private ListingNotificationRepository listingNotificationRepository;

    @Autowired
    private SoldListingNotificationRepository soldListingNotificationRepository;

    public NotificationResource(UserRepository userRepository,
                                BusinessRepository businessRepository,
                                MarketCardNotificationRepository marketCardNotificationRepository,
                                KeywordNotificationRepository keywordNotificationRepository,
                                ListingNotificationRepository listingNotificationRepository,
                                SoldListingNotificationRepository soldListingNotificationRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.marketCardNotificationRepository = marketCardNotificationRepository;
        this.keywordNotificationRepository = keywordNotificationRepository;
        this.listingNotificationRepository = listingNotificationRepository;
        this.soldListingNotificationRepository = soldListingNotificationRepository;
    }

    private static final Logger logger = LogManager.getLogger(NotificationResource.class.getName());

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

        List<Object> notificationPayloads = new ArrayList<>();
        List<MarketCardNotification> marketCardNotifications = marketCardNotificationRepository.findAllByUserId(currentUser.getId());
        List<KeywordNotification> keywordNotifications = new ArrayList<>();
        List<ListingNotification> listingNotifications = listingNotificationRepository.findAllByUsersId(currentUser.getId());
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
        for (ListingNotification listingNotification : listingNotifications) {
            logger.debug("Listing Notification (Id: {}) received.", listingNotification.getId());
            notificationPayloads.add(listingNotification.toListingNotificationPayload());
        }

        return notificationPayloads;
    }

    /**
     * Retrieve all notifications for a business.
     *
     * @param id           The ID of the business you'd like to retrieve the notifications for.
     * @param sessionToken The token used to identify the user.
     * @return List<SoldListingNotificationPayloads> The list of sold listing notifications for the business.
     * @throws Exception Exception
     */
    @GetMapping("/businesses/{id}/notifications")
    public List<SoldListingNotificationPayload> retrieveAllBusinessNotifications(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken, @PathVariable Integer id
    ) throws Exception {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        Authorization.verifyBusinessExists(id, businessRepository);

        Authorization.verifyBusinessAdmin(currentUser, id);

        List<SoldListingNotification> soldListingNotifications = soldListingNotificationRepository.findAllByBusinessId(id);
        List<SoldListingNotificationPayload> soldListingNotificationPayloads = new ArrayList<>();

        for (SoldListingNotification soldListingNotification : soldListingNotifications) {
            logger.debug("Sold Listing Notification received: {}", soldListingNotification);
            soldListingNotificationPayloads.add(soldListingNotification.toSoldListingNotificationPayload());
        }

        return soldListingNotificationPayloads;
    }

    /**
     * DELETE endpoint for deleting a notification, given the notification id.
     *
     * @param id           The ID of the notification you'd like to delete.
     * @param sessionToken The token used to identify the user.
     */
    @DeleteMapping("/users/notifications/{id}")
    @Transactional
    @ResponseStatus(code = HttpStatus.OK, reason = "Notifications Successfully deleted")
    public void deleteNotification(
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer id,
            @RequestParam String type
    ) {
        //401
        User currentUser = Authorization.getUserVerifySession(sessionToken, userRepository);

        switch (type) {
            case "KEYWORD":
                deleteKeywordNotification(currentUser, id);
                break;
            case "LISTING":
                deleteListingNotification(currentUser, id);
                break;
            case "SOLD_LISTING" :
                deleteSoldListingNotification(id);
                break;
            case "MARKETPLACE" :
                deleteMarketPlaceCardNotification(id);
                break;
            default:
                logger.error("Notification Deletion Error - Notification Type Error.");
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Invalid notification type has been given.");
        }
    }

    private void deleteKeywordNotification(User user, Integer id) {
        //403
        // Checks user is GAA/DGAA if notification is only for GAA/DGAA
        if (!Authorization.isGAAorDGAA(user)) {
            logger.error("Notification Deletion Error - 403 [FORBIDDEN] - User doesn't have permissions to delete notification");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid permissions to delete notifications");
        }

        logger.debug("Notification Deletion Update - User has permissions to delete notifications");

        //406
        Optional<KeywordNotification> notification = keywordNotificationRepository.findById(id);
        if (notification.isEmpty()) {
            logger.error("Keyword Notification Deletion Error - 406 [NOT_ACCEPTABLE] - Keyword Notification at ID {} not found", id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Keyword Notification not found");
        }
        logger.debug("Keyword Notification Deletion Update - Notification found");

        keywordNotificationRepository.delete(notification.get());
        logger.debug("Keyword Notification Deletion Update - Notification Deleted");
    }

    private void deleteListingNotification(User user, Integer id) {
        //406
        Optional<ListingNotification> optionalListingNotification = listingNotificationRepository.findById(id);
        if (optionalListingNotification.isEmpty()) {
            logger.error("Listing Notification Deletion Error - 406 [NOT_ACCEPTABLE] - Listing Notification at ID {} not found", id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Listing Notification not found");
        }

        logger.debug("Listing Notification Deletion Update - Notification found");
        //remove user from notification subscription list
        ListingNotification listingNotification = optionalListingNotification.get();
        List<User> users = listingNotification.getUsers();
        List<User> updatedUser = new ArrayList<>();

        if (users.contains(user)) {
            for (User user1: users) {
                if (user != user1) {
                    updatedUser.add(user);
                }
            }
            listingNotification.setUsers(updatedUser);
            listingNotificationRepository.save(listingNotification);
        } else {
            //406
            logger.error("Listing Notification Deletion Error - 406 [NOT_ACCEPTABLE] - User at ID {} not subscribed to listing notification", user.getId());
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "User not subscribed to listing notification");
        }

        logger.debug("Listing Notification Deletion Update - User have been remove from notice list");

        //list is empty, so delete notification
        if (users.isEmpty()) {
            listingNotificationRepository.deleteById(id);
            logger.debug("Listing Notification Deletion Update - Notification Deleted");
        }
    }

    private void deleteSoldListingNotification(Integer id) {
        //406
        if (soldListingNotificationRepository.findById(id).isEmpty()) {
            logger.error("Sold Listing Notification Deletion Error - 406 [NOT_ACCEPTABLE] - Sold Listing Notification at ID {} not found", id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Sold Listing Notification not found");
        }
        logger.debug("Sold Listing Notification Deletion Update - Notification found");

        soldListingNotificationRepository.deleteById(id);
        logger.debug("Sold Listing Notification Deletion Update - Notification Deleted");
    }

    private void deleteMarketPlaceCardNotification(Integer id) {
        //406
        if (marketCardNotificationRepository.findById(id).isEmpty()) {
            logger.error("Marketplace Card Notification Deletion Error - 406 [NOT_ACCEPTABLE] - Marketplace Card Notification at ID {} not found", id);
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Marketplace Card Notification not found");
        }
        logger.debug("Marketplace Card Notification Deletion Update - Notification found");

        marketCardNotificationRepository.deleteById(id);
        logger.debug("Marketplace Card Notification Deletion Update - Notification Deleted");
    }
}
