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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
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

}
