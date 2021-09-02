package org.seng302.marketplaceConversation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.MarketplaceConversationResource;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

    private String payloadJson;

    private User sender;
    private User receiver;
    private User gaa;
    private User dgaa;
    private MarketplaceCard marketplaceCard;
    private Conversation conversation;
    private Message message;
    private String content;

    private final String messagePayloadJson = "{\"senderId\":%d," +
            "\"receiverId\":%d," +
            "\"marketplaceCardId\":%d," +
            "\"content\":\"%s\"," +
            "\"created\":\"%s\"}";

    /**
     * Before each create a user that will be used in all tests when creating cards.
     *
     * @throws Exception thrown if there is an error when creating an address or user.
     */
    @BeforeEach
    void setup() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        sender = new User(
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
        sender.setId(1);
        sender.setSessionUUID(User.generateSessionUUID());

        receiver = new User("Another",
                "User",
                "Middle",
                "AU",
                "bio",
                "anotheruser@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        receiver.setId(2);
        receiver.setSessionUUID(User.generateSessionUUID());

        gaa = new User("Global",
                "Admin",
                "Application",
                "GAA",
                "bio",
                "gaa@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gaa.setId(3);
        gaa.setSessionUUID(User.generateSessionUUID());

        dgaa = new User("Default",
                "Admin",
                "Application",
                "DGAA",
                "bio",
                "dgaa@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dgaa.setId(4);
        dgaa.setSessionUUID(User.generateSessionUUID());

        marketplaceCard = new MarketplaceCard(
                receiver.getId(),
                receiver,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        conversation = new Conversation(
                        sender,
                        receiver,
                        marketplaceCard
        );
        conversation.setId(1);

        content = "Hi Hayley, I want to buy some baked goods :)";

        message = new Message(
                conversation,
                sender,
                content
        );
        message.setId(1);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceConversationResource(
                userRepository, marketplaceCardRepository, marketplaceConversationRepository, marketplaceConversationMessageRepository))
                .build();
    }

    // --------------------------------- Tests for POST /home/conversation/{conversationId} ----------------------------

    /**
     * Tests that a UNAUTHORIZED (401) status is received when sending a valid marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint when the user is not logged in.
     * Conversation already exists and message does not exist.
     *
     * @throws Exception thrown if there is an error when checking that the users is logged in.
     */
    @Test
    void givenNoCookie_WhenCreateMessage_ThenReceiveUnauthorizedStatus() throws Exception {
        // given
        given(userRepository.findById(sender.getId())).willReturn(Optional.ofNullable(sender));
        String nonExistingSessionUUID = User.generateSessionUUID();
        given(userRepository.findBySessionUUID(nonExistingSessionUUID)).willReturn(Optional.empty());
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));

        // when
        response = mvc.perform(post(String.format("/home/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", nonExistingSessionUUID))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests that a BAD_REQUEST (400) status is received when sending a marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint and the receiver id does not correspond
     * to an existing user.
     *
     * @throws Exception thrown if there is an error when checking that the receiving user exists.
     */
    @Test
    void givenValidCookieAndNonExistentReceiverId_WhenCreateMessage_ThenReceiveBadRequestStatus() throws Exception {
        // given
        given(userRepository.findBySessionUUID(sender.getSessionUUID())).willReturn(Optional.ofNullable(sender));
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));
        given(marketplaceConversationMessageRepository.findMessageById(message.getId())).willReturn(Optional.ofNullable(message));

        Integer nonExistentUserId = 1000;
        given(userRepository.findById(nonExistentUserId)).willReturn(Optional.empty());

        // when
        response = mvc.perform(post(String.format("/home/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid Conversation - invalid receiver id");
    }

    /**
     * Tests that a BAD_REQUEST (400) status is received when sending a marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint and the receiver id does not correspond
     * to an existing marketplace card.
     *
     * @throws Exception thrown if there is an error when checking that the card exists.
     */
    @Test
    void givenValidCookieAndValidReceiverIdAndInvalidMarketplaceCardId_WhenCreateMessage_ThenReceiveBadRequestStatus() throws Exception {
        // given
        given(userRepository.findBySessionUUID(sender.getSessionUUID())).willReturn(Optional.ofNullable(sender));
        given(userRepository.findById(sender.getId())).willReturn(Optional.ofNullable(sender));
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));
        given(marketplaceConversationMessageRepository.findMessageById(message.getId())).willReturn(Optional.ofNullable(message));
        given(userRepository.findById(receiver.getId())).willReturn(Optional.ofNullable(receiver));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(post(String.format("/home/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid Conversation - invalid card id");
    }

    /**
     * Tests that a NOT_ACCEPTABLE (406) status is received when sending a marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint and the conversation id does not correspond
     * to an existing marketplace conversation, and the receiver and card exist.
     *
     * @throws Exception thrown if there is an error when checking that the conversation exists.
     */
    @Test
    void givenValidCookieAndValidReceiverIdAndValidCardIdAndNonExistentConversationId_WhenCreateMessage_ThenReceiveBadRequestStatus() throws Exception {
        // given
        given(userRepository.findBySessionUUID(sender.getSessionUUID())).willReturn(Optional.ofNullable(sender));
        given(userRepository.findById(sender.getId())).willReturn(Optional.ofNullable(sender));
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));
        given(marketplaceConversationMessageRepository.findMessageById(message.getId())).willReturn(Optional.empty());
        given(userRepository.findById(receiver.getId())).willReturn(Optional.ofNullable(receiver));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));
        given(marketplaceConversationRepository.findConversationById(conversation.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(post(String.format("/home/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
        assertThat(response.getErrorMessage()).isEqualTo("Invalid Conversation - conversation id does not exist");
    }


    /**
     * Tests that a CREATED (201) status is received when sending a marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint and the receiver id corresponds
     * to an existing marketplace conversation, the card id corresponds to an existing marketplace card and the
     * conversation id is not provided (and thus the conversation is created).
     *
     * @throws Exception thrown if there is an error when checking that the conversation exists.
     */
    @Test
    void givenValidDataAndConversationIdIsNotProvided_WhenCreateMessage_ThenReceiveCreatedStatus() throws Exception {
        // given
        given(userRepository.findBySessionUUID(sender.getSessionUUID())).willReturn(Optional.ofNullable(sender));
        given(userRepository.findById(sender.getId())).willReturn(Optional.ofNullable(sender));
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));
        given(userRepository.findById(receiver.getId())).willReturn(Optional.ofNullable(receiver));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));
        given(marketplaceConversationRepository.findById(conversation.getId())).willReturn(Optional.empty());

        // when
        response = mvc.perform(post(String.format("/home/conversation"))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /**
     * Tests that a CREATED (201) status is received when sending a marketplace conversation message
     * to the /home/conversation/{conversationId} API endpoint and the receiver id corresponds
     * to an existing marketplace conversation, the card id corresponds to an existing marketplace card and the
     * conversation id is an existing conversation.
     *
     * @throws Exception thrown if there is an error when checking that the conversation exists.
     */
    @Test
    void givenValidDataAndConversationIdExists_WhenCreateMessage_ThenReceiveCreatedStatus() throws Exception {
        // given
        given(userRepository.findBySessionUUID(sender.getSessionUUID())).willReturn(Optional.ofNullable(sender));
        given(userRepository.findById(sender.getId())).willReturn(Optional.ofNullable(sender));
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCard.getId(), content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));
        given(userRepository.findById(receiver.getId())).willReturn(Optional.ofNullable(receiver));
        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));
        given(marketplaceConversationRepository.findConversationById(conversation.getId())).willReturn(Optional.ofNullable(conversation));

        // when
        response = mvc.perform(post(String.format("/home/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }
}
