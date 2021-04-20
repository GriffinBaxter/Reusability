package org.seng302.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.address.Address;
import org.seng302.address.AddressPayload;
import org.seng302.address.AddressRepository;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductRepository;
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
    private AddressRepository addressRepository;
    private ProductRepository productRepository;

    /**
     * This constructor is implicitly called by Spring (purpose of the @Autowired
     * annotation). Injected constructors can be supplied with instances of other
     * classes (i.e. dependency injection)
     */
    @Autowired
    public MainApplicationRunner(UserRepository userRepository, BusinessRepository businessRepository, AddressRepository addressRepository, ProductRepository productRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
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
        addressRepository.findAll().forEach(logger::info);

        addTestUsers();
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
     * @throws Exception
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
                    "90210"
            );
            addressRepository.save(address);
            User dGAA = new User(
                    "John",
                    "Doe",
                    "S",
                    "Johnny",
                    "Biography",
                    "email@email.com",
                    LocalDate.of(2000, 2, 2),
                    "0271316",
                    address,
                    "Password123!",
                    LocalDateTime.of(LocalDate.of(2021, 2, 2),
                            LocalTime.of(0, 0)),
                    Role.DEFAULTGLOBALAPPLICATIONADMIN);
            dGAA = userRepository.save(dGAA);
            logger.error("DGAA does not exist. New DGAA created {}", dGAA);

            // Added for testing purposes, REMOVE BEFORE MERGING
            Business business = new Business(
                    2,
                    "name",
                    "some text",
                    address,
                    BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                    LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                    userRepository.findById(2).get()
            );
            businessRepository.save(business);
            Product product = new Product(
                    "PROD",
                    businessRepository.findBusinessById(3).get(),
                    "Beans",
                    "Description",
                    "Manufacturer",
                    20.00,
                    LocalDateTime.of(LocalDate.of(2021, 1, 1),
                            LocalTime.of(0, 0))
            );
            productRepository.save(product);
            Product otherProduct = new Product(
                    "DUCT",
                    businessRepository.findBusinessById(3).get(),
                    "Ice Cream",
                    "New Desc",
                    "New Manufactere",
                    21.00,
                    LocalDateTime.of(LocalDate.of(2022, 1, 1),
                            LocalTime.of(0, 0))
            );
            productRepository.save(otherProduct);
        } else {
            logger.info("DGGA exists.");
        }
    }

    public void addTestUsers() throws Exception {

    }

}
