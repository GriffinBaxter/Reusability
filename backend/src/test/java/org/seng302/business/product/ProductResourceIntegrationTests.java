package org.seng302.business.product;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessRepository;
import org.seng302.business.BusinessType;
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

/**
 * ProductResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class ProductResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    private MockHttpServletResponse response;

    private final String productPayloadJson = "{\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.1f}";

    private String payloadJson;

    private final String expectedProductJson = "{\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.1f," +
            "\"created\":\"%s\"}";

    private String expectedJson;

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Product product;

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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );

        this.mvc = MockMvcBuilders.standaloneSetup(new ProductResource(
                productRepository, businessRepository, userRepository))
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
    public void canCreateProductWhenBusinessExistsAndDataValidWithBusinessAdministratorUserCookie() throws Exception {
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
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void canCreateProductWhenBusinessExistsAndDataValidWithDgaaCookie() throws Exception {
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
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void canCreateProductWhenBusinessExistsAndDataValidWithGaaCookie() throws Exception {
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
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void canCreateProductWithProductIdThatExistsForAnotherBusiness() throws Exception {
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void cantCreateProductWhenBusinessExistsButProductIdAlreadyExists() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.ofNullable(product));
        payloadJson = String.format(productPayloadJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice());

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
    public void cantCreateProductWhenBusinessExistsButDataIsInvalid() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.empty());
        payloadJson = String.format(productPayloadJson, "P", product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice());

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
    public void cantCreateProductWhenBusinessExistsAndDataValidWithNonAdminCookie() throws Exception {
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void cantCreateProductWhenBusinessExistsAndDataValidWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        Product newProduct = new Product(
                "NEW",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        payloadJson = String.format(productPayloadJson, newProduct.getProductId(), newProduct.getName(),
                newProduct.getDescription(), newProduct.getManufacturer(),
                newProduct.getRecommendedRetailPrice());
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
    public void cantCreateProductWhenBusinessDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        payloadJson = String.format(productPayloadJson, "PRO", "name", "desc", "manu", 30.00);

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
    public void canRetrieveProductsWhenBusinessExistsWithBusinessAdministratorUserCookie() throws Exception {
        // given
        given(userRepository.findById(3)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
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
    public void canRetrieveProductsWhenBusinessExistsWithDGAACookie() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl(list);
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
     * Tests that an OK status and a list of product payloads is received when the business ID in the
     * /businesses/{id}/products API endpoint exists.
     * Test specifically for when the cookie contains an ID belonging to a GAA.
     *
     * @throws Exception Exception error
     */
    @Test
    public void canRetrieveProductsWhenBusinessExistsWithGAACookie() throws Exception {
        // given
        given(userRepository.findById(2)).willReturn(Optional.ofNullable(gAA));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        expectedJson = "[" + String.format(expectedProductJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(), product.getRecommendedRetailPrice(),
                product.getCreated()) + "]";

        // when
        List<Product> list = List.of(product);
        Page<Product> pagedResponse = new PageImpl(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);
        when(productRepository.findProductsByBusinessId(1, paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /**
     * Tests that a NOT_ACCEPTABLE status is received when the business ID in the /businesses/{id}/products
     * API endpoint does not exist.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveProductsWhenBusinessDoesntExist() throws Exception {
        // given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(dGAA));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        when(businessRepository.findBusinessById(0)).thenReturn(Optional.empty());
        response = mvc.perform(get(String.format("/businesses/%d/products", 0))
                .param("orderBy", "productIdASC")
                .param("page", "0")
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
    public void cantRetrieveProductsWhenBusinessExistsWithNonExistingIdCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0")
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
    public void cantRetrieveProductsWhenBusinessExistsWithNonAdminUserCookie() throws Exception {
        // given
        given(userRepository.findById(4)).willReturn(Optional.ofNullable(anotherUser));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
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
     * /businesses/{id}/products API endpoint exists but there is no cookie.
     *
     * @throws Exception Exception error
     */
    @Test
    public void cantRetrieveProductsWhenBusinessExistsWithNoCookie() throws Exception {
        // given
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));
        expectedJson = "";

        // when
        response = mvc.perform(get(String.format("/businesses/%d/products", business.getId()))
                .param("orderBy", "productIdASC")
                .param("page", "0"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

}
