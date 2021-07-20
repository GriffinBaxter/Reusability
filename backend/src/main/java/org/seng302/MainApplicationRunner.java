package org.seng302;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.model.Address;
import org.seng302.model.MarketCardNotification;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.repository.*;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * This spring component runs at application startup to do some initialisation
 * work.
 */
@Component
@Profile("!test")
public class MainApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(MainApplicationRunner.class.getName());
    private UserRepository userRepository;
    private BusinessRepository businessRepository;
    private AddressRepository addressRepository;
    private ProductRepository productRepository;
    private MarketplaceCardRepository marketplaceCardRepository;
    private MarketCardNotificationRepository marketCardNotificationRepository;

    @Value("${dgaa.email}")
    private String dgaaEmail;
    @Value("${dgaa.password}")
    private String dgaaPassword;

    @Autowired
    private ConfigurableApplicationContext context;

    /**
     * This constructor is implicitly called by Spring (purpose of the @Autowired
     * annotation). Injected constructors can be supplied with instances of other
     * classes (i.e. dependency injection)
     */
    @Autowired
    public MainApplicationRunner(UserRepository userRepository,
                                 BusinessRepository businessRepository,
                                 AddressRepository addressRepository,
                                 ProductRepository productRepository,
                                 MarketplaceCardRepository marketplaceCardRepository,
                                 MarketCardNotificationRepository marketCardNotificationRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.marketplaceCardRepository = marketplaceCardRepository;
        this.marketCardNotificationRepository = marketCardNotificationRepository;
    }

    /**
     * By overriding the run method, we tell Spring to run this code at startup. See
     * https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (isPresent(dgaaEmail) && isPresent(dgaaPassword)) {
            logger.info("Startup application with {}", args);
            userRepository.findAll().forEach(logger::info);
            businessRepository.findAll().forEach(logger::info);
            addressRepository.findAll().forEach(logger::info);

        } else {
            logger.fatal("Environment variables for DGAA email and/or password are not defined.");
            logger.info("-- shutting down application --");
            context.close();
        }
    }

    /**
     * Checks to see whether a Default Global Application Admin exists.
     * If one does not exist the function automatically creates a
     * Default Global Application Admin with a predefined username and password.
     * This is a scheduled task and will therefore periodically check. The
     * period between checks can be altered by changing the
     * fixed-delay.in.milliseconds section in the application.properties file.
     * The system logs are updated when checked.
     *
     * @throws Exception An exception
     */
    @Scheduled(fixedDelayString = "${fixed-delay.in.milliseconds}")
    public void checkDGAAExists() throws Exception {
        if (!(userRepository.existsByRole(Role.DEFAULTGLOBALAPPLICATIONADMIN))) {
            Address address = new Address(
                    "3/24",
                    "Ilam Road",
                    "Christchurch",
                    "Canterbury",
                    "New Zealand",
                    "90210",
                    "Ilam"
            );
            addressRepository.save(address);
            User dGAA = new User(
                    "John",
                    "Doe",
                    "S",
                    "Johnny",
                    "Biography",
                    dgaaEmail,
                    LocalDate.of(2000, 2, 2),
                    "0271316",
                    address,
                    dgaaPassword,
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    Role.DEFAULTGLOBALAPPLICATIONADMIN);
            dGAA = userRepository.save(dGAA);
            logger.error("DGAA does not exist. New DGAA created {}", dGAA);
        } else {
            logger.info("DGGA exists.");
        }
    }

    /**
     * Check displayPeriodEnd for all Market cards, and compare with current time, if the card will expired in next 24h,
     * create/update a notification for this card.
     */
    @Scheduled(fixedDelayString = "${fixed-delay.in.milliseconds}")
    public void checkNotifications() {
        // Time
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expiredTime = currentTime.plusDays(1);

        List<MarketplaceCard> marketplaceCards = marketplaceCardRepository.findAll();
        String notificationMessageFormat = "Your card (%s) will be expired in %s.";
        String deletingNotificationMessage = "Your card (%s) has been deleted.";
        Long second;
        String timeLeft;
        String fullNotificationMessage;
        MarketCardNotification marketCardNotification;

        for (MarketplaceCard marketplaceCard : marketplaceCards) {
            logger.debug("Marketplace card retrieved with ID {}: {}", marketplaceCard.getId(), marketplaceCard);
            if (marketplaceCard.getDisplayPeriodEnd().isBefore(expiredTime)) {
                if (marketplaceCard.getDisplayPeriodEnd().isAfter(currentTime)) {
                    // expired in next 24h
                    logger.debug("Marketplace card with ID {} will be expired in next 24h. ({})",
                            marketplaceCard.getId(), marketplaceCard);

                    second = Duration.between(currentTime, marketplaceCard.getDisplayPeriodEnd()).getSeconds();
                    timeLeft = String.format("%dh %dm %ds", second / 3600, second % 3600 / 60, second % 60);
                    fullNotificationMessage = String.format(notificationMessageFormat, marketplaceCard.getTitle(), timeLeft);
                } else {
                    // expired
                    fullNotificationMessage = String.format(deletingNotificationMessage, marketplaceCard.getTitle());
                }

                Optional<MarketCardNotification> optionalNotification = marketCardNotificationRepository
                        .findByUserIdAndMarketCardId(marketplaceCard.getCreatorId(), marketplaceCard.getId());

                if (optionalNotification.isEmpty()) {
                    // Create
                    logger.debug("Notification message {} is not exist.", optionalNotification);
                    marketCardNotification = new MarketCardNotification(marketplaceCard.getCreatorId(),
                            marketplaceCard.getId(),
                            marketplaceCard,
                            fullNotificationMessage,
                            currentTime);

                } else {
                    // Update
                    logger.debug("Notification message {} has been retrieved.", optionalNotification);
                    marketCardNotification = optionalNotification.get();
                    marketCardNotification.setDescription(fullNotificationMessage);
                    marketCardNotification.setCreated(currentTime);
                }
                marketCardNotificationRepository.save(marketCardNotification);
                logger.debug("Notification message ({}) has been saved.", marketCardNotification.getDescription());
            }
        }
    }

    public boolean isPresent(String dgaaData) {
        return (dgaaData != null) && !(dgaaData.isEmpty());
    }

}
