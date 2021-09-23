package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.ListingResource;
import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class ListingSearchStepDefs extends CucumberSpringConfiguration {

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
    private  BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private MockHttpServletResponse response;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Product product;
    private Product product2;

    private InventoryItem inventoryItem;
    private InventoryItem inventoryItem2;

    private Listing listing;
    private Listing listing2;

    private String expectedJSON;

    private final String expectedListingJSON =
            "{\"id\":%d," +
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
                    "\"businessesAdministered\":[]," +
                    "\"images\":[]," +
                    "\"dateOfBirth\":\"%s\"," +
                    "\"phoneNumber\":\"%s\"," +
                    "\"homeAddress\":%s}]," +
                    "\"primaryAdministratorId\":%d," +
                    "\"name\":\"%s\"," +
                    "\"description\":\"%s\"," +
                    "\"address\":%s," +
                    "\"businessType\":\"%s\"," +
                    "\"created\":\"%s\"," +
                    "\"currencySymbol\":\"%s\"," +
                    "\"currencyCode\":\"%s\"," +
                    "\"businessImages\":[]}," +
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

    @Before
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        Address address2 = new Address(
                "111",
                "Grant Road",
                "Invercargill",
                "Southland",
                "New Zealand",
                "9879",
                "Otatara"
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
                "First Business",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        business.setId(1);

        anotherBusiness = new Business(
                anotherUser.getId(),
                "Second Business",
                "some text",
                address2,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                anotherUser,
                "$",
                "NZD"
        );
        anotherBusiness.setId(2);
        anotherUser.setBusinessesAdministeredObjects(List.of(business, anotherBusiness));

        product = new Product(
                "PROD",
                anotherBusiness,
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

        product2 = new Product(
                "PROD2",
                business,
                "Cookies",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002627"
        );

        inventoryItem2 = new InventoryItem(
                product2,
                "PROD2",
                20,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );
        inventoryItem2.setId(2);

        userRepository = mock(UserRepository.class);
        listingRepository = mock(ListingRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                        listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository, soldListingRepository, listingNotificationRepository, soldListingNotificationRepository, bookmarkedListingMessageRepository))
                .build();
    }

    /* ------------------------------------------AC2------------------------------------------ */

    @Given("there exists a listing with name {string}")
    public void thereExistsAListingWithName(String name) throws IllegalListingArgumentException {
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

        assertThat(listing.getInventoryItem().getProduct().getName()).isEqualTo(name);
    }

    @When("I search for listings with no filters")
    public void iSearchForListingsWithNoFilters() throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    @Then("I receive the listing with name {string}")
    public void iReceiveTheListingWithName(String name) throws UnsupportedEncodingException {
        if (name.equals("Beans")) {
            expectedJSON = "[" + String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                    product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                    anotherBusiness.getId(), anotherUser.getId(), anotherUser.getFirstName(), anotherUser.getLastName(), anotherUser.getMiddleName(), anotherUser.getNickname(),
                    anotherUser.getBio(), anotherUser.getEmail(), anotherUser.getCreated(), anotherUser.getRole(), anotherUser.getDateOfBirth(), anotherUser.getPhoneNumber(),
                    anotherUser.getHomeAddress(), anotherBusiness.getPrimaryAdministratorId(), anotherBusiness.getName(),
                    anotherBusiness.getDescription(), anotherBusiness.getAddress(), anotherBusiness.getBusinessType(), anotherBusiness.getCreated(), anotherBusiness.getCurrencySymbol(), anotherBusiness.getCurrencyCode(),
                    product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                    inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                    listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                    listing.isBookmarked(anotherUser), listing.getTotalBookmarks())
                    + "]";
        } else {
            expectedJSON = "[" + String.format(expectedListingJSON, listing2.getId(), inventoryItem2.getId(), product2.getProductId(), product2.getName(),
                    product2.getDescription(), product2.getManufacturer(), product2.getRecommendedRetailPrice(), product2.getCreated(),
                    business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                    user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                    user.getHomeAddress(), business.getPrimaryAdministratorId(), business.getName(),
                    business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), business.getCurrencySymbol(), business.getCurrencyCode(),
                    product2.getBarcode(), inventoryItem2.getQuantity(), inventoryItem2.getPricePerItem(), inventoryItem2.getTotalPrice(),
                    inventoryItem2.getManufactured(), inventoryItem2.getSellBy(), inventoryItem2.getBestBefore(), inventoryItem2.getExpires(),
                    listing2.getQuantity(), listing2.getPrice(), listing2.getMoreInfo(), listing2.getCreated().toString(), listing2.getCloses().toString(),
                    listing2.isBookmarked(user), listing2.getTotalBookmarks())
                    + "]";
        }

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /* ------------------------------------------AC4------------------------------------------ */

    @Given("there exists listings with names {string} and {string}")
    public void thereExistsListingsWithNamesAnd(String name1, String name2) throws IllegalListingArgumentException {

        listing = new Listing(
                inventoryItem,
                3,
                10.5,
                "more info",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusDays(1)
        );
        listing.setId(1);

        listing2 = new Listing(
                inventoryItem2,
                3,
                19.0,
                "more info",
                LocalDateTime.now().minusDays(10),
                LocalDateTime.now().plusYears(2)
        );
        listing2.setId(1);

        assertThat(listing.getInventoryItem().getProduct().getName()).isEqualTo(name1);
        assertThat(listing2.getInventoryItem().getProduct().getName()).isEqualTo(name2);
    }

    @When("I search for listings in product name descending order")
    public void iSearchForListingsInProductNameDescendingOrder() throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        List<Listing> list = List.of(listing2, listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                        .param("orderBy", "productNameDESC")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    @Then("I receive the listings with names {string} and {string} in that order")
    public void iReceiveTheListingsWithNamesAndInThatOrder(String name2, String name1) throws UnsupportedEncodingException {

        expectedJSON = "[" + String.format(expectedListingJSON, listing2.getId(), inventoryItem2.getId(), product2.getProductId(), product2.getName(),
                product2.getDescription(), product2.getManufacturer(), product2.getRecommendedRetailPrice(), product2.getCreated(),
                business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), business.getCurrencySymbol(), business.getCurrencyCode(),
                product2.getBarcode(), inventoryItem2.getQuantity(), inventoryItem2.getPricePerItem(), inventoryItem2.getTotalPrice(),
                inventoryItem2.getManufactured(), inventoryItem2.getSellBy(), inventoryItem2.getBestBefore(), inventoryItem2.getExpires(),
                listing2.getQuantity(), listing2.getPrice(), listing2.getMoreInfo(), listing2.getCreated().toString(), listing2.getCloses().toString(),
                listing2.isBookmarked(user), listing2.getTotalBookmarks())
                + "," + String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                anotherBusiness.getId(), anotherUser.getId(), anotherUser.getFirstName(), anotherUser.getLastName(), anotherUser.getMiddleName(), anotherUser.getNickname(),
                anotherUser.getBio(), anotherUser.getEmail(), anotherUser.getCreated(), anotherUser.getRole(), anotherUser.getDateOfBirth(), anotherUser.getPhoneNumber(),
                anotherUser.getHomeAddress(), anotherBusiness.getPrimaryAdministratorId(), anotherBusiness.getName(),
                anotherBusiness.getDescription(), anotherBusiness.getAddress(), anotherBusiness.getBusinessType(), anotherBusiness.getCreated(), anotherBusiness.getCurrencySymbol(), anotherBusiness.getCurrencyCode(),
                product.getBarcode(), inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString(),
                listing.isBookmarked(anotherUser), listing.getTotalBookmarks())
                + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /* ------------------------------------------AC5------------------------------------------ */

    @When("I search for listings with businesses of type retail trade")
    public void iSearchForListingsWithBusinessesOfTypeRetailTrade() throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        String businessType = "RETAIL_TRADE";
        BusinessType convertedBusinessType = BusinessType.RETAIL_TRADE;

        List<Listing> list = List.of(listing2);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, List.of(convertedBusinessType), null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("businessTypes", businessType)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------AC6------------------------------------------ */

    @When("I search for product names with the query {string}")
    public void iSearchForProductNamesWithTheQuery(String query) throws Exception {
        List<String> names = Arrays.asList(query);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", query)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------AC7------------------------------------------ */

    @When("I limit the price range to a minimum of ${double} and maximum of ${double}")
    public void iLimitThePriceRangeToAMinimumOf$AndMaximumOf$(double minimum, double maximum) throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, minimum, maximum, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("minimumPrice", String.valueOf(minimum))
                .param("maximumPrice", String.valueOf(maximum))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------AC8------------------------------------------ */

    @When("I search for products for business names with the query {string}")
    public void iSearchForProductsForBusinessNamesWithTheQuery(String businessName) throws Exception {
        List<String> names = Arrays.asList(businessName);

        List<Listing> list = List.of(listing2);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByBusinessName(
                names, paging, null, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", businessName)
                .param("searchType", "businessName")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------AC9------------------------------------------ */

    @When("I search for products for business addresses with the query {string}")
    public void iSearchForProductsForBusinessAddressesWithTheQuery(String addressName) throws Exception {
        List<String> names = Arrays.asList(addressName);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByLocation(
                names, paging, null, null, null, null, null, null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", addressName)
                .param("searchType", "location")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------AC10------------------------------------------ */

    @When("I limit the closing date range to from {string} to {string}")
    public void iLimitTheClosingDateRangeToFromTo(String fromDate, String toDate) throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null,
                LocalDateTime.of(2021,1,1,0,0),
                LocalDateTime.of(2022,1,1,0,0),
                null
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("fromDate", fromDate)
                .param("toDate", toDate)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }

    /* ------------------------------------------BS2------------------------------------------ */

    @When("I search using the barcode {string}")
    public void iLimitTheClosingDateRangeToFromTo(String barcode) throws Exception {
        String searchQuery = "";
        List<String> names = Arrays.asList(searchQuery);

        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("inventoryItemId.product.name").ignoreCase());
        Pageable paging = PageRequest.of(0, 12, sort);

        when(listingRepository.findAllListingsByProductName(
                names, paging, null, null, null, null, null, barcode
        )).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get("/listings").param("searchQuery", searchQuery)
                .param("barcode", barcode)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))).andReturn().getResponse();
    }
}
