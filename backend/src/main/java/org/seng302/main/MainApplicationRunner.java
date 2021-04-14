package org.seng302.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.BusinessRepository;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

/**
 * This spring component runs at application startup to do some initialisation
 * work.
 */
@Component
public class MainApplicationRunner implements ApplicationRunner {

    private static final Logger logger = LogManager.getLogger(MainApplicationRunner.class.getName());
    private UserRepository userRepository;
    private BusinessRepository businessRepository;

    /**
     * This constructor is implicitly called by Spring (purpose of the @Autowired
     * annotation). Injected constructors can be supplied with instances of other
     * classes (i.e. dependency injection)
     */
    @Autowired
    public MainApplicationRunner(UserRepository userRepository, BusinessRepository businessRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
    }

    /**
     * By overriding the run method, we tell Spring to run this code at startup. See
     * https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne
     *
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        logger.info("Startup application with {}", args);

        userRepository.findAll().forEach(logger::info);
        businessRepository.findAll().forEach(logger::info);
    }


    /**
     * Checks to see whether a Default Global Application Admin exists.
     * If one does not exist the function automatically creates a
     * Default Global Application Admin with a predefined username and password.
     * This is a scheduled task and will therefore periodically check. The
     * period between checks can be altered by changing the
     * fixed-delay.in.milliseconds section in the application.properties file.
     * The system logs are updated when checked.
     * @throws Exception
     */
    @Scheduled(fixedDelayString = "${fixed-delay.in.milliseconds}")
    public void checkDGAAExists() throws Exception {
        if (!(userRepository.existsByRole(Role.DEFAULTGLOBALAPPLICATIONADMIN))) {
            User dGAA = new User(
                    "John",
                    "Doe",
                    "S",
                    "Johnny",
                    "Biography",
                    "email@email.com",
                    LocalDate.of(2020, 2, 2),
                    "0271316",
                    "address",
                    "password",
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    Role.DEFAULTGLOBALAPPLICATIONADMIN);
            dGAA = userRepository.save(dGAA);
            logger.error("DGAA does not exist. New DGAA created {}", dGAA);
        } else {
            logger.info("DGGA exists.");
        }
    }
}
