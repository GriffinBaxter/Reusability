package org.seng302.business.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.model.Address;
import org.seng302.model.*;
import org.seng302.controller.ProductResource;
import org.seng302.Main;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.ProductRepository;
import org.seng302.model.repository.ProductUpdateService;
import org.seng302.model.repository.UserRepository;
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
 * ProductResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ProductResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductUpdateService productUpdateService;

    private MockHttpServletResponse response;

    private final String productPayloadJson = "{\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.1f," +
            "\"barcode\":\"%s\"}";

    private String payloadJson;

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
            "\"dateOfBirth\":\"%s\"," +
            "\"phoneNumber\":\"%s\"," +
            "\"homeAddress\":{\"streetNumber\":\"%s\",\"streetName\":\"%s\",\"suburb\":\"%s\",\"city\":\"%s\",\"region\":\"%s\",\"country\":\"%s\",\"postcode\":\"%s\"}}]," +
            "\"primaryAdministratorId\":%d," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"address\":%s," +
            "\"businessType\":\"%s\"," +
            "\"created\":\"%s\"}," +
            "\"barcode\":\"%s\"}";

    private String expectedJson;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Product product;

    private Product anotherProduct;

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
        user = new User("first",
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

        anotherProduct = new Product(
                "PROD2",
                business,
                "AnotherProduct",
                "Description2",
                "Manufacturer2",
                22.00,
                "036000291452"
        );


        this.mvc = MockMvcBuilders.standaloneSetup(new ProductResource(
                productRepository, businessRepository, userRepository, productUpdateService))
                .build();
    }

    /**
     * Tests that a CREATED status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains a product with valid data (and a product ID
     * that doesn't already exist for the given business) and an existing business ID.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canCreateProductWhenBusinessExistsAndDataValidWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        Product newProduct = new Product(
                "NEW",
                business,
                "NewProd",
                "NewDesc",
                "Manufacturer",
                10.00,
                "9400547002634"
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a CREATED status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains a product with valid data (and a product ID
     * that doesn't already exist for the given business) and an existing business ID.
     * Test specifically for when the cookie contains an ID belonging to a DGAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canCreateProductWhenBusinessExistsAndDataValidWithDgaaCookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        Product newProduct = new Product(
                "NEW",
                business,
                "NewProd",
                "NewDesc",
                "Manufacturer",
                10.00,
                "9400547002634"
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a CREATED status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains a product with valid data (and a product ID
     * that doesn't already exist for the given business) and an existing business ID.
     * Test specifically for when the cookie contains an ID belonging to a GAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canCreateProductWhenBusinessExistsAndDataValidWithGaaCookie() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(gAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        Product newProduct = new Product(
                "NEW",
                business,
                "NewProd",
                "NewDesc",
                "Manufacturer",
                10.00,
                "9400547002634"
        );

        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a CREATED status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains a product with valid data (and a product ID
     * that doesn't already exist for the given business but exists for a different business) and an
     * existing business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void canCreateProductWithProductIdThatExistsForAnotherBusiness() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(businessRepository.findBusinessById(2)).willReturn(Optional.ofNullable(anotherBusiness));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.ofNullable(product));

        Product newProduct = new Product(
                "PROD",
                anotherBusiness,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), anotherBusiness.getId()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        response = mvc.perform(post(String.format("/businesses/%d/products", anotherBusiness.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains a product ID that already exists for an
     * existing business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateProductWhenBusinessExistsButProductIdAlreadyExists() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.ofNullable(product));
        payloadJson = String.format(productPayloadJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice(), product.getBarcode());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a BAD_REQUEST status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains invalid data and an existing business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateProductWhenBusinessExistsButDataIsInvalid() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.empty());
        payloadJson = String.format(productPayloadJson, "P", product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice(), product.getBarcode());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests that a FORBIDDEN status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains valid data and an existing business ID but with
     * a non-admin cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateProductWhenBusinessExistsAndDataValidWithNonAdminCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        Product newProduct = new Product(
                "NEW",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests that an UNAUTHORIZED status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains valid data and an existing business ID but with
     * no cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateProductWhenBusinessExistsAndDataValidWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        Product newProduct = new Product(
                "NEW",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice(), newProduct.getBarcode());
        given(productRepository.findProductByIdAndBusinessId(newProduct.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        // when
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when sending a product creation payload to the
     * /businesses/{id}/products API endpoint that contains valid data but a non-existing business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantCreateProductWhenBusinessDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        payloadJson = String.format(productPayloadJson, "PRO", "name", "desc", "manu", 30.00, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(post(String.format("/businesses/%d/products", 0))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    //---------------------------------- Tests for /businesses/{id}/products endpoint ----------------------------------

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a DGAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithDGAACookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a GAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithGAACookie() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(gAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the order by and page params provided are valid.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithValidOrderByAndPageParams() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a BAD_REQUEST status and no product payloads are received when the business ID in the
     * /businesses/{id}/products API endpoint exists but the order by param is invalid.
     * Test specifically for when the order by param provided is invalid.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessExistsWithInvalidOrderByParam() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "a")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a BAD_REQUEST status and no product payloads are received when the business ID in the
     * /businesses/{id}/products API endpoint exists but the page param is invalid.
     * Test specifically for when the page param provided is invalid.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessExistsWithInvalidPageParam() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "a")
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the business ID in the /businesses/{id}/products
     * API endpoint does not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(businessRepository.findBusinessById(0)).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/businesses/%d/products", 0))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status and is received when the business ID in the
     * /businesses/{id}/products API endpoint exists but the cookie contains a non-existing user ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessExistsWithNonExistingIdCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", String.valueOf(0))))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a FORBIDDEN status and is received when the business ID in the
     * /businesses/{id}/products API endpoint exists but the cookie contains a non-admin user ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessExistsWithNonAdminUserCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an UNAUTHORIZED status and is received when the business ID in the
     * /businesses/{id}/products API endpoint exists but there is no cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveProductsWhenBusinessExistsWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId())))
                    .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the barcode ascending order by and page params provided are valid.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithValidBarcodeAscOrderByAndPageParams() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("barcode").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                        .param("orderBy", "barcodeASC")
                        .param("page", "0")
                        .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the barcode descending order by and page params provided are valid.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveProductsWhenBusinessExistsWithValidBarcodeDescOrderByAndPageParams() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("barcode").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                        .param("orderBy", "barcodeDESC")
                        .param("page", "0")
                        .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    // ------------------------------------------ /businesses/{businessId}/products/{productId} endpoint tests --------------------------------------

    /**
     * Tests that a UNAUTHORIZED status is returned when attempting to modify a product without a cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithoutASessionCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }


    /**
     * Tests that a BAD_REQUEST status is returned when attempting to modify a product with invalid product id (path variable).
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithInvalidProductId() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(productRepository.findProductByIdAndBusinessId("FAKE", product.getBusinessId())).thenReturn(Optional.empty());
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), "FAKE"))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Tests that a OK status is returned when attempting to modify a product with valid details and as DGAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canModifyProductAsDGAA() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when attempting to modify a product with valid details and as GAA.
     *
     * @throws Exception Exception error
     */
    @Test
    void canModifyProductAsGAA() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(gAA.getId())).willReturn(Optional.of(gAA));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.of(gAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when attempting to modify a product with valid details and as a USER that is an admin of the business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canModifyProductAsUserAdmin() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when attempting to modify a product with valid details and as a USER that is not an admin of the business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canModifyProductAsUserNonAdmin() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(anotherUser.getId())).willReturn(Optional.of(anotherUser));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.of(anotherUser));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }


    /**
     * Tests that a OK status is returned and no details have changed when trying to modify a product with an empty payload.
     *
     * @throws Exception Exception error
     */
    @Test
    void canModifyProductWithEmptyPayloadAndNoEffectsOnProduct() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, "NEW-ID", "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Tests that a BAD_REQUEST status is returned when the new ID provided for the product is already associated with the business and exists.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductIfTheNewIdAlreadyExists() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(productRepository.findProductByIdAndBusinessId(anotherProduct.getProductId(), product.getBusinessId())).willReturn(Optional.of(anotherProduct));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format(productPayloadJson, anotherProduct.getProductId(), "New name", "New desc", "New manufacturer", 666.0, "9400547002634");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                        .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Tests that a BAD_REQUEST status is returned and no details have changed when trying to modify a product with no name included.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithMissingNewName() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format("{\"id\": \"%s\", \"description\": \"%s\", \"manufacturer\": \"%s\", \"recommendedRetailPrice\": %.1f}", "NEW-ID", "New desc", "New manufacturer", 666.0);

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    /**
     * Tests that a OK status is returned when trying to modify a product with no new id included.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithMissingNewId() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format("{" +
                "\"name\":\"%s\"," +
                "\"description\":\"%s\"," +
                "\"manufacturer\":\"%s\"," +
                "\"recommendedRetailPrice\":%.1f}", "New name", "New desc", "New manufacturer", 666.0);

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when trying to modify a product with no new description included.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithMissingNewDescription() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format("{" +
                "\"id\":\"%s\"," +
                "\"name\":\"%s\"," +
                "\"manufacturer\":\"%s\"," +
                "\"recommendedRetailPrice\":%.1f}", "NEW-ID","New name", "New manufacturer", 666.0);

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when trying to modify a product with no new manufacturer included.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithMissingNewManufacturer() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format("{" +
                "\"id\":\"%s\"," +
                "\"name\":\"%s\"," +
                "\"description\":\"%s\"," +
                "\"recommendedRetailPrice\":%.1f}", "NEW-ID","New name", "New desc", 666.0);

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    /**
     * Tests that a OK status is returned when trying to modify a product with no new recommended retail price included.
     *
     * @throws Exception Exception error
     */
    @Test
    void cannotModifyAProductWithMissingNewRecommendedRetailPrice() throws Exception {
        // given
        given(businessRepository.findBusinessById(product.getBusinessId())).willReturn(Optional.of(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), product.getBusinessId())).willReturn(Optional.of(product));
        given(userRepository.findById(dGAA.getId())).willReturn(Optional.of(dGAA));
        expectedJson = "";
        payloadJson = String.format("{" +
                "\"id\":\"%s\"," +
                "\"name\":\"%s\"," +
                "\"description\":\"%s\"," +
                "\"manufacturer\":\"%s\"" +
                "}", "NEW-ID","New name", "New desc", "New manufacturer");

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.of(dGAA));
        response = mvc.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID()))
        ).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }


    //---------------------------------- Tests for /businesses/{id}/productAll endpoint ----------------------------------

    /**
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/productAll API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveAllProductsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), product.getBarcode()) + "," +
                String.format(expectedProductJson, anotherProduct.getProductId(), anotherProduct.getName(),
                anotherProduct.getDescription(), anotherProduct.getManufacturer(), anotherProduct.getRecommendedRetailPrice(),
                anotherProduct.getCreated(), business.getId(), user.getId(), user.getFirstName(), user.getLastName(), user.getMiddleName(), user.getNickname(),
                user.getBio(), user.getEmail(), user.getCreated(), user.getRole(), user.getDateOfBirth(), user.getPhoneNumber(),
                user.getHomeAddress().getStreetNumber(), user.getHomeAddress().getStreetName(), user.getHomeAddress().getSuburb(),
                user.getHomeAddress().getCity(), user.getHomeAddress().getRegion(), user.getHomeAddress().getCountry(),
                user.getHomeAddress().getPostcode(), business.getPrimaryAdministratorId(), business.getName(),
                business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated(), anotherProduct.getBarcode()) + "]";

        // when
        List<Product> list = List.of(product, anotherProduct);
        when(productRepository.findAllByBusinessId(1)).thenReturn(list);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get(String.format("/businesses/%d/productAll", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT ACCEPTABLE status is received when the business ID in the
     * /businesses/{id}/productAll API endpoint does not exist.
     * Test specifically for when the cookie contains an ID belonging to an authorized user.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveAllProductsWhenBusinessDoesNotExistWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get(String.format("/businesses/%d/productAll", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests that a UNAUTHORIZED status is received when the user has an invalid cookie when
     * trying to retrieve all products.
     *
     * @throws Exception Exception error
     */
    @Test
    void cantRetrieveAllProductsWhenCookieIsInvalid() throws Exception {
        // given
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.empty());

        // when
        response = mvc.perform(get(String.format("/businesses/%d/productAll", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an OK status and an empty list of product payloads is received when the business ID in the
     * /businesses/{id}/productAll API endpoint exists but the business has no products.
     * Test specifically for when the cookie contains an ID belonging to a USER who is an administrator of the given business.
     *
     * @throws Exception Exception error
     */
    @Test
    void canRetrieveAllProductsWhenBusinessExistsWithBusinessAdministratorUserCookieAndWithNoProducts() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[]";

        // when
        List<Product> list = List.of();
        when(productRepository.findAllByBusinessId(1)).thenReturn(list);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));

        response = mvc.perform(get(String.format("/businesses/%d/productAll", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

}
