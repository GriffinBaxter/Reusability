package org.seng302.business.inventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * ProductResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
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

    private MockHttpServletResponse response;

    private User user;

    private User anotherUser;

    private Business business;

    private Product product;

    @BeforeEach
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
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
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );

        this.mvc = MockMvcBuilders.standaloneSetup(new InventoryItemResource(
                inventoryItemRepository, productRepository, businessRepository, userRepository))
                .build();
    }

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
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.ofNullable(null));
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


}