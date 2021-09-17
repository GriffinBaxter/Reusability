package org.seng302.steps;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import static org.assertj.core.api.Assertions.assertThat;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.seng302.controller.BusinessResource;
import org.seng302.controller.ImageResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.lenient;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.seng302.services.FileStorageService;
import org.seng302.view.outgoing.AddressPayload;
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

public class ModifyBusinessStepDefs {

    private MockMvc mvc;

    private MockMvc imageMvc;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductImageRepository productImageRepository;

    @MockBean
    private UserImageRepository userImageRepository;

    @MockBean
    private BusinessImageRepository businessImageRepository;

    private Business business;

    private User user;

    private User anotherUser;

    private Address address;

    private Address anotherAddress;

    private String payloadJson;

    private MockHttpServletResponse response;

    private Cookie cookie;

    private MockMultipartFile image;

    private MockMultipartFile anotherImage;

    private FileStorageService fileStorageService;

    private List<BusinessImage> businessImages;

    private BusinessImage primaryBusinessImage;

    private BusinessImage newPrimaryBusinessImage;

    @Before
    public void createMockMvc() throws Exception{
        addressRepository = mock(AddressRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        businessImageRepository = mock(BusinessImageRepository.class);
        fileStorageService = Mockito.mock(FileStorageService.class, withSettings().stubOnly());

        this.mvc = MockMvcBuilders.standaloneSetup(
                new BusinessResource(businessRepository, userRepository, addressRepository)
        ).build();
        this.imageMvc = MockMvcBuilders.standaloneSetup(new ImageResource(
                businessRepository, userRepository, productRepository, productImageRepository,
                userImageRepository, businessImageRepository, fileStorageService)
        ).build();
    }

    @Given("I am logged in as a primary admin of a business")
    public void iAmLoggedInAsAPrimaryAdminOfABusiness() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        anotherAddress = new Address("123", "new", "new", "new", "new","123","new");

        user = new User(
                "user",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "user@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        anotherUser = new User(
                "another",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "another@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.CHARITABLE_ORGANISATION,
                LocalDateTime.now(),
                user,
                "#",
                "123"
        );
        user.setBusinessesAdministeredObjects(List.of(business));

        cookie = new Cookie("JSESSIONID", user.getSessionUUID());
        primaryBusinessImage = new BusinessImage(
                1, business.getId(), "apples.jpg", "test/apples.jpg", true
        );
        newPrimaryBusinessImage = new BusinessImage(
                2, business.getId(), "Iphone13.jpg", "test/Iphone13.jpg", false
        );
        businessImages = new ArrayList<>();
    }

    @When("I try to modify all the attributes of a business")
    public void iTryToModifyAllTheAttributesOfABusiness() throws Exception{
        String session = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", session);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.of(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        AddressPayload newAddress = new AddressPayload(anotherAddress.getStreetNumber(), anotherAddress.getStreetName(), anotherAddress.getCity(), anotherAddress.getRegion(),
                anotherAddress.getCountry(), anotherAddress.getPostcode(), anotherAddress.getSuburb());
        payloadJson = "{" +
                "\"primaryAdministratorId\":" + anotherUser.getId() + "," +
                "\"name\":\"" + "new" + "\"," +
                "\"description\":\"" + "new" + "\"," +
                "\"address\":" + newAddress + "," +
                "\"businessType\":\"" + "ACCOMMODATION AND FOOD SERVICES" + "\"," +
                "\"currencySymbol\":\"" + "$" + "\"," +
                "\"currencyCode\":\"" + "NZD" + "\"" +
                "}";

        response = mvc.perform(put(String.format("/businesses/%d", business.getId())).cookie(cookie).content(payloadJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    }


    @Then("The business is modified")
    public void theBusinessIsModified() throws Exception{
        Business newBusiness = new Business(anotherUser.getId(), "new", "new", anotherAddress, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES, business.getCreated(), anotherUser, "$", "NZD");
        assertThat(newBusiness).hasToString(business.toString());
    }

    // ----------------------------------------------- Business Images -----------------------------------------------

    @And("I have a business image with filename of {string}.")
    public void iHaveABusinessImageWithFilenameOf(String fileName) throws IOException {
        if (image == null) {
            image = new MockMultipartFile("images", fileName,
                    MediaType.IMAGE_JPEG_VALUE, this.getClass().getResourceAsStream(fileName));
            assertThat(image.getOriginalFilename()).isEqualTo(fileName);
        } else {
            anotherImage = new MockMultipartFile("images", fileName,
                    MediaType.IMAGE_JPEG_VALUE, this.getClass().getResourceAsStream(fileName));
            assertThat(anotherImage.getOriginalFilename()).isEqualTo(fileName);
        }
    }

    @When("I upload this image for my business.")
    public void iUploadThisImageForMyBusiness() throws Exception {
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));

        lenient().when(fileStorageService.generateThumbnail(any(MultipartFile.class), anyString())).thenReturn(
                new ByteArrayInputStream("mockedThumbnailInputStream".getBytes()));
        lenient().when(fileStorageService.storeFile(any(InputStream.class), anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryBusinessImage.getFilename());

        when(businessImageRepository.findBusinessImageByBusinessIdAndIsPrimary(business.getId(), true))
                .thenReturn(businessImages);
        when(businessImageRepository.saveAndFlush(any(BusinessImage.class))).thenReturn(primaryBusinessImage);
        response = imageMvc.perform(multipart("/images").file(image).cookie(cookie)
                        .param("uncheckedImageType", "BUSINESS_IMAGE")
                        .param("businessId", String.valueOf(business.getId())))
                .andReturn().getResponse();
    }

    @Then("This business image will be stored.")
    public void thisBusinessImageWillBeStored() throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(String.format("{\"id\":%d}", primaryBusinessImage.getId()));
    }

    @And("There is no image for my business.")
    public void thereIsNoImageForMyBusiness() {
        assertThat(businessImages.size()).isZero();
    }

    @Then("The primary business image is {string}.")
    public void thePrimaryBusinessImageIs(String fileName) {
        BusinessImage primaryImage =
                primaryBusinessImage.getIsPrimary() ? primaryBusinessImage : newPrimaryBusinessImage;
        assertThat(primaryImage.getIsPrimary()).isTrue();
        assertThat(primaryImage.getFilename()).isEqualTo(fileName);
    }

    @When("I set the second business image be the primary business image.")
    public void iSetTheSecondBusinessImageBeThePrimaryBusinessImage() throws Exception {
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        List<BusinessImage> businessImages = List.of(primaryBusinessImage);
        when(businessImageRepository.findBusinessImageByBusinessIdAndIsPrimary(business.getId(), true))
                .thenReturn(businessImages);
        when(businessImageRepository.findById(newPrimaryBusinessImage.getId()))
                .thenReturn(Optional.of(newPrimaryBusinessImage));
        response = imageMvc.perform(put(String.format("/images/%d/makePrimary", newPrimaryBusinessImage.getId()))
                        .cookie(cookie)
                        .param("uncheckedImageType", "BUSINESS_IMAGE")
                        .param("businessId", String.valueOf(business.getId())))
                .andReturn().getResponse();
    }

    @Then("A thumbnail of this business image is automatically created.")
    public void aThumbnailOfThisBusinessImageIsAutomaticallyCreated() {
        assertThat(primaryBusinessImage.getThumbnailFilename()).isNotEmpty();
    }
}
