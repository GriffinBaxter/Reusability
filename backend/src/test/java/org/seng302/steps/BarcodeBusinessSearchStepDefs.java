package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.InventoryItemResource;
import org.seng302.controller.ListingResource;
import org.seng302.controller.ProductResource;
import org.seng302.controller.UserResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

public class BarcodeBusinessSearchStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc userMVC;

    @Autowired
    private MockMvc inventoryMVC;

    @Autowired
    private MockMvc listingMVC;

    @Autowired
    private MockMvc productMVC;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private BusinessRepository businessRepository;

    @Autowired
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    @MockBean
    private InventoryItemRepository inventoryRepository;

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

    @Autowired
    @MockBean
    private ProductUpdateService productUpdateService;

    private MockHttpServletResponse response;

    private User user;
    private Address address;
    private Business business;
    private Product product;
    private InventoryItem inventoryItem;
    private Listing listing;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String expectedProductJson = "{\"id\":\"%s\"," +
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
            "\"images\":[]," +
            "\"dateOfBirth\":\"%s\"," +
            "\"phoneNumber\":\"%s\"," +
            "\"homeAddress\":{\"streetNumber\":\"%s\",\"streetName\":\"%s\",\"suburb\":\"%s\",\"city\":\"%s\",\"region\":\"%s\",\"country\":\"%s\",\"postcode\":\"%s\"}}]," +
            "\"primaryAdministratorId\":%d," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"address\":%s," +
            "\"businessType\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"currencySymbol\":\"%s\"," +
            "\"currencyCode\":\"%s\"}," +
            "\"barcode\":\"%s\"}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        businessRepository = mock(BusinessRepository.class);
        productRepository = mock(ProductRepository.class);
        inventoryRepository = mock(InventoryItemRepository.class);
        listingRepository = mock(ListingRepository.class);
        soldListingRepository = mock(SoldListingRepository.class);
        listingNotificationRepository = mock(ListingNotificationRepository.class);
        soldListingNotificationRepository = mock(SoldListingNotificationRepository.class);
        bookmarkedListingMessageRepository = mock(BookmarkedListingMessageRepository.class);
        productUpdateService = mock(ProductUpdateService.class);

        this.productMVC = MockMvcBuilders.standaloneSetup(new ProductResource(productRepository, businessRepository, userRepository, productUpdateService)).build();
        this.inventoryMVC = MockMvcBuilders.standaloneSetup(new InventoryItemResource(inventoryRepository, productRepository, businessRepository, userRepository)).build();
        this.listingMVC = MockMvcBuilders.standaloneSetup(new ListingResource(listingRepository, inventoryRepository, productRepository, businessRepository, userRepository, soldListingRepository, listingNotificationRepository, soldListingNotificationRepository, bookmarkedListingMessageRepository)).build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();
    }

    @Given("I am a logged in business administrator.")
    public void i_am_a_logged_in_business_administrator() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);
        business = new Business(
                user.getId(),
                "Business Name",
                "Description",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        business.setId(1);
        user.setBusinessesAdministeredObjects(List.of(business));

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, user.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, user.getId()));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Given("I have a product with a barcode {string}")
    public void iHaveAProductWithABarcode(String barcode) throws Exception {
        product = new Product(
                "TEST-LIST",
                business,
                "Listing",
                "description",
                "manufacturer",
                Double.parseDouble("4.5"),
                barcode
        );
    }

    @When("I request to retrieve products for my business with barcode {string}")
    public void iRequestToRetrieveProductsForMyBusinessWithBarcode(String barcode) throws Exception {
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);

        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));

        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findAllProductsByBusinessIdAndIncludedFieldsAndBarcode(List.of("TEST-LIST"), List.of("id"), business.getId(), paging, barcode)).thenReturn(pagedResponse);

        response = productMVC.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("searchQuery", "TEST-LIST")
                .param("searchBy", "id")
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .param("barcode", barcode)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

    }

    @Then("My product is returned in a list by its self")
    public void myProductIsReturnedInAListByItsSelf() throws UnsupportedEncodingException {

        String expectedJSON = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(),
                business.getCurrencySymbol(), business.getCurrencyCode(), product.getBarcode()) + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    @Given("I have a inventory item with a barcode {string}")
    public void iHaveAInventoryItemWithABarcode(String barcode) throws Exception {
        product = new Product(
                "TEST",
                business,
                "name",
                "description",
                "manufacturer",
                Double.parseDouble("4.5"),
                barcode
        );
        inventoryItem = new InventoryItem(product, product.getProductId(),
                5, 2.0,10.0,
                null,null,null,LocalDate.now().plusDays(2));
    }

    @When("I request to retrieve inventory item for my business with barcode {string}")
    public void iRequestToRetrieveInventoryItemForMyBusinessWithBarcode(String barcode) throws Exception {
        List<InventoryItem> list = List.of(inventoryItem);
        Page<InventoryItem> pagedResponse = new PageImpl<>(list);

        Sort sort = Sort.by(Sort.Order.asc("productId").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(inventoryRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, business.getId(), paging)).thenReturn(pagedResponse);

        response = inventoryMVC.perform(get(String.format("/businesses/%d/inventory", business.getId()))
                .param("barcode", barcode)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
        ).andReturn().getResponse();

    }

    @Then("My inventory item is returned in a list by its self")
    public void myInventoryItemIsReturnedInAListByItsSelf() throws UnsupportedEncodingException {
        String expectedJSON = "[{" +
                "\"id\":0," +
                "\"product\":{" + "\"id\":\"TEST\",\"name\":\"name\",\"description\":\"description\",\"manufacturer\":\"manufacturer\",\"recommendedRetailPrice\":4.5," + "\"created\":\"" + product.getCreated().toString() + "\",\"images\":[]," +
                "\"business\":{\"id\":1," +
                "\"administrators\":[" +
                "{\"id\":1,\"firstName\":\"Bob\",\"lastName\":\"Smith\",\"middleName\":\"Ben\",\"nickname\":\"Bobby\",\"bio\":\"cool person\",\"email\":\"email@email.com\",\"created\":\"2021-02-02T00:00\",\"role\":\"GLOBALAPPLICATIONADMIN\",\"businessesAdministered\":[null],\"images\":[],\"dateOfBirth\":\"2007-02-02\",\"phoneNumber\":\"0271316\",\"homeAddress\":{\"streetNumber\":\"3/24\",\"streetName\":\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"region\":\"Canterbury\",\"country\":\"New Zealand\",\"postcode\":\"90210\"}}]," +
                "\"primaryAdministratorId\":1,\"name\":\"Business Name\",\"description\":\"Description\"," +
                "\"address\":{\"streetNumber\":\"3/24\",\"streetName\":\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"region\":\"Canterbury\",\"country\":\"New Zealand\",\"postcode\":\"90210\"}" +
                ",\"businessType\":\"ACCOMMODATION_AND_FOOD_SERVICES\",\"created\":\"2021-02-02T00:00\",\"currencySymbol\":\"$\",\"currencyCode\":\"NZD\"}," +
                "\"barcode\":\"9300675024235\"}" +
                ",\"quantity\":5,\"pricePerItem\":2.0,\"totalPrice\":10.0,\"manufactured\":\"\",\"sellBy\":\"\",\"bestBefore\":\"\",\"expires\":\"" + inventoryItem.getExpires().toString() + "\"}]";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    @Given("I have a listing with a barcode {string}")
    public void iHaveAListingWithABarcode(String barcode) throws Exception {
        product = new Product(
                "TEST-LIST",
                business,
                "Listing",
                "description",
                "manufacturer",
                Double.parseDouble("4.5"),
                barcode
        );
        inventoryItem = new InventoryItem(product, product.getProductId(),
                5, 2.0,10.0,
                null,null,null,LocalDate.now().plusDays(2));
        listing = new Listing(inventoryItem, 2, 9.0, "",
                LocalDateTime.now(), LocalDateTime.now().plusDays(2));
    }

    @When("I request to retrieve listings for my business with barcode {string}")
    public void iRequestToRetrieveListingsForMyBusinessWithBarcode(String barcode) throws Exception {
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<>(list);

        Sort sort = Sort.by(Sort.Order.asc("closes").ignoreCase())
                .and(Sort.by(Sort.Order.asc("id").ignoreCase()));

        Pageable paging = PageRequest.of(0, 5, sort);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(listingRepository.findByBusinessIdAndInventoryItemProductBarcode(business.getId(), barcode, paging)).thenReturn(pagedResponse);

        response = listingMVC.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("barcode", barcode)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
        ).andReturn().getResponse();
    }

    @Then("My listing is returned in a list by its self")
    public void myListingIsReturnedInAListByItsSelf() throws UnsupportedEncodingException {
        String expectedJSON = "[{" +
                "\"id\":0," +
                "\"inventoryItem\":{" +
                "\"id\":0," +
                "\"product\":{" + "\"id\":\"TEST-LIST\",\"name\":\"Listing\",\"description\":\"description\",\"manufacturer\":\"manufacturer\",\"recommendedRetailPrice\":4.5," + "\"created\":\"" + product.getCreated().toString() + "\",\"images\":[]," +
                "\"business\":{\"id\":1," +
                "\"administrators\":[" +
                "{\"id\":1,\"firstName\":\"Bob\",\"lastName\":\"Smith\",\"middleName\":\"Ben\",\"nickname\":\"Bobby\",\"bio\":\"cool person\",\"email\":\"email@email.com\",\"created\":\"2021-02-02T00:00\",\"role\":\"GLOBALAPPLICATIONADMIN\",\"businessesAdministered\":[null],\"images\":[],\"dateOfBirth\":\"2007-02-02\",\"phoneNumber\":\"0271316\",\"homeAddress\":{\"streetNumber\":\"3/24\",\"streetName\":\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"region\":\"Canterbury\",\"country\":\"New Zealand\",\"postcode\":\"90210\"}}]," +
                "\"primaryAdministratorId\":1,\"name\":\"Business Name\",\"description\":\"Description\"," +
                "\"address\":{\"streetNumber\":\"3/24\",\"streetName\":\"Ilam Road\",\"suburb\":\"Ilam\",\"city\":\"Christchurch\",\"region\":\"Canterbury\",\"country\":\"New Zealand\",\"postcode\":\"90210\"}" +
                ",\"businessType\":\"ACCOMMODATION_AND_FOOD_SERVICES\",\"created\":\"2021-02-02T00:00\",\"currencySymbol\":\"$\",\"currencyCode\":\"NZD\"}," +
                "\"barcode\":\"" + product.getBarcode() +
                "\"}" +
                ",\"quantity\":5,\"pricePerItem\":2.0,\"totalPrice\":10.0,\"manufactured\":\"\",\"sellBy\":\"\",\"bestBefore\":\"\",\"expires\":\"" + inventoryItem.getExpires().toString() + "\"}," +
                "\"quantity\":2,\"price\":9.0,\"moreInfo\":\"\",\"created\":\"" + listing.getCreated().toString() +
                "\",\"closes\":\"" + listing.getCloses().toString() +
                "\",\"isBookmarked\":false,\"totalBookmarks\":0" +
                "}]";
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }
}
