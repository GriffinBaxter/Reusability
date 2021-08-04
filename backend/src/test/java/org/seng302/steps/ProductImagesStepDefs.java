package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seng302.Main;
import org.seng302.controller.ImageResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalProductArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.*;
import org.seng302.model.repository.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.controller.InventoryItemResource;
import org.seng302.model.enums.Role;
import org.seng302.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
    private MockMultipartFile jpgImage;
    private MockMultipartFile jpegImage;
    private MockMultipartFile pngImage;
    private MockMultipartFile gifImage;
    private MockMultipartFile otherFile;
    private Image primaryImage;
    private Image nonPrimaryImage;
    private Image newImage;
    private String productId;
    private Integer businessId;
    private String sessionToken;

    @Before
    public void createMockMvc() throws IOException {
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        imageRepository = mock(ImageRepository.class);
        fileStorageService = Mockito.mock(FileStorageService.class, withSettings().stubOnly().useConstructor("test-images"));
        this.mvc = MockMvcBuilders.standaloneSetup(new ImageResource(businessRepository, userRepository, productRepository, imageRepository, fileStorageService)).build();
        jpgImage = new MockMultipartFile("images", "testImage.jpg", MediaType.IMAGE_JPEG_VALUE, this.getClass().getResourceAsStream("testImage.jpg"));

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

        primaryImage = new Image(1, productId, businessId, filename, filename, true);

        when(userRepository.findBySessionUUID(sessionToken)).thenReturn(Optional.of(user));
        when(businessRepository.findBusinessById(businessId)).thenReturn(Optional.of(business));
        when(productRepository.findProductByIdAndBusinessId(productId, businessId)).thenReturn(Optional.of(product));
        lenient().when(fileStorageService.generateThumbnail(any(MultipartFile.class), anyString())).thenReturn(
                new ByteArrayInputStream("mockedThumbnailInputStream".getBytes())
        );
        lenient().when(fileStorageService.storeFile(any(InputStream.class), anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryImage.getFilename());
        List<Image> images = new ArrayList<Image>();

        when(imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true)).thenReturn(images);
        when(imageRepository.saveAndFlush(any(Image.class))).thenReturn(primaryImage);

        response = mvc.perform(multipart(String.format("/businesses/%d/products/%s/images", businessId, productId)).file(jpgImage).cookie(cookie)).andReturn().getResponse();

    }

    @Then("this image is stored and displayed")
    public void this_image_is_stored_and_displayed() throws UnsupportedEncodingException {

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(String.format("{\"id\":%d}", primaryImage.getId()));

    }

    @Given("the primary image of this product is {string}")
    public void the_primary_image_of_this_product_is(String filename) {

        primaryImage = new Image(1, productId, businessId, filename, filename, true);

        primaryImage.setIsPrimary(true);
        List <Image> primaryImages = new ArrayList<>();
        primaryImages.add(primaryImage);
        assertThat(primaryImage.getFilename()).isEqualTo(filename);
        given(imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true)).willReturn(primaryImages);

    }

    @Given("it has a non-primary image of {string}")
    public void it_has_a_non_primary_image_of(String filename) {

        nonPrimaryImage = new Image(2, productId, businessId, filename, filename, false);
        fileStorageService = Mockito.mock(FileStorageService.class, withSettings().stubOnly().useConstructor("test-images"));

        this.mvc = MockMvcBuilders.standaloneSetup(new ImageResource(businessRepository, userRepository, productRepository, imageRepository, fileStorageService)).build();


        nonPrimaryImage.setIsPrimary(false);
        List <Image> nonPrimaryImages = new ArrayList<>();
        nonPrimaryImages.add(nonPrimaryImage);
        given(imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, false)).willReturn(nonPrimaryImages);

    }

    @When("I change the primary image to {string}")
    public void i_change_the_primary_image_to(String filename) throws Exception {

        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        List<Image> images = List.of(primaryImage);
        newImage = new Image(2, productId, businessId, filename, filename, false);

        when(imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true)).thenReturn(images);
        when(imageRepository.findImageByIdAndBussinesIdAndProductId(newImage.getId(), businessId, productId)).thenReturn(Optional.of(newImage));

        response = mvc.perform(put(String.format("/businesses/%d/products/%s/images/%d/makeprimary", businessId, productId, newImage.getId())).cookie(cookie)).andReturn().getResponse();

    }

    @Then("the primary image is updated")
    public void the_primary_image_is_updated() {

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(newImage.getIsPrimary()).isTrue();
        assertThat(primaryImage.getIsPrimary()).isFalse();

    }

    @Given("this business only has the image of {string}")
    public void this_business_only_has_the_image_of(String filename) {

        primaryImage = new Image(1, productId, businessId, "storage/test-images/" + filename , filename, true);
        List <Image> primaryImages = List.of(primaryImage);
        given(imageRepository.findImageByBussinesIdAndProductId(businessId, productId)).willReturn(primaryImages);

    }

    @When("this file is deleted")
    public void this_file_is_deleted() throws Exception {

        sessionToken = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", sessionToken);

        lenient().when(fileStorageService.deleteFile(anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryImage.getFilename());
        List<Image> images = List.of(primaryImage);
        when(imageRepository.findImageByBussinesIdAndProductIdAndIsPrimary(businessId, productId, true)).thenReturn(images);
        when(imageRepository.findImageByIdAndBussinesIdAndProductId(primaryImage.getId(), businessId, productId)).thenReturn(Optional.of(primaryImage));
        response = mvc.perform(delete(String.format("/businesses/%d/products/%s/images/%d", businessId, productId, primaryImage.getId())).cookie(cookie)).andReturn().getResponse();

    }

    @Then("this business has no images")
    public void this_business_has_no_images() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

}
