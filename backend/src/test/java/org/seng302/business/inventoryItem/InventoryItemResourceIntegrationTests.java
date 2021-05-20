package org.seng302.business.inventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
import org.seng302.business.listing.Listing;
import org.seng302.business.product.Product;
import org.seng302.business.product.ProductRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * InventoryItemResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class InventoryItemResourceIntegrationTests {

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

    private final String inventoryItemPayloadJson = "{" +
            "\"productId\":\"%s\"," +
            "\"quantity\":%d," +
            "\"pricePerItem\":%f," +
            "\"totalPrice\":%f," +
            "\"manufactured\":\"%s\"," +
            "\"sellBy\":\"%s\"," +
            "\"bestBefore\":\"%s\"," +
            "\"expires\":\"%s\"" +
            "}";

    private String payloadJson;

    private final String expectedInventoryItemJson = "[" +
            "{\"id\":%s," +
            "\"product\":{" +
            "\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.2f," +
            "\"created\":\"%s\"}," +
            "\"quantity\":%d," +
            "\"pricePerItem\":%.2f," +
            "\"totalPrice\":%.2f," +
            "\"manufactured\":\"%s\"," +
            "\"sellBy\":\"%s\"," +
            "\"bestBefore\":\"%s\"," +
            "\"expires\":\"%s\"}" +
            "]";

    private String expectedJson;

    private MockHttpServletResponse response;

    private User user;

    private User anotherUser;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    @BeforeEach
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

        anotherUser = new User("first",
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
        anotherUser.setId(2);
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
        business.setId(3);
        product = new Product(
                "WATT-420-BEANS",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.99,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );

        inventoryItem = new InventoryItem(
                product,
                product.getProductId(),
                4,
                6.51,
                21.99,
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 1)
        );
        inventoryItem.setId(4);

        this.mvc = MockMvcBuilders.standaloneSetup(new InventoryItemResource(
                inventoryItemRepository, productRepository, businessRepository, userRepository))
                .build();
    }

