package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.mockito.Mockito;
import org.seng302.controller.ImageResource;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.User;
import org.seng302.model.UserImage;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.seng302.services.FileStorageService;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

/**
 * Modify user profile step definitions class
 */
public class ModifyUserStepDefs {

    private MockMvc userMvc;

    private MockMvc imageMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

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

    private MockMultipartFile image;

    private MockMultipartFile anotherImage;

    private FileStorageService fileStorageService;

    private List<UserImage> userImages;

    private final String modifiedUserPayload = "{\n" +
            "\"firstName\": \"%s\",\n" +
            "\"lastName\": \"%s\",\n" +
            "\"middleName\": \"%s\",\n" +
            "\"nickname\": \"%s\",\n" +
            "\"bio\": \"%s\",\n" +
            "\"email\": \"%s\",\n" +
            "\"dateOfBirth\": \"%s\",\n" +
            "\"phoneNumber\": \"%s\",\n" +
            "\"homeAddress\": {\n" +
            "\"streetNumber\": \"%s\",\n" +
            "\"streetName\": \"%s\",\n" +
            "\"suburb\": \"%s\",\n" +
            "\"city\": \"%s\",\n" +
            "\"region\": \"%s\",\n" +
            "\"country\": \"%s\",\n" +
            "\"postcode\": \"%s\"\n" +
            "},\n" +
            "\"currentPassword\": \"%s\",\n" +
            "\"newPassword\": \"%s\"\n" +
            "}";

    private String imageReturnPayload = "{\"id\":%d," +
            "\"filename\":\"%s\"," +
            "\"isPrimary\":%b," +
            "\"thumbnailFilename\":\"%s\"}";

    private MockHttpServletResponse response;

    private User user;

    private User anotherUser;

    private Address address;

    private Cookie cookie;

    private String password;

    private UserImage primaryUserImage;

    private UserImage newPrimaryUserImage;

    private Integer userId;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        businessRepository = mock(BusinessRepository.class);
        productRepository = mock(ProductRepository.class);
        productImageRepository = mock(ProductImageRepository.class);
        userImageRepository = mock(UserImageRepository.class);
        fileStorageService = Mockito.mock(FileStorageService.class, withSettings().stubOnly());

