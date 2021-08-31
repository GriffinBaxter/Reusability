package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.ProductResource;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalProductArgumentException;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class AddBarcodeStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc userMVC;

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
    private ProductUpdateService productUpdateService;

    @Autowired
    @MockBean
    private ConversationRepository conversationRepository;

    @Autowired
    @MockBean
    private MessageRepository messageRepository;

    private MockHttpServletResponse response;

    private User user;
    private Address address;
    private Business business;
    private Product product;
    private Product anotherProduct;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String productPayloadFormat = "{\"id\":\"%s\"," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"manufacturer\":\"%s\"," +
            "\"recommendedRetailPrice\":%.1f," +
            "\"barcode\":\"%s\"}";

    private String productPayload;
    private String returnProductPayload;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        businessRepository = mock(BusinessRepository.class);
        productRepository = mock(ProductRepository.class);
        productUpdateService = mock(ProductUpdateService.class);

        this.productMVC = MockMvcBuilders.standaloneSetup(new ProductResource(
                productRepository, businessRepository, userRepository, productUpdateService
        )).build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(
                userRepository, addressRepository, conversationRepository, messageRepository)).build();
    }

    @Given("I am logged in as a business administrator for an existing business.")
    public void i_am_logged_in_as_a_business_administrator_for_an_existing_business() throws Exception {
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
                user
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

    @Given("I have an existing product with Product Id {string}, name {string}, description {string}, manufacturer {string}, recommendPrice {string} and barcode {string}")
    public void i_have_an_existing_product_with_product_id_name_description_manufacturer_recommend_price_and_barcode(String id, String name, String description, String manufacturer, String recommendPrice, String barcode) throws IllegalProductArgumentException {
        product = new Product(
                id,
                business,
                name,
                description,
                manufacturer,
                Double.parseDouble(recommendPrice),
                barcode
        );
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId())).willReturn(Optional.ofNullable(product));
    }

    @When("I create a product with Product Id {string}, name {string}, description {string}, manufacturer {string}, recommendPrice {string} and barcode {string}")
    public void i_create_a_product_with_product_id_name_description_manufacturer_recommend_price_and_barcode(String id, String name, String description, String manufacturer, String recommendPrice, String barcode) throws Exception {
        product = new Product(
                id,
                business,
                name,
                description,
                manufacturer,
                Double.parseDouble(recommendPrice),
                barcode
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        productPayload = String.format(productPayloadFormat, product.getProductId(), product.getName(), product.getDescription(),
                product.getManufacturer(), product.getRecommendedRetailPrice(), product.getBarcode());

        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.empty());

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        response = productMVC.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(productPayload)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I modify the product to have a barcode of {string}")
    public void i_modify_the_product_to_have_a_barcode_of(String barcode) throws Exception {
        anotherProduct = new Product(
                product.getProductId(),
                business,
                product.getName(),
                product.getDescription(),
                product.getManufacturer(),
                product.getRecommendedRetailPrice(),
                barcode
        );

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(1)).willReturn(Optional.ofNullable(business));

        productPayload = String.format(productPayloadFormat, anotherProduct.getProductId(), anotherProduct.getName(), anotherProduct.getDescription(),
                anotherProduct.getManufacturer(), anotherProduct.getRecommendedRetailPrice(), barcode);

        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), 1)).willReturn(Optional.ofNullable(product));

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(anotherProduct);

        response = productMVC.perform(put(String.format("/businesses/%d/products/%s", product.getBusinessId(), product.getProductId()))
                .contentType(MediaType.APPLICATION_JSON).content(productPayload)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
        ).andReturn().getResponse();
    }

    @Then("I expect the product to be successfully created")
    public void i_expect_the_product_to_be_successfully_created() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Then("I expect the product to successfully be modified")
    public void i_expect_the_product_to_successfully_be_modified() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
