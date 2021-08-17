package org.seng302.business.listing;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.repository.*;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.InventoryItem;
import org.seng302.model.Product;
import org.seng302.controller.ListingResource;
import org.seng302.Main;
import org.seng302.model.Listing;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * ListingResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ListingResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private InventoryItemRepository inventoryItemRepository;

    @MockBean
    private ListingRepository listingRepository;

    @MockBean
    private SoldListingRepository soldListingRepository;

    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @MockBean
    private SoldListingNotificationRepository soldListingNotificationRepository;

    @MockBean
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private MockHttpServletResponse response;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Business adminBusiness;

    private Product product;

    private Product adminProduct;

    private InventoryItem inventoryItem;

    private InventoryItem adminInventoryItem;

    private Listing listing;

    private Listing adminListing;

    private SoldListing soldListing;

    private ListingNotification listingNotification;

    private final String listingPayload = "{\"inventoryItemId\":\"%s\"," +
                                                "\"quantity\":%d," +
                                                "\"price\":%.1f," +
                                                "\"moreInfo\":\"%s\"," +
                                                "\"closes\":\"%s\"}";

    private String expectedJSON;

    private final String expectedListingsJSON = "[" +
                                            "{\"id\":%s," +
                                            "\"inventoryItem\":" +
                                                "{\"id\":%s," +
                                                "\"product\":{" +
                                                    "\"id\":\"%s\"," +
                                                    "\"name\":\"%s\"," +
                                                    "\"description\":\"%s\"," +
                                                    "\"manufacturer\":\"%s\"," +
                                                    "\"recommendedRetailPrice\":%.1f," +
                                                    "\"created\":\"%s\"," +
                                                    "\"images\":[]," +
                                                    "\"business\":{" +
                                                    "\"id\":%d," +
                                                    "\"administrators\":" +
                                                        "[{\"id\":%d," +
                                                        "\"firstName\":\"%s\"," +
                                                        "\"lastName\":\"%s\"," +
                                                        "\"middleName\":\"%s\"," +
                                                        "\"nickname\":\"%s\"," +
                                                        "\"bio\":\"%s\"," +
                                                        "\"email\":\"%s\"," +
                                                        "\"created\":\"%s\"," +
                                                        "\"role\":\"%s\"," +
                                                        "\"businessesAdministered\":[null]," +
                                                        "\"dateOfBirth\":\"%s\"," +
                                                        "\"phoneNumber\":\"%s\"," +
                                                        "\"homeAddress\":{\"streetNumber\":\"%s\",\"streetName\":\"%s\",\"suburb\":\"%s\",\"city\":\"%s\",\"region\":\"%s\",\"country\":\"%s\",\"postcode\":\"%s\"}}]," +
                                                    "\"primaryAdministratorId\":%d," +
                                                    "\"name\":\"%s\"," +
                                                    "\"description\":\"%s\"," +
                                                    "\"address\":%s," +
                                                    "\"businessType\":\"%s\"," +
                                                    "\"created\":\"%s\"}," +
                                                    "\"barcode\":\"%s\"}," +
                                                "\"quantity\":%d," +
                                                "\"pricePerItem\":%.1f," +
                                                "\"totalPrice\":%.1f," +
                                                "\"manufactured\":\"%s\"," +
                                                "\"sellBy\":\"%s\"," +
                                                "\"bestBefore\":\"%s\"," +
                                                "\"expires\":\"%s\"}," +
                                            "\"quantity\":%d," +
                                            "\"price\":%.1f," +
                                            "\"moreInfo\":\"%s\"," +
                                            "\"created\":\"%s\"," +
                                            "\"closes\":\"%s\"," +
                                            "\"isBookmarked\":%s," +
                                            "\"totalBookmarks\":%d" +
                                            "}]";

    private final String expectedListingJSON = "{\"id\":%d," +
            "\"inventoryItem\":" +
            "{\"id\":%d," +
            "\"product\":{" +
            "\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.1f," +
            "\"created\":\"%s\"," +
            "\"images\":[]," +
            "\"business\":{" +
            "\"id\":%d," +
            "\"administrators\":" +
            "[{\"id\":%d," +
            "\"firstName\":\"%s\"," +
            "\"lastName\":\"%s\"," +
            "\"middleName\":\"%s\"," +
            "\"nickname\":\"%s\"," +
            "\"bio\":\"%s\"," +
            "\"email\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"role\":\"%s\"," +
            "\"businessesAdministered\":[null]," +
            "\"dateOfBirth\":\"%s\"," +
            "\"phoneNumber\":\"%s\"," +
            "\"homeAddress\":{\"streetNumber\":\"%s\",\"streetName\":\"%s\",\"suburb\":\"%s\",\"city\":\"%s\",\"region\":\"%s\",\"country\":\"%s\",\"postcode\":\"%s\"}}]," +
            "\"primaryAdministratorId\":%d," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"address\":%s," +
            "\"businessType\":\"%s\"," +
            "\"created\":\"%s\"}," +
            "\"barcode\":\"%s\"}," +
            "\"quantity\":%d," +
            "\"pricePerItem\":%.1f," +
            "\"totalPrice\":%.1f," +
            "\"manufactured\":\"%s\"," +
            "\"sellBy\":\"%s\"," +
            "\"bestBefore\":\"%s\"," +
            "\"expires\":\"%s\"}," +
            "\"quantity\":%d," +
            "\"price\":%.1f," +
            "\"moreInfo\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"closes\":\"%s\"," +
            "\"isBookmarked\":%s," +
            "\"totalBookmarks\":%d" +
            "}";

    private final String expectedBookMarkStatusPayload = "{" +
            "\"bookmarked\":%s" +
            "}";

    @BeforeAll
    void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        dGAA = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dGAA.setId(1);
        dGAA.setSessionUUID(User.generateSessionUUID());
        gAA = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gAA.setId(2);
        gAA.setSessionUUID(User.generateSessionUUID());
        user = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(3);
        user.setSessionUUID(User.generateSessionUUID());
        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(4);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user
        );
        business.setId(1);

        anotherBusiness = new Business(
                user.getId(),
                "anotherName",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user
        );
        anotherBusiness.setId(2);
        user.setBusinessesAdministeredObjects(List.of(business, anotherBusiness));

        product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        inventoryItem = new InventoryItem(
                product,
                "PROD",
                20,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );
        inventoryItem.setId(1);

        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        listing = new Listing(
                inventoryItem,
                3,
                10.5,
                "more info",
                LocalDateTime.now().minusDays(10),
                dateTime
        );
        listing.setId(1);

        soldListing = new SoldListing(business, anotherUser, listing.getCreated(),
                                        product.getProductId(), listing.getQuantity(),
                                        listing.getPrice(), listing.getTotalBookmarks());

        listingNotification = new ListingNotification("Listing notification");

        adminBusiness = new Business(
                dGAA.getId(),
                "name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                dGAA
        );
        adminBusiness.setId(3);
        dGAA.setBusinessesAdministeredObjects(List.of(adminBusiness));

        adminProduct = new Product(
                "PROD",
                adminBusiness,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        adminInventoryItem = new InventoryItem(
                adminProduct,
                "PROD",
                20,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );
        adminInventoryItem.setId(1);

        adminListing = new Listing(
                adminInventoryItem,
                3,
                10.5,
                "more info",
                LocalDateTime.now().minusDays(10),
                dateTime
        );
        adminListing.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository, soldListingRepository, listingNotificationRepository, soldListingNotificationRepository, bookmarkedListingMessageRepository))
                .build();
    }

    /**
     * Tests that a Created status is return if the user is a business administrator for endpoint
     * /businesses/{id}/listings
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void canCreateListingWhenBusinessExistsAndDataValidWithBusinessAdministratorUserCookie() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.ofNullable(product));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(inventoryItem));

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                                    newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a Bad Request status is returned if the create business data in the payload is invalid at endpoint
     * /businesses/{id}/listings
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void canCreateListingWhenBusinessExistsAndDataInvalidWithBusinessAdministratorUserCookie() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.ofNullable(product));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.empty());

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a Not Acceptable status is returned if the business doesn't exist at ID in the
     * /businesses/{id}/listings endpoint.
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void cantCreateListingWhenBusinessDoesntExistButDataValid() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.empty());

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that an Unauthorized status is returned if the user doesn't have a JSESSIONID at endpoint
     * /businesses/{id}/listings
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void canCreateListingWhenBusinessExistsAndDataValidWithoutUserCookie() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.ofNullable(product));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(inventoryItem));

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a created status is returned if the user is a GAA but not a business administrator for endpoint
     * /businesses/{id}/listings
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void canCreateListingWhenBusinessExistsAndDataValidWithUserCookieGAA() throws Exception {
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(gAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(inventoryItem));

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a created status is returned if the user is a DGAA but not a business administrator for endpoint
     * /businesses/{id}/listings
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void canCreateListingWhenBusinessExistsAndDataValidWithUserCookieDGAA() throws Exception {
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(inventoryItem));

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a listing creation payload to the
     * /businesses/{id}/listings API endpoint that contains invalid data and an existing business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateListingWhenBusinessExistsButDataIsInvalid() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(inventoryItem));

        String json = String.format(listingPayload, inventoryItem.getId(), 0, 0.0, "info", null);

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that an Unauthorized status is returned if the JSESSIONID is invalid at
     * /businesses/{id}/listings Api endpoint
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void cantCreateListingWhenBusinessExistsAndDataValidWithInvalidUserCookie() throws Exception {
        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", "0")))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a Forbidden status is returned if the user is not an admin of business at
     * /businesses/{id}/listings Api endpoint
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void cantCreateListingWhenBusinessExistsAndDataValidWithNoCookie() throws Exception {
        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a Forbidden status is returned if the user is not an admin of business at
     * /businesses/{id}/listings Api endpoint
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void cantCreateListingWhenBusinessExistsAndDataValidWithNonAdminUserCookie() throws Exception {
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        Listing newListing = new Listing(
                inventoryItem,
                10,
                null,
                "info",
                LocalDateTime.now(),
                null
        );

        String json = String.format(listingPayload, newListing.getInventoryItem().getId(), newListing.getQuantity(),
                newListing.getPrice(), newListing.getMoreInfo(), newListing.getCloses());

        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    // GET Tests

    /**
     * Tests that an OK status and a list of listing payloads are received when the business ID in the
     * /businesses/{id}/listings API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveListingsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("closes").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(listingRepository.findListingsByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status and a list of listing payloads are received when the business ID in the
     * /businesses/{id}/listings API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is not an administrator of the business
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveListingsWhenBusinessExistsWithUserCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("closes").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(listingRepository.findListingsByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received if the business ID in
     * /businesses/{id}/listings endpoint does not exist
     * @throws Exception thrown if there is an error with MockMVC.
     */
    @Test
    void cantRetrieveListingsWhenBusinessDoesntExistWithUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(0)).willReturn(Optional.empty());

        expectedJSON = "";

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(listingRepository.findListingsByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings", 0))
                .param("orderBy", "createdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status and no payloads are received when there is no JSESSIONID for
     * /businesses/{id}/listings API endpoint
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingsWhenBusinessExistsWithoutUserCookie() throws Exception {
        // given
        expectedJSON = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "createdASC")
                .param("page", "0"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status and no payloads are received when there is an invalid JSESSIONID for
     * /businesses/{id}/listings API endpoint.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingsWhenBusinessExistsWithInvalidUserCookie() throws Exception {
        // given
        expectedJSON = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "createdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", "0")))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status and a list of listing payloads is received when the business ID in the
     * /businesses/{id}/listings API endpoint exists.
     * Test specifically for when the order by and page params provided are valid.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveListingsWhenBusinessExistsWithValidOrderByAndPageParams() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("closes").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(listingRepository.findListingsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "closesASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a BAD_REQUEST status and no listing payloads are received when the business ID in the
     * /businesses/{id}/listings API endpoint exists but the order by param is invalid.
     * Test specifically for when the order by param provided is invalid.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingsWhenBusinessExistsWithInvalidOrderByParam() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJSON = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "a")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a BAD_REQUEST status and no listing payloads are received when the business ID in the
     * /businesses/{id}/listings API endpoint exists but the page param is invalid.
     * Test specifically for when the page param provided is invalid.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingsWhenBusinessExistsWithInvalidPageParam() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJSON = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "closesASC")
                .param("page", "a")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    // GET listings search tests

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using the
     * listing name.
     * Test specifically for when the user searching for a listing as a DGAA.
     */
    @Test
    void canSearchListingsByNameWhenListingExistsWithDgaaCookieTest() throws Exception {
        // given
        String searchQuery = "Beans";
        List<String> names = Arrays.asList(searchQuery);

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using the
     * listing name.
     * Test specifically for when the order by and page params provided are valid.
     */
    @Test
    void canSearchListingsWhenListingExistsWithValidOrderByAndPageParamsTest() throws Exception {
        // given
        String searchQuery = "Beans";
        List<String> names = Arrays.asList(searchQuery);

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("orderBy", "productNameASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using the
     * listing name.
     * Test specifically for when the user searching for a listing as a USER.
     */
    @Test
    void canSearchListingsByNameWhenListingExistsWithUserCookieTest() throws Exception {
        // given
        String searchQuery = "Beans";
        List<String> names = Arrays.asList(searchQuery);

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests for an OK status but an empty response is received when searching for a listing that does not exist using
     * the /listings API endpoint. The listing (that does not exist by the name searched for) is searched for
     * using the listing name.
     */
    @Test
    void emptySearchListingsByNameWhenListingDoesntExistTest() throws Exception {
        // given
        String searchQuery = "PRODUCT";
        List<String> names = Arrays.asList(searchQuery);

        expectedJSON = "[]";

        // when
        List<Listing> list = List.of();
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a BAD_REQUEST status is received when searching for a listing using the /listings API endpoint
     * when the order by param is invalid.
     * Test specifically for when the order by param provided is invalid.
     */
    @Test
    void cantSearchListingsWithInvalidOrderByParam() throws Exception {
        // given
        String searchQuery = "Beans";

        expectedJSON = "";

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("orderBy", "b")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a BAD_REQUEST status is received when searching for a listing using the /listings API endpoint
     * when the page param is invalid.
     * Test specifically for when the page param provided is invalid.
     */
    @Test
    void cantSearchListingsWithInvalidPageParam() throws Exception {
        // given
        String searchQuery = "Beans";

        expectedJSON = "";

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("orderBy", "productNameASC")
                .param("page", "b")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when searching for a listing using the /listings API endpoint
     * when the cookie contains a non-existing ID.
     */
    @Test
    void cantSearchListingsWithNonExistingIdCookie() throws Exception {
        // given
        String searchQuery = "Beans";

        expectedJSON = "";

        // when
        when(userRepository.findBySessionUUID("1")).thenReturn(Optional.empty());

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status is received when searching for a listing using the /listings API endpoint
     * when there is no cookie.
     */
    @Test
    void cantSearchListingsWithNoCookie() throws Exception {
        // given
        String searchQuery = "Beans";

        expectedJSON = "";

        // when
        response = mvc.perform(get("/listings").param("searchQuery", searchQuery))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for listings using the /listings API endpoint
     * and that the JSON response is equal to the listings searched for. The listings are searched for using business
     * type.
     * Test specifically for when searching only by business type.
     */
    @Test
    void canSearchListingsByTypeWhenListingExistsTest() throws Exception {
        // given
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        String businessType = "ACCOMMODATION_AND_FOOD_SERVICES";
        BusinessType convertedBusinessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, List.of(convertedBusinessType), null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("businessTypes", businessType)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using business
     * type and listing name.
     * Test specifically for when searching by business type and listing name.
     */
    @Test
    void canSearchListingsByNameAndTypeWhenListingExistsTest() throws Exception {
        // given
        String searchQuery = "Beans";
        List<String> names = Arrays.asList(searchQuery);

        String businessType = "ACCOMMODATION_AND_FOOD_SERVICES";
        BusinessType convertedBusinessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, List.of(convertedBusinessType), null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("businessTypes", businessType)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using multiple business
     * types and listing name.
     * Test specifically for when searching by business type and listing name.
     */
    @Test
    void canSearchListingsByNameAndMultipleTypesWhenListingExistsTest() throws Exception {
        // given
        String searchQuery = "Beans";
        List<String> names = Arrays.asList(searchQuery);

        String businessType1 = "ACCOMMODATION_AND_FOOD_SERVICES";
        String businessType2 = "RETAIL_TRADE";
        BusinessType convertedBusinessType1 = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;
        BusinessType convertedBusinessType2 = BusinessType.RETAIL_TRADE;

        expectedJSON = "[" + String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks()) + "," + String.format(expectedListingJSON, adminListing.getId(),
                adminInventoryItem.getId(), adminProduct.getProductId(), adminProduct.getName(), adminProduct.getDescription(), adminProduct.getManufacturer(),
                adminProduct.getRecommendedRetailPrice(), adminProduct.getCreated(), adminBusiness.getId(), dGAA.getId(), dGAA.getFirstName(),
                dGAA.getLastName(), dGAA.getMiddleName(), dGAA.getNickname(), dGAA.getBio(), dGAA.getEmail(), dGAA.getCreated(), dGAA.getRole(),
                dGAA.getDateOfBirth(), dGAA.getPhoneNumber(), dGAA.getHomeAddress().getStreetNumber(), dGAA.getHomeAddress().getStreetName(),
                dGAA.getHomeAddress().getSuburb(), dGAA.getHomeAddress().getCity(), dGAA.getHomeAddress().getRegion(), dGAA.getHomeAddress().getCountry(),
                dGAA.getHomeAddress().getPostcode(), adminBusiness.getPrimaryAdministratorId(), adminBusiness.getName(),
                adminBusiness.getDescription(), adminBusiness.getAddress(), adminBusiness.getBusinessType(), adminBusiness.getCreated(),
                adminProduct.getBarcode(), adminInventoryItem.getQuantity(), adminInventoryItem.getPricePerItem(), adminInventoryItem.getTotalPrice(),
                adminInventoryItem.getManufactured(), adminInventoryItem.getSellBy(), adminInventoryItem.getBestBefore(), adminInventoryItem.getExpires(),
                adminListing.getQuantity(), adminListing.getPrice(), adminListing.getMoreInfo(), adminListing.getCreated().toString(), adminListing.getCloses().toString(),
                adminListing.isBookmarked(user), adminListing.getTotalBookmarks()) + "]";

        // when
        List<Listing> list = List.of(listing, adminListing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, List.of(convertedBusinessType1, convertedBusinessType2), null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("businessTypes", businessType1, businessType2)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using business
     * type, business name and filters.
     * Test specifically for when searching by business type, business name and filters.
     */
    @Test
    void canSearchListingsByBusinessNameAndTypeAndFiltersWhenListingExistsTest() throws Exception {
        // given
        String searchQuery = "name";
        List<String> names = Arrays.asList(searchQuery);

        String businessType = "ACCOMMODATION_AND_FOOD_SERVICES";
        BusinessType convertedBusinessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.business.address.country").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByBusinessName(
                names, paging, List.of(convertedBusinessType),
                10.0, 11.0,
                LocalDateTime.of(2021, 1, 1, 0, 0),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("searchType", "businessName")
                .param("orderBy", "countryASC")
                .param("businessTypes", businessType)
                .param("minimumPrice", "10.0")
                .param("maximumPrice", "11.0")
                .param("fromDate", "2021-01-01T00:00")
                .param("toDate", "2022-01-01T00:00")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status is received when searching for a listing using the /listings API endpoint
     * and that the JSON response is equal to the listing searched for. The listing is searched for using business
     * type, location and filters.
     * Test specifically for when searching by business type, location and filters.
     */
    @Test
    void canSearchListingsByLocationAndBusinessTypeAndFiltersWhenListingExistsTest() throws Exception {
        // given
        String searchQuery = "Christchurch";
        List<String> names = Arrays.asList(searchQuery);

        String businessType = "ACCOMMODATION_AND_FOOD_SERVICES";
        BusinessType convertedBusinessType = BusinessType.ACCOMMODATION_AND_FOOD_SERVICES;

        expectedJSON = String.format(expectedListingsJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.expires").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByLocation(
                names, paging, List.of(convertedBusinessType),
                9.0, 12.0,
                LocalDateTime.of(2020, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0)
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("searchType", "location")
                .param("orderBy", "expiryDateASC")
                .param("businessTypes", businessType)
                .param("minimumPrice", "9.0")
                .param("maximumPrice", "12.0")
                .param("fromDate", "2020-01-01T00:00")
                .param("toDate", "2023-01-01T00:00")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status and a listing payload is received when the business and listing IDs in the
     * /businesses/{businessId}/listings/{listingId} API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveListingWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findListingByBusinessIdAndId(business.getId(), listing.getId())).willReturn(Optional.ofNullable(listing));

        expectedJSON = String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(user), listing.getTotalBookmarks());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an OK status and a listing payload is received when the business and listing IDs in the
     * /businesses/{businessId}/listings/{listingId} API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is not an administrator of the business
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveListingWhenBusinessExistsWithUserCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findListingByBusinessIdAndId(business.getId(), listing.getId())).willReturn(Optional.ofNullable(listing));

        expectedJSON = String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(anotherUser), listing.getTotalBookmarks());



        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a BAD_REQUEST status is received if the listing ID in
     * /businesses/{businessId}/listings/{listingId} endpoint does not exist but the business ID does.
     * @throws Exception An Exception
     */
    @Test
    void cantRetrieveListingWhenBusinessExistsButListingDoesNotExistWithUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findListingByBusinessIdAndId(business.getId(), listing.getId())).willReturn(Optional.empty());

        expectedJSON = "";

        // when

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received if the business ID in
     * /businesses/{businessId}/listings/{listingId} endpoint does not exist
     * @throws Exception An Exception
     */
    @Test
    void cantRetrieveListingWhenBusinessDoesntExistWithUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(0)).willReturn(Optional.empty());

        expectedJSON = "";

        // when

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status and no payload is received when there is no JSESSIONID for
     * /businesses/{businessId}/listings/{listingId} endpoint
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingWhenBusinessExistsWithoutUserCookie() throws Exception {
        // given
        expectedJSON = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests that an UNAUTHORIZED status and no payload is received when there is an invalid JSESSIONID for
     * /businesses/{businessId}/listings/{listingId} API endpoint.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveListingWhenBusinessExistsWithInvalidUserCookie() throws Exception {
        // given
        expectedJSON = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", "0")))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Test that an OK status return and bookmark states will be change to true when user has not bookmarked given listing.
     *
     * @throws Exception Exception error
     */
    @Test
    void canChangeListingBookmarkState_WhenUserNotMarkedGivenListingBefore() throws Exception {
        // given
        expectedJSON = String.format(expectedBookMarkStatusPayload, "true");
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%s/bookmark", listing.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Test that an OK status return and bookmark states will be change to false when user marked given listing.
     *
     * @throws Exception Exception error
     */
    @Test
    void canChangeListingBookmarkState_WhenUserMarkedGivenListing() throws Exception {
        // given
        expectedJSON = String.format(expectedBookMarkStatusPayload, "false");
        listing.addUserToANewBookmark(user);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%s/bookmark", listing.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Test that an UNAUTHORIZED status return and bookmark states will not be change when user not login (or miss
     * section token).
     *
     * @throws Exception Exception error
     */
    @Test
    void canNotChangeListingBookmarkState_WhenUserNotLogin() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.empty());
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%s/bookmark", listing.getId())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test that a NOT_ACCEPTABLE status return and bookmark states will not be change when given listing not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    void canNotChangeListingBookmarkState_WhenListingNotExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(put(String.format("/listings/%s/bookmark", listing.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that when buying a listing as a DGAA then an OK status is received, two listing notifications are created,
     * a sold listing is created, inventory item quantity is updated, and the listing is deleted.
     *
     * @throws Exception Exception error
     */
    @Test
    void canBuyListing_WhenUserIsDgaa() throws Exception {
        // given
        given(userRepository.findBySessionUUID(dGAA.getSessionUUID())).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(inventoryItem.getId())).willReturn(Optional.ofNullable(inventoryItem));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        when(soldListingRepository.save(any(SoldListing.class))).thenReturn(soldListing);
        when(listingNotificationRepository.save(any(ListingNotification.class))).thenReturn(listingNotification);
        when(inventoryItemRepository.save(inventoryItem)).thenReturn(inventoryItem);
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that when buying a listing as a User then an OK status is received, two listing notifications are created,
     * a sold listing is created, inventory item quantity is updated, and the listing is deleted.
     *
     * @throws Exception Exception error
     */
    @Test
    void canBuyListing_WhenUserAndNotBusinessAdministrator() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(inventoryItem.getId())).willReturn(Optional.ofNullable(inventoryItem));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        when(soldListingRepository.save(any(SoldListing.class))).thenReturn(soldListing);
        when(listingNotificationRepository.save(any(ListingNotification.class))).thenReturn(listingNotification);
        when(inventoryItemRepository.save(inventoryItem)).thenReturn(inventoryItem);
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that when buying a listing when not logged in, an UNAUTHORIZED status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenNoCookie() throws Exception {
        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test that when buying a listing as a DGAA and an administrator of the business, a FORBIDDEN status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenDgaaAndBusinessAdministrator() throws Exception {
        // given
        given(userRepository.findBySessionUUID(dGAA.getSessionUUID())).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(adminBusiness.getId())).willReturn(Optional.ofNullable(adminBusiness));
        given(listingRepository.findById(adminListing.getId())).willReturn(Optional.ofNullable(adminListing));

        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", adminListing.getId()))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getErrorMessage()).isEqualTo("Cannot purchase your own listing");
    }

    /**
     * Test that when buying a listing as a User and an administrator of the business, a FORBIDDEN status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenUserAndBusinessAdministrator() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getErrorMessage()).isEqualTo("Cannot purchase your own listing");
    }

    /**
     * Test that when buying a listing when a listing with the given ID doesn't exist then a NOT_ACCEPTABLE status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenListingDoesNotExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Listing does not exist");
    }

    /**
     * Test that when buying a listing when the business the listing belongs to doesn't exist then an
     * INTERNAL_SERVER_ERROR status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenListingBusinessDoesNotExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getErrorMessage()).isEqualTo("Business for listing does not exist");
    }

    /**
     * Test that when buying a listing when the inventory item the listing is for to doesn't exist then an
     * INTERNAL_SERVER_ERROR status is received.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotBuyListing_WhenListingInventoryItemDoesNotExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(inventoryItemRepository.findInventoryItemById(inventoryItem.getId())).willReturn(Optional.empty());
        given(listingRepository.findById(listing.getId())).willReturn(Optional.ofNullable(listing));

        // when
        response = mvc.perform(put(String.format("/listings/%d/buy", listing.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getErrorMessage()).isEqualTo("Inventory item for listing does not exist");
    }

    /**
     * Test that when a user has already bookmarked and/or un-bookmarked a listing, they can retrieve
     * all BookmarkedListingMessages.
     * Tests specifically for when searching as a standard user (non-admin).
     */
    @Test
    void canRetrieveBookmarkedListingMessagesWithMessages() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        LocalDateTime created = LocalDateTime.now();
        BookmarkedListingMessage bookmarkedListingMessage1 = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked. ",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage1.setId(1);
        BookmarkedListingMessage bookmarkedListingMessage2 = new BookmarkedListingMessage(
                String.format("Bookmark for product listing '%s' has been removed.",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage2.setId(2);
        user.setBookmarkedListingMessages(List.of(bookmarkedListingMessage1, bookmarkedListingMessage2));

        expectedJSON = "[{" +
                "\"id\":" + bookmarkedListingMessage1.getId() + "," +
                "\"description\":\"" + bookmarkedListingMessage1.getDescription() + "\"," +
                "\"created\":\"" + bookmarkedListingMessage1.getCreated() + "\"," +
                "\"listingId\":" + bookmarkedListingMessage1.getListing().getId() + "," +
                "\"businessId\":" + bookmarkedListingMessage1.getListing().getBusinessId() + "," +
                "\"closes\":\"" + bookmarkedListingMessage1.getListing().getCloses() + "\"" +
                "}," + "{" +
                "\"id\":" + bookmarkedListingMessage2.getId() + "," +
                "\"description\":\"" + bookmarkedListingMessage2.getDescription() + "\"," +
                "\"created\":\"" + bookmarkedListingMessage2.getCreated() + "\"," +
                "\"listingId\":" + bookmarkedListingMessage1.getListing().getId() + "," +
                "\"businessId\":" + bookmarkedListingMessage1.getListing().getBusinessId() + "," +
                "\"closes\":\"" + bookmarkedListingMessage1.getListing().getCloses() + "\"" +
                "}]";

        // when
        response = mvc.perform(get("/home/bookmarkMessages")
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Test that when a user has not already bookmarked and/or un-bookmarked a listing, they can retrieve
     * all BookmarkedListingMessages (resulting in an empty list returned)
     * Tests specifically for when searching as a standard user (non-admin).
     */
    @Test
    void canRetrieveBookmarkedListingMessagesWithNoMessages() throws Exception {
        // given
        given(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).willReturn(Optional.ofNullable(anotherUser));

        expectedJSON = "[]";

        // when
        response = mvc.perform(get("/home/bookmarkMessages")
                        .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);

    }

    /**
     * Tests that a 200 is returned when the user tries to delete their own message
     * @throws Exception
     */
    @Test
    void canDeleteBookmarkMessageWithExistingMessage() throws Exception {
        // given
        LocalDateTime created = LocalDateTime.now();

        BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked. ",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage.setId(1);
        bookmarkedListingMessage.addUser(user);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(bookmarkedListingMessageRepository.findById(1)).willReturn(Optional.of(bookmarkedListingMessage));

        // when
        response = mvc.perform(delete(String.format("/home/bookmarkMessages/%d", 1))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a 200 is returned when a GAA tries to delete another users message
     * @throws Exception exception
     */
    @Test
    void canDeleteBookmarkMessageWithExistingMessageWhenGAA() throws Exception {
        // given
        LocalDateTime created = LocalDateTime.now();

        BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked. ",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage.setId(1);
        bookmarkedListingMessage.addUser(user);

        given(userRepository.findBySessionUUID(gAA.getSessionUUID())).willReturn(Optional.of(gAA));
        given(bookmarkedListingMessageRepository.findById(1)).willReturn(Optional.of(bookmarkedListingMessage));

        // when
        response = mvc.perform(delete(String.format("/home/bookmarkMessages/%d", 1))
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a 406 is returned when the user tries to delete a non-existing message
     * @throws Exception exception
     */
    @Test
    void cantDeleteBookmarkMessageThatDoesntExist() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(bookmarkedListingMessageRepository.findById(1)).willReturn(Optional.empty());

        // when
        response = mvc.perform(delete(String.format("/home/bookmarkMessages/%d", 1))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that a 403 is returned when the user tries to delete another users message
     * @throws Exception exception
     */
    @Test
    void cantDeleteOtherUsersBookmarkMessageWithExistingMessage() throws Exception {
        // given
        LocalDateTime created = LocalDateTime.now();

        BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked. ",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage.setId(1);
        bookmarkedListingMessage.addUser(anotherUser);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(bookmarkedListingMessageRepository.findById(1)).willReturn(Optional.of(bookmarkedListingMessage));

        // when
        response = mvc.perform(delete(String.format("/home/bookmarkMessages/%d", 1))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests that a 401 is returned when the user is not logged in
     * @throws Exception exception
     */
    @Test
    void cantDeleteBookmarkMessageWithExistingMessageWhenNotLoggedIn() throws Exception {
        // given
        LocalDateTime created = LocalDateTime.now();

        BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked. ",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage.setId(1);
        bookmarkedListingMessage.addUser(user);

        given(bookmarkedListingMessageRepository.findById(1)).willReturn(Optional.of(bookmarkedListingMessage));

        // when
        response = mvc.perform(delete(String.format("/home/bookmarkMessages/%d", 1)))
                .andReturn().getResponse();
        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

}
