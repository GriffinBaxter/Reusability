package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mock;
import org.seng302.controller.ImageResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalProductArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.repository.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.InventoryItem;
import org.seng302.controller.InventoryItemResource;
import org.seng302.model.Product;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
import org.seng302.services.FileStorageService;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

public class ProductImagesStepDefs {

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
    private ImageRepository imageRepository;

    @Autowired
    @MockBean
    private FileStorageService fileStorageService;

    private Address address;

    private User user;

    private Business business;

    private Product product;

    private MockHttpServletResponse response;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";

    private final String expectedUserIdJson = "{\"userId\":%s}";

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
            "\"created\":\"%s\"," +
            "\"images\":[]}";

    private String expectedJson;

    @Before
    public void createMockMvc() {
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new ImageResource(businessRepository, userRepository, productRepository, imageRepository, fileStorageService)).build();
    }

    @Given("I am logged in as the administrator with first name {string} and last name {string} of the existing business with name {string}")
    public void i_am_logged_in_as_the_administrator_with_first_name_and_last_name_of_the_existing_business_with_name(String firstName, String lastName, String businessName) throws Exception {

        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(firstName,
                lastName,
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
        user.setSessionUUID(User.generateSessionUUID());

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        Assertions.assertTrue(userRepository.findBySessionUUID(user.getSessionUUID()).isPresent());

        business = new Business(
                user.getId(),
                businessName,
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);

        //TODO: assert user is admin of business and business exists

    }

    @Given("this business has the product with the product name of {string}")
    public void this_business_has_the_product_with_the_product_name_of(String productName) throws Exception {

        product = new Product(
                "NEW",
                business,
                productName,
                "NewDesc",
                "Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0))
        );

        payloadJson = String.format(productPayloadJson, product.getProductId(), product.getName(),
                product.getDescription(), product.getManufacturer(),
                product.getRecommendedRetailPrice());
        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.empty());

        when(productRepository.save(any(Product.class))).thenReturn(product);
        response = mvc.perform(post(String.format("/businesses/%d/products", business.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // TODO assert product exists for business

    }

    @When("I upload an image for this product with the filename of {string}")
    public void i_upload_an_image_for_this_product_with_the_filename_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("this image is stored and displayed")
    public void this_image_is_stored_and_displayed() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("the primary image of this product is {string}")
    public void the_primary_image_of_this_product_is(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("it has a non-primary image of {string}")
    public void it_has_a_non_primary_image_of(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("I change the primary image to {string}")
    public void i_change_the_primary_image_to(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("{string} becomes the primary image for the product {string}")
    public void becomes_the_primary_image_for_the_product(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("{string} only has the image of {string}")
    public void only_has_the_image_of(String string, String string2) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @When("{string} is deleted")
    public void is_deleted(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("Apples has no images")
    public void apples_has_no_images() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

}
