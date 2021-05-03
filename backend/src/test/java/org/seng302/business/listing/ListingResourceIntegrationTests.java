package org.seng302.business.listing;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
import org.seng302.business.inventoryItem.InventoryItem;
import org.seng302.business.inventoryItem.InventoryItemRepository;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductRepository;
import org.seng302.business.product.ProductResource;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class ListingResourceIntegrationTests {

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

    private MockHttpServletResponse response;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    private final String listingPayload = "{\"inventoryItemId\":\"%s\"," +
                                                "\"quantity\":%d," +
                                                "\"price\":%f," +
                                                "\"moreInfo\":\"%s\"," +
                                                "\"closes\":\"%s\"}";

    private String expectedJSON;

    private final String expectedListingJSON = "[" +
                                            "{\"id\":%s," +
                                            "\"inventoryItem\":" +
                                                "{\"id\":%s," +
                                                "\"product\":{" +
                                                    "\"id\":\"%s\"," +
                                                    "\"name\":\"%s\"," +
                                                    "\"description\":\"%s\"," +
                                                    "\"manufacturer\":\"%s\"," +
                                                    "\"recommendedRetailPrice\":%.1f," +
                                                    "\"created\":\"%s\"}," +
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
                                            "\"closes\":\"%s\"}" +
                                            "]";


    private String inventoryItemId;
    private Integer quantity;
    private Double price;
    private String moreInfo;
    private LocalDateTime closes;

    @BeforeAll
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
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

        listing = new Listing(
                inventoryItem,
                3,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
        listing.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository))
                .build();
    }

    @Test
    public void canCreateProductWhenBusinessExistsAndDataValidWithBusinessAdministratorUserCookie() throws Exception {
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

    @Test
    public void canCreateProductWhenBusinessExistsAndDataInvalidWithBusinessAdministratorUserCookie() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.ofNullable(product));
        given(inventoryItemRepository.findInventoryItemById(1)).willReturn(Optional.ofNullable(null));

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

    @Test
    public void canCreateProductWhenBusinessDoesntExistsAndDataValid() throws Exception {
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(null));
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
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    @Test
    public void canCreateProductWhenBusinessExistsAndDataValidWithoutUserCookie() throws Exception {
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

    @Test
    public void canCreateProductWhenBusinessExistsAndDataValidWithUserCookieGAA() throws Exception {
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(user));
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

    @Test
    public void canCreateProductWhenBusinessExistsAndDataValidWithInvalidUserCookie() throws Exception {
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

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(null));
        when(listingRepository.save(any(Listing.class))).thenReturn(listing);
        response = mvc.perform(post(String.format("/businesses/%d/listings", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(json)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
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
    public void canRetrieveListingsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedListingJSON, listing.getId(), inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires(),
                listing.getQuantity(), listing.getPrice(), listing.getMoreInfo(), listing.getCreated().toString(), listing.getCloses().toString());


        // when
        List<Listing> list = List.of(listing);
        Page<Listing> pagedResponse = new PageImpl<Listing>(list);
        Sort sort = Sort.by(Sort.Order.asc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(listingRepository.findListingsByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/listings", business.getId()))
                .param("orderBy", "createdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }
}
