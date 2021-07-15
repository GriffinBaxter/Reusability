package org.seng302.image;

import io.cucumber.java.bs.A;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.seng302.Main;
import org.seng302.controller.ImageResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.BusinessRepository;
import org.seng302.model.repository.ImageRepository;
import org.seng302.model.repository.ProductRepository;
import org.seng302.model.repository.UserRepository;
import org.seng302.services.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class ImageResourceIntergrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ImageRepository imageRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ImageResource imageResource;

    private Address address;

    private User user;

    private User anotherUser;

    private Business business;

    private Product product;

    private MultipartFile uploadedImage;

    private Image image;

    private String productId;

    private Integer businessId;

    private String sessopmToken;

    private String expectedJson;


    @BeforeAll
    public void setup() throws Exception{
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
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
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user
        );
        business.setId(2);
        anotherUser = new User(
                "John",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(3);
        anotherUser.setSessionUUID(User.generateSessionUUID());
        this.mvc = MockMvcBuilders.standaloneSetup(imageResource).build();
    }

    public void testingFileCreation() {
        // Given
        uploadedImage = new MockMultipartFile("data", "image.txt", "text/plain", "some plain text".getBytes(StandardCharsets.UTF_8));
        Cookie cookie = new Cookie("JSESSIONID", sessopmToken);
        // When

        // Then
    }

}
