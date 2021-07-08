package org.seng302.keyword;

import org.assertj.core.api.OptionalAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.model.Address;
import org.seng302.model.Keyword;
import org.seng302.model.User;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.KeywordRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * KeywordResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class KeywordResourceIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private KeywordRepository keywordRepository;

    private MockHttpServletResponse response;

    private static User user;

    private String jsonPOST = "{\"name\": \"%s\"}";

    private static Keyword keyword;

    @BeforeAll
    static void before() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(
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
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        keyword = new Keyword("Resource", LocalDateTime.now());
    }

    // -------- POST ENDPOINT TESTS ----------------------------

    /**
     * Tests that a valid keyword that doesn't exist can be created
     * @throws Exception thrown if there is an error creating the keyword
     */
    @Test
    void canCreateKeywordWhenValidAndDoesntExist() throws Exception {
        // Given
        String newKeyword = "Creation";

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findByName(newKeyword)).willReturn(Optional.ofNullable(null));

        String payloadJson = String.format(jsonPOST, newKeyword);
        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/keywords")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();
        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a valid keyword that already exists is found
     */
    @Test
    void returnKeywordIdWhenValidAndExists() throws Exception {
        // Given
        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findByName(keyword.getName())).willReturn(Optional.ofNullable(keyword));

        String payloadJson = String.format(jsonPOST, keyword.getName());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/keywords")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * Tests that a keyword is not created when the keyword name is invalid
     */
    @Test
    void cantCreateKeywordWhenNameInvalid() throws Exception {
        // Given
        String newKeyword = "X";

        given(userRepository.findById(1)).willReturn(Optional.ofNullable(user));
        given(keywordRepository.findByName(newKeyword)).willReturn(Optional.ofNullable(null));

        String payloadJson = String.format(jsonPOST, newKeyword.repeat(25));

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(post("/keywords")
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson)
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
