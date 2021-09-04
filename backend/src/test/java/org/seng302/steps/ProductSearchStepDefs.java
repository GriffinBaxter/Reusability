package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.ProductResource;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/**
 * Product search step definitions class
 */
public class ProductSearchStepDefs extends CucumberSpringConfiguration {

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
    private ProductUpdateService productUpdateService;

    private User user;
    private Business business;
    private Product product1;
    private Product product2;
    private Product product3;

    private MockHttpServletResponse response;

    @Before
    public void setupMVC() {
        userRepository = mock(UserRepository.class);
        businessRepository = mock(BusinessRepository.class);
        productUpdateService = mock(ProductUpdateService.class);
        productRepository = mock(ProductRepository.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new ProductResource(productRepository, businessRepository, userRepository, productUpdateService)).build();
    }

    @Given("I am logged in as a user with id {int} who is an administrator of a business with id {int}")
    public void iAmLoggedInAsAUserWithIdWhoIsAnAdministratorOfABusinessWithId(int userId, int businessId) throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

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
        user.setId(userId);
        user.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                userId,
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user
        );
        business.setId(businessId);

        user.setBusinessesAdministeredObjects(List.of(business));

        assertThat(business.isAnAdministratorOfThisBusiness(user)).isTrue();
    }

    @When("I search the product catalogue of the business with id {int}")
    public void iSearchTheProductCatalogueOfTheBusinessWithId(int businessId) throws Exception {
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(businessId)).willReturn(Optional.ofNullable(business));

        product1 = new Product(
                "PROD1",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        product2 = new Product(
                "PROD2",
                business,
                "AnotherProduct",
                "Watties",
                "Manufacturer2",
                22.00,
                "036000291452"
        );

        product3 = new Product(
                "PROD3",
                business,
                "AnotherProduct2",
                "Beans",
                "Watties",
                22.00,
                "036000291452"
        );

        List<Product> list = List.of(product1, product2, product3);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(productRepository.findAllProductsByBusinessIdAndIncludedFields(List.of(""), List.of("name"), businessId, paging)).thenReturn(pagedResponse);

        response = mvc.perform(get(String.format("/businesses/%d/products", businessId))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                        .andReturn().getResponse();
    }

    @Then("I receive a 200 response and a payload with a list of products")
    public void iReceiveA200ResponseAndAPayloadWithAListOfProducts() throws Exception {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(List.of(product1.convertToPayload(), product2.convertToPayload(), product3.convertToPayload()).toString().replace(", ", ","));
    }

    @Given("I am logged in as a user with id {int} who is not an administrator of a business with id {int}")
    public void iAmLoggedInAsAUserWithIdWhoIsNotAnAdministratorOfABusinessWithId(int userId, int businessId) throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

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
        user.setId(userId);
        user.setSessionUUID(User.generateSessionUUID());

        User anotherUser = new User("first",
                "last",
                "middle",
                "nick",
                "bio",
                "emaile@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(1);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                anotherUser.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                anotherUser
        );
        business.setId(businessId);

        assertThat(business.isAnAdministratorOfThisBusiness(user)).isFalse();
    }

    @Then("I receive a 403 response")
    public void iReceiveA403Response() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    @Given("There is a business with id {int} and it has products")
    public void thereIsABusinessWithIdAndItHasProducts(int businessId) throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

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
        business.setId(businessId);

        user.setBusinessesAdministeredObjects(List.of(business));

        product1 = new Product(
                "PROD1",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        product2 = new Product(
                "PROD2",
                business,
                "AnotherProduct",
                "Watties",
                "Manufacturer2",
                22.00,
                "036000291452"
        );

        product3 = new Product(
                "PROD3",
                business,
                "AnotherProduct2",
                "Beans",
                "Watties",
                22.00,
                "036000291452"
        );
    }

    @When("I search the product catalogue of the business with id {int} using fields {string} and {string} and query {string}")
    public void iSearchTheProductCatalogueOfTheBusinessWithIdUsingFieldsAndQuery(int businessId, String field1, String field2, String query) throws Exception {
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(businessId)).willReturn(Optional.ofNullable(business));

        List<Product> list = List.of(product2, product3);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(productRepository.findAllProductsByBusinessIdAndIncludedFields(List.of(query), List.of(field1, field2), businessId, paging)).thenReturn(pagedResponse);

        response = mvc.perform(get(String.format("/businesses/%d/products", businessId))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("searchQuery", query)
                        .param("searchBy", field1, field2))
                        .andReturn().getResponse();
    }

    @Then("I receive a 200 response and a payload with a list of products with manufacturer or description matching \"Watties\"")
    public void iReceiveA200ResponseAndAPayloadWithAListOfProductsWithManufacturerOrDescriptionMatching() throws Exception {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(List.of(product2.convertToPayload(), product3.convertToPayload()).toString().replace(", ", ","));
    }

    @When("I search the product catalogue of the business with id {int} using no fields and query {string}")
    public void iSearchTheProductCatalogueOfTheBusinessWithIdUsingNoFieldsAndQuery(int businessId, String query) throws Exception {
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(businessId)).willReturn(Optional.ofNullable(business));

        List<Product> list = List.of(product1);
        Page<Product> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.asc("id").ignoreCase()).and(Sort.by(Sort.Order.asc("name").ignoreCase()));
        Pageable paging = PageRequest.of(0, 5, sort);

        when(productRepository.findAllProductsByBusinessIdAndIncludedFields(List.of(query), List.of("name"), businessId, paging)).thenReturn(pagedResponse);

        response = mvc.perform(get(String.format("/businesses/%d/products", businessId))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("searchQuery", query))
                        .andReturn().getResponse();
    }

    @Then("I receive a 200 response and a product with the name \"Beans\"")
    public void iReceiveA200ResponseAndAProductWithTheName() throws Exception {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(List.of(product1.convertToPayload()).toString());
    }
}
