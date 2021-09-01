package org.seng302.conversation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.MarketplaceConversationResource;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketplaceCardRepository;
import org.seng302.model.repository.MarketplaceConversationMessageRepository;
import org.seng302.model.repository.MarketplaceConversationRepository;
import org.seng302.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * MarketplaceConversationResource test class
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketplaceConversationResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @MockBean
    private MarketplaceConversationRepository marketplaceConversationRepository;

    @MockBean
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    private MockHttpServletResponse response;

    private User instigator;
    private User receiver;

    private MarketplaceCard marketplaceCard;

    private Conversation conversation;

    @BeforeEach
    public void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        instigator = new User(
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
        instigator.setId(1);
        instigator.setSessionUUID(User.generateSessionUUID());

        receiver = new User(
                "Abby",
                "Wyatt",
                "W",
                "Abby",
                "bio",
                "Abby@example.com",
                LocalDate.of(2020, Month.JANUARY, 1).minusYears(13),
                "1234567555",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        receiver.setId(2);
        receiver.setSessionUUID(User.generateSessionUUID());

        marketplaceCard = new MarketplaceCard(
                instigator.getId(),
                instigator,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        conversation = new Conversation(instigator, receiver, marketplaceCard);
        conversation.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(
                   new MarketplaceConversationResource(userRepository, marketplaceCardRepository, marketplaceConversationRepository, marketplaceConversationMessageRepository))
                   .build();
    }

    /**
     * Tests that an UNAUTHORIZED status is received when no cookie is provided with the GET request.
     *
     * @throws Exception thrown if there's an error with the mock mvc methods.
     */
    @Test
    void cannotRetrieveConversationsWithNoCookie() throws Exception {
        // When
        response = mvc.perform(get("/home/conversation")).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an UNAUTHORIZED status is received when an invalid JSESSIONID is provided with the GET request.
     *
     * @throws Exception thrown if there's an error with the mock mvc methods.
     */
    @Test
    void cannotRetrieveConversationsWithInvalidCookie() throws Exception {
        // When
        response = mvc.perform(get("/home/conversation")
                        .cookie(new Cookie("JSESSIONID", "0"))).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that an OK status and an empty list are received when trying to retrieve conversations and the user has no
     * conversations associated with them.
     *
     * @throws Exception thrown if there's an error with the mock mvc methods.
     */
    @Test
    void canRetrieveEmptyListWhenNoAssociatedConversationsExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(instigator.getSessionUUID())).willReturn(Optional.ofNullable(instigator));

        // When
        when(marketplaceConversationRepository.findAllByInstigatorIdOrReceiverId_OrderByCreatedDesc(instigator.getId(), instigator.getId())).thenReturn(List.of());
        response = mvc.perform(get("/home/conversation")
                .cookie(new Cookie("JSESSIONID", instigator.getSessionUUID()))).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[]");
    }

    /**
     * Tests that an OK status and a list of conversations are received when trying to retrieve conversations and the user has a
     * conversation associated with them.
     *
     * @throws Exception thrown if there's an error with the mock mvc methods.
     */
    @Test
    void canRetrieveListOfConversationsWhenAssociatedConversationsExist() throws Exception {
        // Given
        given(userRepository.findBySessionUUID(instigator.getSessionUUID())).willReturn(Optional.ofNullable(instigator));

        // When
        when(marketplaceConversationRepository.findAllByInstigatorIdOrReceiverId_OrderByCreatedDesc(instigator.getId(), instigator.getId())).thenReturn(List.of(conversation));
        response = mvc.perform(get("/home/conversation")
                .cookie(new Cookie("JSESSIONID", instigator.getSessionUUID()))).andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[" + conversation.toConversationPayload().toString() + "]");
    }

}
