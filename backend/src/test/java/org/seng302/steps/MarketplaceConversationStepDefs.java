package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.en.And;
import org.seng302.controller.MarketplaceConversationResource;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalMessageContentException;
import org.seng302.exceptions.IllegalUserArgumentException;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Marketplace conversation step definitions class
 */

public class MarketplaceConversationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MockMvc userMVC;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;


    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationRepository marketplaceConversationRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    private Address address1;
    private Address address2;
    private User instigator;
    private User receiver;
    private MarketplaceCard marketplaceCard;
    private Conversation conversation;
    private Message message1;
    private Message message2;
    private String payloadJson;

    private Message message;
    private Message message3;
    private ArrayList<Message> messageArrayList = new ArrayList<>();

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
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceConversationResource(userRepository, marketplaceCardRepository, marketplaceConversationRepository, marketplaceConversationMessageRepository)).build();
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
        instigator = new User("Bob",
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
        instigator.setId(1);

        given(userRepository.findByEmail(instigator.getEmail())).willReturn(Optional.of(instigator));

        response = userMVC.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format(loginPayloadJson, instigator.getEmail(), "Password123!")))
                .andReturn().getResponse();

        assertThat(response.getContentAsString()).isEqualTo(String.format(expectedUserIdJson, instigator.getId()));
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


    @Given("There exists a conversation with instigator {string} with id {int}, recipient {string} with id {int}, and marketplace card with title {string}.")
    public void thereExistsAConversationWithInstigatorWithIdRecipientWithIdAndMarketplaceCardWithTitle(String instigatorName, int instigatorId, String receiverName, int receiverId, String cardTitle) throws IllegalAddressArgumentException, IllegalUserArgumentException, IllegalMarketplaceCardArgumentException, IllegalMessageContentException {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        String[] instigatorNames = instigatorName.split(" ");
        String[] receiverNames = receiverName.split(" ");

        instigator = new User(
                instigatorNames[0],
                instigatorNames[1],
                "",
                "",
                "Biography",
                "instigator@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        instigator.setId(instigatorId);
        instigator.setSessionUUID(User.generateSessionUUID());

        receiver = new User(
                receiverNames[0],
                receiverNames[1],
                "",
                "",
                "bio",
                "receiver@example.com",
                LocalDate.of(2020, Month.JANUARY, 1).minusYears(13),
                "1234567555",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        receiver.setId(receiverId);
        receiver.setSessionUUID(User.generateSessionUUID());

        marketplaceCard = new MarketplaceCard(
                instigator.getId(),
                instigator,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                cardTitle,
                "Come join Hayley and help her celebrate her birthday!"
        );
        marketplaceCard.setId(1);

        conversation = new Conversation(instigator, receiver, marketplaceCard);
        message = new Message(conversation, instigator, "Initial Message");
        message2 = new Message(conversation, receiver, "First reply");
        conversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));
    }

    @When("The recipient with id {int} tries to retrieve their conversations.")
    public void theUserWithIdTriesToRetrieveTheirConversations(int id) throws Exception {
        given(userRepository.findBySessionUUID(receiver.getSessionUUID())).willReturn(Optional.ofNullable(receiver));

        when(marketplaceConversationRepository.findAllByInstigatorIdAndDeletedByInstigatorOrReceiverIdAndDeletedByReceiver_OrderByCreatedDesc(id, false, id, false)).thenReturn(List.of(conversation));

        response = mvc.perform(get("/home/conversation")
                .cookie(new Cookie("JSESSIONID", receiver.getSessionUUID()))).andReturn().getResponse();
    }

    @Then("I receive a 200 response and a conversation with instigator {string}, recipient {string}, and marketplace card {string} is returned in a list.")
    public void iReceiveA200ResponseAndAConversationWithInstigatorRecipientAndMarketplaceCardIsReturnedInAList(String instigatorName, String receiverName, String cardTitle) throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo("[" + conversation.toConversationPayload().toString() + "]");

        assertThat(conversation.toConversationPayload().getInstigatorName()).isEqualTo(instigatorName);
        assertThat(conversation.toConversationPayload().getReceiverName()).isEqualTo(receiverName);
        assertThat(conversation.toConversationPayload().getMarketplaceCardTitle()).isEqualTo(cardTitle);
    }

    @When("The recipient with id {int} tries to retrieve the messages in their conversation.")
    public void the_recipient_with_id_tries_to_retrieve_the_messages_in_their_conversation(Integer recipientId) throws Exception {
        given(userRepository.findBySessionUUID(receiver.getSessionUUID())).willReturn(Optional.ofNullable(receiver));

        when(marketplaceConversationRepository.findConversationById(conversation.getId())).thenReturn(Optional.of(conversation));
        messageArrayList.add(message2);
        messageArrayList.add(message);
        when(marketplaceConversationMessageRepository.findAllByConversationId_OrderByCreatedDesc(conversation.getId())).thenReturn(messageArrayList);

        response = mvc.perform(get("/home/conversation/" + conversation.getId() + "/messages")
                .cookie(new Cookie("JSESSIONID", receiver.getSessionUUID()))).andReturn().getResponse();
    }

    @When("The recipient replies to the conversation with the message {string}")
    public void the_recipient_replies_to_the_conversation_with_the_message(String message) throws IllegalMessageContentException {
        message3 = new Message(conversation, receiver, message);
        messageArrayList.add(message3);
    }

    @Then("A 200 response is received containing an ordered list of full messages.")
    public void a_response_is_received_containing_an_ordered_list_of_full_messages() throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        StringBuilder responseInnerJSON = new StringBuilder();
        for (Message message : messageArrayList) {
            responseInnerJSON.append(message.toMessagePayload().toString()).append(",");
        }
        // Remove trailing comma
        responseInnerJSON = new StringBuilder(responseInnerJSON.substring(0, responseInnerJSON.length() - 1));
        assertThat(response.getContentAsString()).isEqualTo("[" + responseInnerJSON + "]");
    }

    @Then("A 200 response is received containing the message {string}")
    public void a_response_is_received_containing_the_message(String messageString) throws UnsupportedEncodingException {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        StringBuilder responseInnerJSON = new StringBuilder();
        for (Message message : messageArrayList) {
            responseInnerJSON.append(message.toMessagePayload().toString()).append(",");
        }
        // Remove trailing comma
        responseInnerJSON = new StringBuilder(responseInnerJSON.substring(0, responseInnerJSON.length() - 1));
        assertThat(response.getContentAsString()).isEqualTo("[" + responseInnerJSON + "]");
        assertThat(response.getContentAsString()).contains(messageString);
    }

    @Given("I have not contacted this user about this card with id {int} before")
    public void i_have_not_contacted_this_user_about_this_card_with_id_before(Integer marketplaceCardId) {
        given(marketplaceConversationRepository.findById(marketplaceCardId)).willReturn(Optional.empty());
        given(marketplaceConversationRepository.findById(marketplaceCardId)).willReturn(Optional.empty());
    }

    @When("I send a message with the content of {string} about this marketplace card with id {int}")
    public void I_send_a_message_with_the_content_of_about_this_marketplace_card_with_id(String content,
                                                                                         Integer marketplaceCardId) throws Exception {
        payloadJson = String.format(messagePayloadJson, instigator.getId(), receiver.getId(),
                marketplaceCardId, content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));

        conversation = new Conversation(
                instigator,
                receiver,
                marketplaceCard
        );
        conversation.setCreated(LocalDateTime.of(2021, 6, 1, 0, 0));

        message1 = new Message(
                conversation,
                instigator,
                content
        );

        when(userRepository.findBySessionUUID(instigator.getSessionUUID())).thenReturn(Optional.ofNullable(instigator));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.ofNullable(receiver));
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));

        when(marketplaceConversationRepository.save(any(Conversation.class))).thenReturn(conversation);
        when(marketplaceConversationMessageRepository.save(any(Message.class))).thenReturn(message1);

        response = mvc.perform(post("/home/conversation")
                        .cookie(new Cookie("JSESSIONID", instigator.getSessionUUID()))
                        .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();
    }

    @Then("A conversation which contains this message is created")
    public void A_conversation_which_contains_this_message_is_created() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Given("I have contacted this user about this card before in the conversation with id {int}")
    public void i_have_contacted_this_user_about_this_card_before_in_the_conversation_with_id(Integer marketplaceCardId) {
        given(marketplaceConversationRepository.findById(marketplaceCardId)).willReturn(Optional.empty());
    }

    @When("I send a message with the content of {string} about this marketplace card with id {int} in the existing conversation with id {int}")
    public void I_send_a_message_with_the_content_of_about_this_marketplace_card_with_id_in_the_existing_conversation(String content,
                                                                                                                      Integer marketplaceCardId,
                                                                                                                      Integer conversationId) throws Exception {
        payloadJson = String.format(messagePayloadJson, instigator.getId(), receiver.getId(),
                marketplaceCardId, content, LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)));

        when(userRepository.findBySessionUUID(instigator.getSessionUUID())).thenReturn(Optional.ofNullable(instigator));
        when(userRepository.findById(receiver.getId())).thenReturn(Optional.ofNullable(receiver));
        when(marketplaceCardRepository.findById(marketplaceCard.getId())).thenReturn(Optional.ofNullable(marketplaceCard));

        when(marketplaceConversationRepository.save(any(Conversation.class))).thenReturn(conversation);
        when(marketplaceConversationMessageRepository.save(any(Message.class))).thenReturn(message2);

        response = mvc.perform(post(String.format("/home/conversation"))
                        .cookie(new Cookie("JSESSIONID", instigator.getSessionUUID()))
                        .contentType(MediaType.APPLICATION_JSON).content(payloadJson))
                .andReturn().getResponse();
    }

    @And("I expect the message to be created in this conversation")
    public void I_expect_the_message_to_be_created_in_this_conversation() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }


}