        this.userMvc = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();
        this.imageMvc = MockMvcBuilders.standaloneSetup(new ImageResource(
                businessRepository, userRepository, productRepository, productImageRepository,
                userImageRepository, businessImageRepository, fileStorageService)
        ).build();
    }


    @Given("I am logged in as an user.")
    public void i_am_logged_in_as_an_user() throws IllegalUserArgumentException, IllegalAddressArgumentException {
        password = "Password123!";
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        user = new User("John",
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
        user.setId(2);
        user.setSessionUUID(User.generateSessionUUID());

        anotherUser = new User("first",
                "last",
                "middle",
                "nick",
                "bio",
                "Jeff99@email.com",
                LocalDate.of(2021, 1, 1).minusYears(13),
                "123456789",
                address,
                password,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        cookie = new Cookie("JSESSIONID", user.getSessionUUID());
        userId = user.getId();
        primaryUserImage = new UserImage(1, userId, "apples.jpg", "test/apples.jpg", true);
        newPrimaryUserImage = new UserImage(2, userId, "Iphone13.jpg", "test/Iphone13.jpg", false);
        userImages = new ArrayList<>();
    }

    @Given("I have firstname {string}, lastname {string}, middle name {string}, nickname {string}, bio {string},email {string}, date of birth {string}, phone number {string}, password {string}.")
    public void i_have_firstname_lastname_middle_name_nickname_bio_email_date_of_birth_phone_number_password(String firstName,
                                                                                                             String lastName,
                                                                                                             String middleName,
                                                                                                             String nickName,
                                                                                                             String bio,
                                                                                                             String email,
                                                                                                             String dateOfBirth,
                                                                                                             String phoneNumber,
                                                                                                             String password) {
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getMiddleName()).isEqualTo(middleName);
        assertThat(user.getNickname()).isEqualTo(nickName);
        assertThat(user.getBio()).isEqualTo(bio);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(user.verifyPassword(password)).isTrue();

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to change my profile as firstname {string}, lastname {string}, middle name {string}, nickname {string}, bio {string},email {string}, date of birth {string}, phone number {string}, current password {string}, new password {string}.")
    public void i_try_to_change_my_profile_as_firstname_lastname_middle_name_nickname_bio_email_date_of_birth_phone_number_password(String firstName,
                                                                                                                                    String lastName,
                                                                                                                                    String middleName,
                                                                                                                                    String nickName,
                                                                                                                                    String bio,
                                                                                                                                    String email,
                                                                                                                                    String dateOfBirth,
                                                                                                                                    String phoneNumber,
                                                                                                                                    String currentPassword,
                                                                                                                                    String newPassword) throws Exception {
        String payload = String.format(modifiedUserPayload, firstName, lastName, middleName, nickName, bio, email,
                dateOfBirth, phoneNumber, address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), address.getCountry(), address.getPostcode(), currentPassword, newPassword);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Then("My profile been successfully modified as firstname {string}, lastname {string}, middle name {string}, nickname {string}, bio {string},email {string}, date of birth {string}, phone number {string}, password {string}.")
    public void my_profile_been_successfully_modified_as_firstname_lastname_middle_name_nickname_bio_email_date_of_birth_phone_number_password(String firstName,
                                                                                                                                               String lastName,
                                                                                                                                               String middleName,
                                                                                                                                               String nickName,
                                                                                                                                               String bio,
                                                                                                                                               String email,
                                                                                                                                               String dateOfBirth,
                                                                                                                                               String phoneNumber,
                                                                                                                                               String password) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getMiddleName()).isEqualTo(middleName);
        assertThat(user.getNickname()).isEqualTo(nickName);
        assertThat(user.getBio()).isEqualTo(bio);
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(user.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(user.verifyPassword(password)).isTrue();
    }

    @Given("My address is {string} {string}, {string} {string}, {string}, my region is {string} and my post code is {string}.")
    public void my_address_is_my_region_is_and_my_post_code_is(String streetNumber,
                                                               String streetName,
                                                               String suburb,
                                                               String city,
                                                               String country,
                                                               String region,
                                                               String postcode) {
        assertThat(user.getHomeAddress().getStreetNumber()).isEqualTo(streetNumber);
        assertThat(user.getHomeAddress().getStreetName()).isEqualTo(streetName);
        assertThat(user.getHomeAddress().getCity()).isEqualTo(city);
        assertThat(user.getHomeAddress().getRegion()).isEqualTo(region);
        assertThat(user.getHomeAddress().getCountry()).isEqualTo(country);
        assertThat(user.getHomeAddress().getPostcode()).isEqualTo(postcode);
        assertThat(user.getHomeAddress().getSuburb()).isEqualTo(suburb);

        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to change my address as {string} {string}, {string} {string}, {string}, and region be {string} and post code be {string}.")
    public void i_try_to_change_my_address_as_and_region_be_and_post_code_be(String streetNumber,
                                                                             String streetName,
                                                                             String suburb,
                                                                             String city,
                                                                             String country,
                                                                             String region,
                                                                             String postcode) throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), streetNumber, streetName, suburb, city, region, country, postcode, password,
                password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Then("My address been successfully modified as {string} {string}, {string} {string}, {string}, and region is {string} and post code is {string}.")
    public void my_address_been_successfully_modified_as_and_region_is_and_post_code_is(String streetNumber,
                                                                                        String streetName,
                                                                                        String suburb,
                                                                                        String city,
                                                                                        String country,
                                                                                        String region,
                                                                                        String postcode) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(user.getHomeAddress().getStreetNumber()).isEqualTo(streetNumber);
        assertThat(user.getHomeAddress().getStreetName()).isEqualTo(streetName);
        assertThat(user.getHomeAddress().getCity()).isEqualTo(city);
        assertThat(user.getHomeAddress().getRegion()).isEqualTo(region);
        assertThat(user.getHomeAddress().getCountry()).isEqualTo(country);
        assertThat(user.getHomeAddress().getPostcode()).isEqualTo(postcode);
        assertThat(user.getHomeAddress().getSuburb()).isEqualTo(suburb);
    }

    @Given("My email address is {string}.")
    public void my_email_address_is(String email) {
        assertThat(user.getEmail()).isEqualTo(email);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to modify email address become {string}.")
    public void i_try_to_modify_email_address_become(String email) throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), email, user.getDateOfBirth(),
                user.getPhoneNumber(), address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), address.getCountry(), address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Then("A BAD_REQUEST stats return and the message say {string}.")
    public void a_bad_request_stats_return_and_the_message_say(String errorMessage) {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo(errorMessage);
    }

    @Given("My email address is {string}, and email {string} been use by an user.")
    public void my_email_address_is_and_email_been_use_by_an_user(String myEmail, String usedEmail) {
        assertThat(user.getEmail()).isEqualTo(myEmail);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail(usedEmail)).willReturn(Optional.ofNullable(anotherUser));
    }

    @Given("My bio is {string}.")
    public void my_bio_is(String bio) {
        assertThat(user.getBio()).isEqualTo(bio);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to modify bio by repeat {string} 30 times.")
    public void i_try_to_modify_it_longer_than_limits_characters(String bio) throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), bio.repeat(30), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), address.getCountry(), address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Given("My date of birth is {string}.")
    public void my_date_of_birth_is(String dateOfBirth) {
        assertThat(user.getDateOfBirth()).isEqualTo(dateOfBirth);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to modify date of birth become {string}.")
    public void i_try_to_modify_date_of_birth_become(String dateOfBirth) throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), dateOfBirth,
                user.getPhoneNumber(), address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), address.getCountry(), address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Given("My first name is {string}.")
    public void my_first_name_is(String firstName) {
        assertThat(user.getFirstName()).isEqualTo(firstName);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to modify first name become null.")
    public void i_try_to_modify_first_name_become_null() throws Exception {
        String payload = String.format(modifiedUserPayload, "", user.getLastName(), user.getMiddleName(),
                user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(), user.getPhoneNumber(),
                address.getStreetNumber(), address.getStreetName(), address.getSuburb(), address.getCity(),
                address.getRegion(), address.getCountry(), address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @When("I try to modify email address become null.")
    public void i_try_to_modify_email_address_become_null() throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), "", user.getDateOfBirth(),
                user.getPhoneNumber(), address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), address.getCountry(), address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Given("My country is {string}.")
    public void my_country_is(String country) {
        assertThat(user.getHomeAddress().getCountry()).isEqualTo(country);
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.of(user));
        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userRepository.findByEmail("bob@email.com")).willReturn(Optional.empty());
    }

    @When("I try to modify country become null.")
    public void i_try_to_modify_country_become_null() throws Exception {
        String payload = String.format(modifiedUserPayload, user.getFirstName(), user.getLastName(),
                user.getMiddleName(), user.getNickname(), user.getBio(), user.getEmail(), user.getDateOfBirth(),
                user.getPhoneNumber(), address.getStreetNumber(), address.getStreetName(), address.getSuburb(),
                address.getCity(), address.getRegion(), "", address.getPostcode(), password, password);

        response = userMvc.perform(put(String.format("/users/%d/profile", user.getId()))
                .contentType(MediaType.APPLICATION_JSON).content(payload)
                .cookie(cookie)).andReturn().getResponse();
    }

    @Given("I have a image with filename of {string}.")
    public void i_have_a_image_with_filename_of(String fileName) throws IOException {
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

    @When("I upload this image for myself.")
    public void i_upload_this_image_for_myself() throws Exception {
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.ofNullable(user));

        lenient().when(fileStorageService.generateThumbnail(any(MultipartFile.class), anyString())).thenReturn(
                new ByteArrayInputStream("mockedThumbnailInputStream".getBytes()));
        lenient().when(fileStorageService.storeFile(any(InputStream.class), anyString())).thenReturn(true);
        lenient().when(fileStorageService.getPathString(anyString())).thenReturn(primaryUserImage.getFilename());

        when(userImageRepository.findUserImagesByUserIdAndIsPrimary(userId, true)).thenReturn(userImages);
        when(userImageRepository.saveAndFlush(any(UserImage.class))).thenReturn(primaryUserImage);
        response = imageMvc.perform(multipart("/images").file(image).cookie(cookie)
                        .param("uncheckedImageType", "USER_IMAGE")
                        .param("userId", String.valueOf(user.getId())))
                .andReturn().getResponse();
    }

    @Then("This image will be stored.")
    public void this_image_will_be_stored() throws UnsupportedEncodingException {
        String expectedResponse = String.format(imageReturnPayload, primaryUserImage.getId(), primaryUserImage.getFilename(),
                primaryUserImage.getIsPrimary(), primaryUserImage.getThumbnailFilename());

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedResponse);
    }

    @Given("There is no image for my account.")
    public void there_is_no_image_for_my_account() {
        assertThat(userImages.size()).isZero();
    }

    @Then("A thumbnail of this image will be automatically created.")
    public void a_thumbnail_of_this_image_will_be_automatically_created() {
        assertThat(primaryUserImage.getThumbnailFilename()).isNotEmpty();
    }

    @Then("The primary image is {string}.")
    public void the_primary_image_is(String fileName) {
        UserImage primaryImage = primaryUserImage.getIsPrimary() ? primaryUserImage : newPrimaryUserImage;
        assertThat(primaryImage.getIsPrimary()).isTrue();
        assertThat(primaryImage.getFilename()).isEqualTo(fileName);
    }

    @When("I set the second image be my primary image.")
    public void i_set_the_second_image_be_my_primary_image() throws Exception {
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        List<UserImage> userImages = List.of(primaryUserImage);
        when(userImageRepository.findUserImagesByUserIdAndIsPrimary(userId, true))
                .thenReturn(userImages);
        when(userImageRepository.findById(newPrimaryUserImage.getId()))
                .thenReturn(Optional.of(newPrimaryUserImage));
        response = imageMvc.perform(put(String.format("/images/%d/makePrimary", newPrimaryUserImage.getId())).cookie(cookie)
                        .param("uncheckedImageType", "USER_IMAGE")
                        .param("userId", String.valueOf(userId))
                        .param("businessId", "")
                        .param("productId", ""))
                .andReturn().getResponse();
    }
}
