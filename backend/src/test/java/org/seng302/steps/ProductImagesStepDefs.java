package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seng302.controller.ImageResource;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.services.FileStorageService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ProductImagesStepDefs {

    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductImageRepository productImageRepository;

    @MockBean
    private UserImageRepository userImageRepository;

    @MockBean
    private BusinessImageRepository businessImageRepository;

    @MockBean
    private FileStorageService fileStorageService;

    private Address address;
    private User user;
    private Business business;
    private Product product;
    private MockHttpServletResponse response;
    private MockMultipartFile jpgImage;
    private ProductImage primaryProductImage;
    private ProductImage nonPrimaryProductImage;
    private ProductImage newProductImage;
    private String productId;
    private Integer businessId;
    private String sessionToken;

    private String imageReturnPayload = "{\"id\":%d," +
            "\"filename\":\"%s\"," +
            "\"isPrimary\":%b," +
            "\"thumbnailFilename\":\"%s\"}";

    @Before
    public void createMockMvc() throws IOException {
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        productImageRepository = mock(ProductImageRepository.class);
        userImageRepository = mock(UserImageRepository.class);
        fileStorageService = Mockito.mock(FileStorageService.class,
                withSettings().stubOnly().useConstructor("test-images"));
        this.mvc = MockMvcBuilders.standaloneSetup(new ImageResource(
                businessRepository, userRepository, productRepository, productImageRepository,
                userImageRepository, businessImageRepository, fileStorageService)
        ).build();
        jpgImage = new MockMultipartFile("images", "testImage.jpg",
                MediaType.IMAGE_JPEG_VALUE, this.getClass().getResourceAsStream("testImage.jpg"));

    }

    @Given("I am logged in as the administrator with first name {string} and last name {string} of the existing business with name {string}")
    public void i_am_logged_in_as_the_administrator_with_first_name_and_last_name_of_the_existing_business_with_name(String firstName,
                                                                                                                     String lastName,
                                                                                                                     String businessName) throws Exception {

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

        business = new Business(
                user.getId(),
                businessName,
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user,
                "$",
                "NZD"
        );
        business.setId(2);
        businessId = business.getId();

        given(userRepository.findById(user.getId())).willReturn(Optional.ofNullable(user));
        Assertions.assertTrue(userRepository.findById(user.getId()).isPresent());

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        Assertions.assertTrue(userRepository.findBySessionUUID(user.getSessionUUID()).isPresent());

        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        Assertions.assertTrue(userRepository.findBySessionUUID(user.getSessionUUID()).isPresent());

        Assert.assertTrue(business.isAnAdministratorOfThisBusiness(user));

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
                ""
        );

        productId = product.getProductId();

        given(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()))
                .willReturn(Optional.ofNullable(product));
        Assert.assertTrue(productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId()).isPresent());

    }

    @When("I upload an image for this product with the filename of {string}")
    public void i_upload_an_image_for_this_product_with_the_filename_of(String filename) throws Exception {

        String sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        primaryProductImage = new ProductImage(1, productId, businessId, filename, filename, true);

        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.of(user));
        when(businessRepository.findBusinessById(businessId)).thenReturn(Optional.of(business));
        when(productRepository.findProductByIdAndBusinessId(productId, businessId)).thenReturn(Optional.of(product));
        lenient().when(fileStorageService.generateThumbnail(any(MultipartFile.class), anyString())).thenReturn(
                new ByteArrayInputStream("mockedThumbnailInputStream".getBytes())
        );
        lenient().when(fileStorageService.storeFile(any(InputStream.class), anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryProductImage.getFilename());
        List<ProductImage> productImages = new ArrayList<>();

        when(productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true))
                .thenReturn(productImages);
        when(productImageRepository.saveAndFlush(any(ProductImage.class))).thenReturn(primaryProductImage);

        response = mvc.perform(multipart("/images").file(jpgImage).cookie(cookie)
                        .param("uncheckedImageType", "PRODUCT_IMAGE")
                        .param("userId", "")
                        .param("businessId", String.valueOf(businessId))
                        .param("productId", productId))
                .andReturn().getResponse();

    }

    @Then("this image is stored and displayed")
    public void this_image_is_stored_and_displayed() throws UnsupportedEncodingException {
        String expectedResponse = String.format(imageReturnPayload, primaryProductImage.getId(), primaryProductImage.getFilename(),
                primaryProductImage.getIsPrimary(), primaryProductImage.getThumbnailFilename());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);

    }

    @Given("the primary image of this product is {string}")
    public void the_primary_image_of_this_product_is(String filename) {

        primaryProductImage = new ProductImage(1, productId, businessId, filename, filename, true);

        primaryProductImage.setIsPrimary(true);
        List<ProductImage> primaryProductImages = new ArrayList<>();
        primaryProductImages.add(primaryProductImage);
        assertThat(primaryProductImage.getFilename()).isEqualTo(filename);
        given(productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true))
                .willReturn(primaryProductImages);

    }

    @Given("it has a non-primary image of {string}")
    public void it_has_a_non_primary_image_of(String filename) {

        nonPrimaryProductImage = new ProductImage(2, productId, businessId, filename, filename, false);
        fileStorageService = Mockito.mock(FileStorageService.class, withSettings().stubOnly().useConstructor("test-images"));

        this.mvc = MockMvcBuilders.standaloneSetup(new ImageResource(
                businessRepository, userRepository, productRepository, productImageRepository,
                userImageRepository, businessImageRepository, fileStorageService)
        ).build();

        nonPrimaryProductImage.setIsPrimary(false);
        List<ProductImage> nonPrimaryProductImages = new ArrayList<>();
        nonPrimaryProductImages.add(nonPrimaryProductImage);
        given(productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, false))
                .willReturn(nonPrimaryProductImages);

    }

    @When("I change the primary image to {string}")
    public void i_change_the_primary_image_to(String filename) throws Exception {

        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        List<ProductImage> productImages = List.of(primaryProductImage);
        newProductImage = new ProductImage(2, productId, businessId, filename, filename, false);

        when(productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true))
                .thenReturn(productImages);
        when(productImageRepository.findById(newProductImage.getId())).thenReturn(Optional.of(newProductImage));

        response = mvc.perform(put(String.format("/images/%d/makePrimary", newProductImage.getId())).cookie(cookie)
                        .param("uncheckedImageType", "PRODUCT_IMAGE")
                        .param("userId", "")
                        .param("businessId", String.valueOf(businessId))
                        .param("productId", productId))
                .andReturn().getResponse();
    }

    @Then("the primary image is updated")
    public void the_primary_image_is_updated() {

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(newProductImage.getIsPrimary()).isTrue();
        assertThat(primaryProductImage.getIsPrimary()).isFalse();

    }

    @Given("this business only has the image of {string}")
    public void this_business_only_has_the_image_of(String filename) {

        primaryProductImage = new ProductImage(1, productId, businessId, "storage/test-images/" + filename,
                filename, true);
        List<ProductImage> primaryProductImages = List.of(primaryProductImage);
        given(productImageRepository.findProductImageByBusinessIdAndProductId(businessId, productId))
                .willReturn(primaryProductImages);
    }

    @When("this file is deleted")
    public void this_file_is_deleted() throws Exception {

        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        lenient().when(fileStorageService.deleteFile(anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryProductImage.getFilename());
        List<ProductImage> productImages = List.of(primaryProductImage);
        when(productImageRepository.findProductImageByBusinessIdAndProductIdAndIsPrimary(businessId, productId, true))
                .thenReturn(productImages);
        when(productImageRepository.findProductImageByIdAndBusinessIdAndProductId(primaryProductImage.getId(), businessId, productId))
                .thenReturn(Optional.of(primaryProductImage));

        response = mvc.perform(delete(String.format("/images/%d", primaryProductImage.getId())).cookie(cookie)
                .param("uncheckedImageType", "PRODUCT_IMAGE")
                .param("userId", "")
                .param("businessId", String.valueOf(businessId))
                .param("productId", productId))
                .andReturn().getResponse();
    }

    @Then("this business has no images")
    public void this_business_has_no_images() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
