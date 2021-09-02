package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.MarketplaceConversationResource;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Marketplace conversation step definitions class
 */
public class MarketplaceConversationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc conversationMVC;

    @Autowired
    private MockMvc userMVC;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationRepository marketplaceConversationRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    private User sender;
    private User receiver;
    private Address address1;
    private Address address2;
    private MarketplaceCard marketplaceCard;
    private Conversation conversation;
    private Message message1;
    private Message message2;
    private String payloadJson;

    private MockHttpServletResponse response;

    private final String loginPayloadJson = "{\"email\": \"%s\", " +
            "\"password\": \"%s\"}";
    private final String expectedUserIdJson = "{\"userId\":%s}";

    private final String messagePayloadJson = "{\"senderId\":%d," +
            "\"receiverId\":%d," +
            "\"marketplaceCardId\":%d," +
            "\"content\":\"%s\"," +
            "\"created\":\"%s\"}";

    private final String expectedConversationIdJson = "{\"conversationId\":%s}";

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        marketplaceConversationRepository = mock(MarketplaceConversationRepository.class);
        marketplaceConversationMessageRepository = mock(MarketplaceConversationMessageRepository.class);

        this.conversationMVC = MockMvcBuilders.standaloneSetup(new MarketplaceConversationResource(
                userRepository, marketplaceCardRepository, marketplaceConversationRepository, marketplaceConversationMessageRepository))
                .build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();
    }

    @Given("I am logged in as a user")
    public void i_am_logged_in_as_a_user() throws Exception {
        address1 = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        address2 = new Address(
                "2",
                "Cottage Road",
                "Itburg",
                "Smothing",
                "Romania",
                "4198",
                "Gragol"
        );
        sender = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        sender.setId(1);

        given(userRepository.findByEmail(sender.getEmail())).willReturn(Optional.of(sender));

        response = userMVC.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format(loginPayloadJson, sender.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, sender.getId()));
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @And("The marketplace card with id {int} exists who belongs to the user with first name {string} and last name {string}")
    public void the_marketplace_card_with_id_exists_who_belongs_to_the_user_with_first_name_and_last_name(Integer marketplaceCardId,
                                                                                                          String firstname, String lastname) throws IllegalMarketplaceCardArgumentException, IllegalUserArgumentException {
        receiver = new User(firstname,
                lastname,
                "Rose",
                "Bobby",
                "cool person",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address1,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        receiver.setId(2);

        marketplaceCard = new MarketplaceCard(
                receiver.getId(),
                receiver,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Baked Goods",
                "Cake, muffins, biscuits"
        );
        marketplaceCard.setId(marketplaceCardId);

        given(marketplaceCardRepository.findById(marketplaceCard.getId())).willReturn(Optional.ofNullable(marketplaceCard));

        assertThat(marketplaceCard.getId()).isEqualTo(marketplaceCardId);
        assertThat(marketplaceCard.getCreatorId()).isEqualTo(2);

    }

    @Given("I have not contacted this user about this card with id {int} before")
    public void i_have_not_contacted_this_user_about_this_card_with_id_before(Integer marketplaceCardId) {
        given(marketplaceConversationRepository.findConversationByMarketplaceCardId(marketplaceCardId)).willReturn(Optional.empty());
    }

    @When("I send a message with the content of {string} about this marketplace card with id {int}")
    public void I_send_a_message_with_the_content_of_about_this_marketplace_card_with_id(String content,
                                                                                         Integer marketplaceCardId) throws Exception {
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCardId, content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));

        conversation = new Conversation(
                sender,
                receiver,
                marketplaceCard
        );

        message1 = new Message(
                conversation,
                sender,
                content
        );

        when(userRepository.findBySessionUUID(sender.getSessionUUID())).thenReturn(Optional.ofNullable(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.ofNullable(receiver));
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));

        when(marketplaceConversationRepository.save(any(Conversation.class))).thenReturn(conversation);
        when(marketplaceConversationMessageRepository.save(any(Message.class))).thenReturn(message1);

        response = conversationMVC.perform(post("/home/conversation")
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();
    }

    @Then("A conversation which contains this message is created")
    public void A_conversation_which_contains_this_message_is_created() {
        System.out.println(response.getErrorMessage());
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Given("I have contacted this user about this card before in the conversation with id {int}")
    public void i_have_contacted_this_user_about_this_card_before_in_the_conversation_with_id(Integer marketplaceCardId) {
        given(marketplaceConversationRepository.findConversationByMarketplaceCardId(marketplaceCardId)).willReturn(Optional.empty());
    }

    @When("I send a message with the content of {string} about this marketplace card with id {int} in the existing conversation with id {int}")
    public void I_send_a_message_with_the_content_of_about_this_marketplace_card_with_id_in_the_existing_conversation(String content,
                                                                                                                      Integer marketplaceCardId,
                                                                                                                      Integer conversationId) throws Exception {
        payloadJson = String.format(messagePayloadJson, sender.getId(), receiver.getId(),
                marketplaceCardId, content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));

        when(userRepository.findBySessionUUID(sender.getSessionUUID())).thenReturn(Optional.ofNullable(sender));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.ofNullable(receiver));
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));

        when(marketplaceConversationRepository.save(any(Conversation.class))).thenReturn(conversation);
        when(marketplaceConversationMessageRepository.save(any(Message.class))).thenReturn(message2);

        response = conversationMVC.perform(post(String.format("/home/conversation", conversationId))
                .cookie(new Cookie("JSESSIONID", sender.getSessionUUID()))
                .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();
    }

    @And("I expect the message to be created in this conversation")
    public void I_expect_the_message_to_be_created_in_this_conversation() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

}
