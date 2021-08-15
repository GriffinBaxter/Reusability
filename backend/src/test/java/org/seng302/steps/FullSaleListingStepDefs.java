package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.InventoryItemResource;
import org.seng302.controller.ListingResource;
import org.seng302.exceptions.IllegalBookmarkedListingMessageArgumentException;
import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FullSaleListingStepDefs extends CucumberSpringConfiguration {

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
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    private MockHttpServletResponse response;

    private String listingPayloadJSON;

    private BookmarkedListingMessage bookmarkedListingMessage1;

    private BookmarkedListingMessage bookmarkedListingMessage2;

    @Before
    public void createMockMvc() {
        listingRepository = mock(ListingRepository.class);
        inventoryItemRepository = mock(InventoryItemRepository.class);
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        soldListingRepository = mock(SoldListingRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository, soldListingRepository, listingNotificationRepository, bookmarkedListingMessageRepository)).build();
    }

    @Given("I am logged in as a business administrator.")
    public void i_am_logged_in_as_a_business_administrator() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        user = new User(
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
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

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
        product = new Product(
                "WATT-420-BEANS",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.99,
                "9400547002634"
        );
        inventoryItem = new InventoryItem(
                product,
                "WATT-420-BEANS",
                100,
                20.99,
                2099.00,
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1)
        );
        user.setBusinessesAdministeredObjects(List.of(business));
        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(listingRepository, inventoryItemRepository, productRepository,
                    businessRepository, userRepository, soldListingRepository, listingNotificationRepository, bookmarkedListingMessageRepository)).build();
    }

    @Given("I have a listing with quantity {int}, price {double}, closing date {string}, and {string} in the more-info section.")
    public void i_have_a_listing_with_quantity_price_closing_date_and_in_the_more_info_section(Integer quantity, Double price, String closingDate, String moreInfo) throws IllegalListingArgumentException {

        LocalDateTime created = LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0));
        listing = new Listing(
                inventoryItem,
                quantity,
                price,
                moreInfo,
                created,
                LocalDateTime.parse(closingDate));

        listingPayloadJSON = "{" +
            "\"id\":" + listing.getId() + "," +
            "\"inventoryItem\":" +
                "{\"id\":" + inventoryItem.getId() + "," +
                "\"product\":" +
                    "{\"id\":\"" + product.getProductId() + "\"," +
                    "\"name\":\"" + product.getName() + "\"," +
                    "\"description\":\"" + product.getDescription() + "\"," +
                    "\"manufacturer\":\"" + product.getManufacturer() + "\"," +
                    "\"recommendedRetailPrice\":" + product.getRecommendedRetailPrice() + "," +
                    "\"created\":\"" + product.getCreated() + "\"," +
                    "\"images\":[]," +
                    "\"business\":" +
                        "{\"id\":" + business.getId() + "," +
                        "\"administrators\":" +
                            "[{\"id\":" + user.getId() + "," +
                            "\"firstName\":\"" + user.getFirstName() + "\"," +
                            "\"lastName\":\"" + user.getLastName() + "\"," +
                            "\"middleName\":\"" + user.getMiddleName() + "\"," +
                            "\"nickname\":\"" + user.getNickname() + "\"," +
                            "\"bio\":\"" + user.getBio() + "\"," +
                            "\"email\":\"" + user.getEmail() + "\"," +
                            "\"created\":\"" + user.getCreated() + "\"," +
                            "\"role\":\"" + user.getRole() + "\"," +
                            "\"businessesAdministered\":[null]," +
                            "\"dateOfBirth\":\"" + user.getDateOfBirth() + "\"," +
                            "\"phoneNumber\":\"" + user.getPhoneNumber() + "\"," +
                            "\"homeAddress\":{" +
                                "\"streetNumber\":\"" + user.getHomeAddress().getStreetNumber() + "\"," +
                                "\"streetName\":\"" + user.getHomeAddress().getStreetName() + "\"," +
                                "\"city\":\"" + user.getHomeAddress().getCity() + "\"," +
                                "\"region\":\"" + user.getHomeAddress().getRegion() + "\"," +
                                "\"country\":\"" + user.getHomeAddress().getCountry() + "\"," +
                                "\"postcode\":\"" + user.getHomeAddress().getPostcode() + "\"," +
                                "\"suburb\":\"" + user.getHomeAddress().getSuburb() + "\"}}]," +
                        "\"primaryAdministratorId\":" + business.getPrimaryAdministratorId() + "," +
                        "\"name\":\"" + business.getName() + "\"," +
                        "\"description\":\"" + business.getDescription() + "\"," +
                        "\"address\":" + business.getAddress() + "," +
                        "\"businessType\":\"" + business.getBusinessType() + "\"," +
                        "\"created\":\"" + business.getCreated() + "\"}," +
                    "\"barcode\":\"" + product.getBarcode() + "\"" +
                "}," +
                "\"quantity\":" + inventoryItem.getQuantity() + "," +
                "\"pricePerItem\":" + inventoryItem.getPricePerItem() + "," +
                "\"totalPrice\":" + inventoryItem.getTotalPrice() + "," +
                "\"manufactured\":\"" + inventoryItem.getManufactured() + "\"," +
                "\"sellBy\":\"" + inventoryItem.getSellBy() + "\"," +
                "\"bestBefore\":\"" + inventoryItem.getBestBefore() + "\"," +
                "\"expires\":\"" + inventoryItem.getExpires() + "\"" +
            "}," +
            "\"quantity\":" + listing.getQuantity() + "," +
            "\"price\":" + listing.getPrice() + "," +
            "\"moreInfo\":\"" + listing.getMoreInfo() + "\"," +
            "\"created\":\"" + listing.getCreated() + "\"," +
            "\"closes\":\"" + listing.getCloses() + "\"," +
            "\"isBookmarked\":" + listing.isBookmarked(user) + "," +
            "\"totalBookmarks\":" + listing.getTotalBookmarks() + "}";

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(listingRepository.findListingByBusinessIdAndId(business.getId(), listing.getId())).willReturn(Optional.ofNullable(listing));

    }

    @When("I request to retrieve my listing.")
    public void i_request_to_retrieve_my_listing() throws Exception {


        response = mvc.perform(get(String.format("/businesses/%d/listings/%d", business.getId(), listing.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

    }

    @Then("My listing is returned with all fields shown.")
    public void my_listing_is_returned_with_all_fields_shown() throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(listingPayloadJSON);
    }

    @Given("I have bookmarked the listing.")
    public void i_have_bookmarked_the_listing() throws IllegalBookmarkedListingMessageArgumentException {
        LocalDateTime created = LocalDateTime.now();
        bookmarkedListingMessage1 = new BookmarkedListingMessage(
                String.format("Product listing '%s' has been bookmarked.",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage1.setId(1);
        user.setBookmarkedListingMessages(List.of(bookmarkedListingMessage1));
        listing.setBookmarkedListings(List.of(user));
    }

    @Given("I have un-bookmarked the listing.")
    public void i_have_un_bookmarked_the_listing() throws IllegalBookmarkedListingMessageArgumentException {
        LocalDateTime created = LocalDateTime.now();
        bookmarkedListingMessage2 = new BookmarkedListingMessage(
                String.format("Bookmark for product listing '%s' has been removed.",
                        listing.getInventoryItem().getProduct().getName()),
                created,
                listing);
        bookmarkedListingMessage2.setId(1);
        user.setBookmarkedListingMessages(List.of(bookmarkedListingMessage2));
        listing.setBookmarkedListings(Collections.emptyList());
    }

    @When("I request to retrieve my list of bookmarked listing messages.")
    public void i_request_to_retrieve_my_list_of_bookmarked_listing_messages() throws Exception {
        response = mvc.perform(get("/home/bookmarkMessages")
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("A bookmark message for the listing is returned.")
    public void a_bookmark_message_for_the_listing_is_returned() throws UnsupportedEncodingException {

        String expectedJSON = "[{" +
                "\"id\":" + bookmarkedListingMessage1.getId() + "," +
                "\"description\":\"" + bookmarkedListingMessage1.getDescription() + "\"," +
                "\"created\":\"" + bookmarkedListingMessage1.getCreated() + "\"," +
                "\"listingId\":" + bookmarkedListingMessage1.getListing().getId() + "," +
                "\"businessId\":" + bookmarkedListingMessage1.getListing().getBusinessId() + "," +
                "\"closes\":\"" + bookmarkedListingMessage1.getListing().getCloses() + "\"" +
                "}]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    @Then("An un-bookmark message for the listing is returned.")
    public void an_un_bookmark_message_for_the_listing_is_returned() throws UnsupportedEncodingException {
        String expectedJSON = "[{" +
                "\"id\":" + bookmarkedListingMessage2.getId() + "," +
                "\"description\":\"" + bookmarkedListingMessage2.getDescription() + "\"," +
                "\"created\":\"" + bookmarkedListingMessage2.getCreated() + "\"," +
                "\"listingId\":" + bookmarkedListingMessage2.getListing().getId() + "," +
                "\"businessId\":" + bookmarkedListingMessage2.getListing().getBusinessId() + "," +
                "\"closes\":\"" + bookmarkedListingMessage2.getListing().getCloses() + "\"" +
                "}]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    @Then("The number of bookmarks the listing has is decremented.")
    public void the_number_of_bookmarks_the_listing_has_is_decremented() {
        assertThat(listing.getTotalBookmarks()).isZero();
    }

}