//------------------------------- Tests for post /businesses/{id}/inventory/ endpoint ----------------------------------

    /**
     * Test that a CREATED(201) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a administrator of this business and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithAdministrator() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a GAA and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithGAA() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());
        anotherUser.setRole(Role.GLOBALAPPLICATIONADMIN);

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a DGAA and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithDGAA() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());
        anotherUser.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a UNAUTHORIZED(401) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie not exist and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWhenCookieNotExist() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test that a NOT_ACCEPTABLE(406) status is received when send correct InventoryRegistrationPayLoad, the business
     * has been given is not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWithBusinessIsNotExist() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.empty());
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that a FORBIDDEN(403) status is received when send correct InventoryRegistrationPayLoad, the business has
     * been given is exist, cookie contain a non-administrator(not GAA or DGAA) of this business and the product given
     * is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWithNon_Administrator() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no Product Id, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWithNotExistProductId() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setProductId(null);
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(null);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWithNotExistQuantity() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setQuantity(null);
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(null);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
//--------------------------------- Test for get /businesses/%d/inventory/ endpoint ------------------------------------

    /**
     * Test that a CREATED(201) status is received when send an InventoryRegistrationPayLoad with no price per item,
     * the business has been given is exist, cookie contain a administrator of this business and the product given is
     * exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithNotExistPricePerItem() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setPricePerItem(null);
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send an InventoryRegistrationPayLoad with no total price,
     * the business has been given is exist, cookie contain a administrator of this business and the product given is
     * exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithNotExistTotalPrice() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setTotalPrice(null);
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send an InventoryRegistrationPayLoad with no manufactured,
     * the business has been given is exist, cookie contain a administrator of this business and the product given is
     * exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithNotExistManufactured() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setManufactured(null);
        String inventoryItemPayloadJson = "{" +
                "\"productId\":\"%s\"," +
                "\"quantity\":%d," +
                "\"pricePerItem\":%f," +
                "\"totalPrice\":%f," +
                "\"manufactured\":%s," +
                "\"sellBy\":\"%s\"," +
                "\"bestBefore\":\"%s\"," +
                "\"expires\":\"%s\"" +
                "}";
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send an InventoryRegistrationPayLoad with no sell by, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithNotExistSellBy() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setSellBy(null);
        String inventoryItemPayloadJson = "{" +
                "\"productId\":\"%s\"," +
                "\"quantity\":%d," +
                "\"pricePerItem\":%f," +
                "\"totalPrice\":%f," +
                "\"manufactured\":\"%s\"," +
                "\"sellBy\":%s," +
                "\"bestBefore\":\"%s\"," +
                "\"expires\":\"%s\"" +
                "}";
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a CREATED(201) status is received when send an InventoryRegistrationPayLoad with no best before,
     * the business has been given is exist, cookie contain a administrator of this business and the product given is
     * exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canCreateAnInventoryItemWithNotExistBestBefore() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setBestBefore(null);
        String inventoryItemPayloadJson = "{" +
                "\"productId\":\"%s\"," +
                "\"quantity\":%d," +
                "\"pricePerItem\":%f," +
                "\"totalPrice\":%f," +
                "\"manufactured\":\"%s\"," +
                "\"sellBy\":\"%s\"," +
                "\"bestBefore\":%s," +
                "\"expires\":\"%s\"" +
                "}";
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no expires, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotCreateAnInventoryItemWithNotExistExpires() throws Exception {
        // given
        InventoryItem inventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        inventoryItem.setExpires(null);
        payloadJson = String.format(inventoryItemPayloadJson, inventoryItem.getProductId(), inventoryItem.getQuantity(),
                inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(), inventoryItem.getManufactured(),
                inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(inventoryItem);
        response = mvc.perform(post(String.format("/businesses/%d/inventory/", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

//------------------------------------ Tests for /businesses/{id}/inventory/ endpoint ----------------------------------

    /**
     * Tests that an OK status and a list of inventory item payloads is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canRetrieveInventoryItemsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        business.addAdministrators(user);
        expectedJson = String.format(expectedInventoryItemJson, inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        List<InventoryItem> list = List.of(inventoryItem);
        Page<InventoryItem> pagedResult = new PageImpl<>(list);
        Sort sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase()).and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase())).and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sortBy);

        when(inventoryItemRepository.findInventoryItemsByBusinessId(business.getId(), paging)).thenReturn(pagedResult);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of inventory item payloads is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a GAA.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canRetrieveInventoryItemsWhenBusinessExistsWithGAAUserCookie() throws Exception {
        // given
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        anotherUser.setRole(Role.GLOBALAPPLICATIONADMIN);
        expectedJson = String.format(expectedInventoryItemJson, inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        List<InventoryItem> list = List.of(inventoryItem);
        Page<InventoryItem> pagedResult = new PageImpl<>(list);
        Sort sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase()).and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase())).and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sortBy);

        when(inventoryItemRepository.findInventoryItemsByBusinessId(business.getId(), paging)).thenReturn(pagedResult);
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));

        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);

    }

    /**
     * Tests that an OK status and a list of inventory item payloads is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a DGAA.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canRetrieveInventoryItemsWhenBusinessExistsWithDGAAUserCookie() throws Exception {
        // given
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        anotherUser.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);
        expectedJson = String.format(expectedInventoryItemJson, inventoryItem.getId(), product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(), product.getCreated(),
                inventoryItem.getQuantity(), inventoryItem.getPricePerItem(), inventoryItem.getTotalPrice(),
                inventoryItem.getManufactured(), inventoryItem.getSellBy(), inventoryItem.getBestBefore(), inventoryItem.getExpires());

        // when
        List<InventoryItem> list = List.of(inventoryItem);
        Page<InventoryItem> pagedResult = new PageImpl<>(list);
        Sort sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase()).and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase())).and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sortBy);

        when(inventoryItemRepository.findInventoryItemsByBusinessId(business.getId(), paging)).thenReturn(pagedResult);
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));

        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);

    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the business ID in the /businesses/{id}/inventory/
     * API endpoint does not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveInventoryWhenBusinessDoesntExist() throws Exception {
        // given
        user.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);
        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(0)).thenReturn(Optional.empty());

        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }


    /**
     * Tests that an UNAUTHORIZED status and is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists but the cookie contains a non-existing user ID.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveInventoryWhenBusinessExistsWithNonExistingIdCookie() throws Exception {

        // given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", "0")))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);

    }

    /**
     * Tests that a FORBIDDEN status and is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists but the cookie contains a non-admin user ID.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveInventoryWhenBusinessExistsWithNonAdminUserCookie() throws Exception {
        // given
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status and is received when the business ID in the
     * /businesses/{id}/inventory/ API endpoint exists but there is no cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveInventoryWhenBusinessExistsWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }


    /**
     * Tests that an UNAUTHORIZED status is given when the the business exists and the user is the business
     * admin BUT they have are not logged in i.e. no is cookie present.
     * This is for testing /businesses/{id}/inventory/ API endpoint exists.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveInventoryItemsWhenBusinessExistsWithBusinessAdministratorNoUserCookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(3)).willReturn(Optional.ofNullable(business));
        business.addAdministrators(user);
        expectedJson = "";

        // when
        List<InventoryItem> list = List.of(inventoryItem);
        Page<InventoryItem> pagedResponse = new PageImpl<>(list);
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase())).and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sortBy);

        when(inventoryItemRepository.findInventoryItemsByBusinessId(3, paging)).thenReturn(pagedResponse);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get(String.format("/businesses/%d/inventory/", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

//--------------------------- Tests for /businesses/{id}/inventory/{inventoryItemId} endpoint --------------------------

    /**
     * Test that a OK(200) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a administrator of this business and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canUpdateAnInventoryItemWithAdministrator() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that a OK(200) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a GAA and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canUpdateAnInventoryItemWithGAA() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());
        anotherUser.setRole(Role.GLOBALAPPLICATIONADMIN);

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that a OK(200) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie contain a DGAA and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canUpdateAnInventoryItemWithDGAA() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());
        anotherUser.setRole(Role.DEFAULTGLOBALAPPLICATIONADMIN);

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Test that a UNAUTHORIZED(401) status is received when send correct InventoryRegistrationPayLoad, the business has been given
     * is exist, cookie not exist and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWhenCookieNotExist() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Test that a NOT_ACCEPTABLE(406) status is received when send correct InventoryRegistrationPayLoad, the business
     * has been given is not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithBusinessIsNotExist() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.empty());
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Test that a FORBIDDEN(403) status is received when send correct InventoryRegistrationPayLoad, the business has
     * been given is exist, cookie contain a non-administrator(not GAA or DGAA) of this business and the product given
     * is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNon_Administrator() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no Product Id, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNotExistInventoryItemId() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.empty());
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("selected inventory item does not exist");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no Product Id, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNoProductId() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setProductId(null);
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid product ID");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNoQuantity() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setQuantity(null);
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid quantity, must have at least one item " +
                "AND must be more than the total quantity in your current listings");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNegativeQuantity() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setQuantity(-10);
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid quantity, must have at least one item " +
                "AND must be more than the total quantity in your current listings");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNegativePricePerItem() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setPricePerItem(-5.2);
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid price per item, must not be negative");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithNegativeTotalPrice() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setTotalPrice(-20.2);
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid total price, must not be negative");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithManufactureDateAfterToday() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setManufactured(LocalDate.of(2022, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid manufacture date");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with no quantity, the
     * business has been given is exist, cookie contain a administrator of this business and the product given is exist
     * in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithExpiresDateBeforeToday() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));
        newInventoryItem.setExpires(LocalDate.of(2020, 2, 2));
        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid expiration date, must have expiration date and cannot add expired item");
    }

    /**
     * Test that a BAD_REQUEST(400) status is received when send an InventoryRegistrationPayLoad with quantity less
     * than the total listings, the business has been given is exist, cookie contain a administrator of this business
     * and the product given is exist in this business.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canNotUpdateAnInventoryItemWithQuantityLessThanTotalListings() throws Exception {
        // given
        InventoryItem newInventoryItem = new InventoryItem(product,
                product.getProductId(),
                3,
                6.5,
                21.99,
                LocalDate.of(2020, 2, 2),
                LocalDate.of(2021, 2, 2),
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 2, 2));

        inventoryItem.setListings(List.of(new Listing(
                inventoryItem,
                4,
                null,
                "info",
                LocalDateTime.now(),
                null
        )));

        payloadJson = String.format(inventoryItemPayloadJson, newInventoryItem.getProductId(), newInventoryItem.getQuantity(),
                newInventoryItem.getPricePerItem(), newInventoryItem.getTotalPrice(), newInventoryItem.getManufactured(),
                newInventoryItem.getSellBy(), newInventoryItem.getBestBefore(), newInventoryItem.getExpires());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(business));
        when(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .thenReturn(Optional.ofNullable(product));
        when(inventoryItemRepository.findInventoryItemById(inventoryItem.getId()))
                .thenReturn(Optional.ofNullable(inventoryItem));
        when(inventoryItemRepository.save(any(InventoryItem.class))).thenReturn(newInventoryItem);
        response = mvc.perform(put(String.format("/businesses/%d/inventory/%d", business.getId(), inventoryItem.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid quantity, must have at least one item " +
                "AND must be more than the total quantity in your current listings");
    }

}
