package org.seng302.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.inventoryItem.InventoryItemRepository;
import org.seng302.business.listing.Listing;
import org.seng302.business.listing.ListingRepository;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductRepository;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private InventoryItemRepository inventoryItemRepository;
    private ListingRepository listingRepository;

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
    public MainApplicationRunner(UserRepository userRepository, BusinessRepository businessRepository, AddressRepository addressRepository, ProductRepository productRepository, InventoryItemRepository inventoryItemRepository, ListingRepository listingRepository) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.addressRepository = addressRepository;
        this.productRepository = productRepository;
        this.inventoryItemRepository = inventoryItemRepository;
        this.listingRepository = listingRepository;
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
            addTestUsers();
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
                    "90210"
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

    public boolean isPresent(String dgaaData) {
        return (dgaaData != null) && !(dgaaData.isEmpty());
    }

    public void addTestUsers() throws Exception {

        //TODO: update to allow for new address format, then uncomment.
        //TODO: write a script for this.
        Address address1 = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        Address address2 = new Address(
                "3/24",
                "Ilam Road",
                "A",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        Address address3 = new Address(
                "3/24",
                "Ilam Road",
                "B",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        Address address4 = new Address(
                "3/24",
                "Ilam Road",
                "C",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        Address address5 = new Address(
                "3/24",
                "Ilam Road",
                "D",
                "Canterbury",
                "New Zealand",
                "90210"
        );


        addressRepository.save(address1);
        addressRepository.save(address2);
        addressRepository.save(address3);
        addressRepository.save(address4);
        addressRepository.save(address5);

        User newUser1 = new User(
                "Alex",
                "Doe",
                "Joe",
                "Generic",
                "Biography",
                "test@email.com",
                LocalDate.of(2008, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser1 = userRepository.save(newUser1);
        logger.info("Added first test user: {}", newUser1);

        User newUser2 = new User(
                "Chad",
                "Taylor",
                "S",
                "Cha",
                "Biography123",
                "chad.taylor@example.com",
                LocalDate.of(2006, 2, 2),
                "0271316678",
                address2,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser2 = userRepository.save(newUser2);
        logger.info("Added second test user: {}", newUser2);

        User newUser3 = new User(
                "Naomi",
                "Wilson",
                "I",
                "Gm",
                "Biography",
                "naomi.wilson@example.com",
                LocalDate.of(2006, 2, 2),
                "0271316",
                address3,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser3 = userRepository.save(newUser3);
        logger.info("Added third test user: {}", newUser3);

        User newUser4 = new User(
                "Seth",
                "Murphy",
                "S",
                "S",
                "Biography",
                "seth.murphy@example.com",
                LocalDate.of(2006, 2, 2),
                "027188316",
                address4,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser4 = userRepository.save(newUser4);
        logger.info("Added fourth test user: {}", newUser4);

        User newUser5 = new User(
                "Minttu",
                "Wainio",
                "A",
                "Min",
                "Biography",
                "minttu.wainio@example.com",
                LocalDate.of(2006, 2, 2),
                "0271316",
                address5,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser5 = userRepository.save(newUser5);
        logger.info("Added fifth test user: {}", newUser5);

        User newUser6 = new User(
                "Francisca",
                "Benitez",
                "T",
                "Fran",
                "Biography",
                "francisca.beni@example.com",
                LocalDate.of(2006, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser6 = userRepository.save(newUser6);
        logger.info("Added sixth test user: {}", newUser6);

        User newUser7 = new User(
                "Francisca",
                "Bznitez",
                "T",
                "Fran",
                "Biography",
                "francisca.benitez@example.com",
                LocalDate.of(2006, 2, 2),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        newUser7 = userRepository.save(newUser7);
        logger.info("Added seventh test user: {}", newUser7);


        Business business = new Business(
                newUser7.getId(),
                "example name",
                "some text",
                newUser7.getHomeAddress(),
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                newUser7
        );
        businessRepository.save(business);

        Product product1 = new Product(
                "APPLE",
                business,
                "Apple",
                "A Description",
                "Manufacturer",
                21.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        productRepository.save(product1);


        Product product2 = new Product(
                "APP-LE",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2020, 1, 1),
                        LocalTime.of(0, 0))
        );
        productRepository.save(product2);
        Product product3 = new Product(
                "APP-LE3",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                11.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        productRepository.save(product3);
        Product product4 = new Product(
                "DUCT",
                business,
                "Duct-Tape",
                "Brand new Description",
                "A New Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 1),
                        LocalTime.of(0, 0))
        );
        productRepository.save(product4);
        Product product5 = new Product(
                "PROD",
                business,
                "Product",
                "New Description",
                "New Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 1),
                        LocalTime.of(1, 0))
        );
        productRepository.save(product5);
        Product product6 = new Product(
                "PROD1",
                business,
                "Product",
                "New Description",
                "New Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 1),
                        LocalTime.of(1, 0))
        );
        product6 = productRepository.save(product6);

        InventoryItem inventoryItem = new InventoryItem(
                product6,
                product6.getProductId(),
                10,
                6.51,
                21.99,
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1)
        );
        inventoryItem = inventoryItemRepository.save(inventoryItem);

        Listing listing = new Listing(
                inventoryItem,
                3,
                20.00,
                "more info",
                LocalDateTime.now(),
                null
        );
        listingRepository.save(listing);
        Listing listing2 = new Listing(
                inventoryItem,
                5,
                null,
                "more i",
                LocalDateTime.now(),
                null
        );
        listingRepository.save(listing2);
        Listing listing3 = new Listing(
                inventoryItem,
                3,
                20.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        listingRepository.save(listing3);
        Listing listing4 = new Listing(
                inventoryItem,
                7,
                null,
                "mo i",
                LocalDateTime.now().minusHours(1),
                null
        );
        listingRepository.save(listing4);
        Listing listing5 = new Listing(
                inventoryItem,
                1,
                21.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(4)
        );
        listingRepository.save(listing5);
        Listing listing6 = new Listing(
                inventoryItem,
                1,
                5.00,
                "moe i",
                LocalDateTime.now().minusHours(2),
                LocalDateTime.now().plusHours(2)
        );
        listingRepository.save(listing6);
    }
}
