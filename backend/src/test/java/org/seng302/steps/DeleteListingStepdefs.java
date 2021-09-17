package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.assertj.core.api.Assertions.assertThat;

import org.seng302.controller.ListingResource;
import org.seng302.controller.MarketplaceCardResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class DeleteListingStepdefs {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private BusinessRepository businessRepository;

    @Autowired
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    @MockBean
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    @MockBean
    private ListingRepository listingRepository;

    @Autowired
    @MockBean
    private SoldListingRepository soldListingRepository;

    @Autowired
    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @Autowired
    @MockBean
    private SoldListingNotificationRepository soldListingNotificationRepository;

    @Autowired
    @MockBean
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private MockHttpServletResponse response;

    private User dGAA;

    private User gAA;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        listingRepository = mock(ListingRepository.class);
        inventoryItemRepository = mock(InventoryItemRepository.class);
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        soldListingRepository = mock(SoldListingRepository.class);
        listingNotificationRepository = mock(ListingNotificationRepository.class);
        soldListingNotificationRepository = mock(SoldListingNotificationRepository.class);
        bookmarkedListingMessageRepository = mock(BookmarkedListingMessageRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository,
                soldListingRepository, listingNotificationRepository, soldListingNotificationRepository, bookmarkedListingMessageRepository))
                .build();
    }

    @Given("The {string} business has id {int} and products and inventory items")
    public void theBusinessHasProductsAndInventoryItems(String businessName, Integer id) throws Exception{
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

        business = new Business(
                user.getId(),
                businessName,
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        business.setId(id);
        user.setBusinessesAdministeredObjects(List.of(business));

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
    }

    @Given("The business with {int} and I have a listing with id {int}")
    public void theBusinessWithICanDeleteListing(int businessId, int listingId) throws Exception {

        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        listing = new Listing(
                inventoryItem,
                3,
                10.5,
                "more info",
                LocalDateTime.now().minusDays(10),
                dateTime
        );
        listing.setId(listingId);
        listing.setBusinessId(businessId);
    }

    @When("As an admin I delete the listing with id {int}")
    public void asAnAdminIDeleteTheListingWithId(int listingId) throws Exception {
        Cookie session = new Cookie("JSESSIONID", user.getSessionUUID());
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        when(listingRepository.findListingByBusinessIdAndId(business.getId(), listingId)).thenReturn(Optional.of(listing));

        response = mvc.perform(delete(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId())).cookie(session)).andReturn().getResponse();
    }

    @When("As an GAA I delete the listing with id {int}")
    public void asAnGaaIDeleteTheListingWithId(int listingId) throws Exception {
        Cookie session = new Cookie("JSESSIONID", gAA.getSessionUUID());
        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.of(gAA));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        when(listingRepository.findListingByBusinessIdAndId(business.getId(), listingId)).thenReturn(Optional.of(listing));

        response = mvc.perform(delete(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId())).cookie(session)).andReturn().getResponse();
    }

    @When("As an DGAA I delete the listing with id {int}")
    public void asAnDgaaIDeleteTheListingWithId(int listingId) throws Exception {
        Cookie session = new Cookie("JSESSIONID", dGAA.getSessionUUID());
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        when(listingRepository.findListingByBusinessIdAndId(business.getId(), listingId)).thenReturn(Optional.of(listing));

        response = mvc.perform(delete(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId())).cookie(session)).andReturn().getResponse();
    }

    @Then("An OK response is returned")
    public void anOkResponseIsReturned() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
