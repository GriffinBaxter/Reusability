package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.MarketplaceConversationResource;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

public class DeleteConversationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationRepository conversationRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationMessageRepository messageRepository;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    private User user;
    private User anotherUser;

    private Conversation conversation;
    private MarketplaceCard marketplaceCard;
    private Message message1;

    private MockHttpServletResponse response;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        conversationRepository = mock(MarketplaceConversationRepository.class);
        messageRepository = mock(MarketplaceConversationMessageRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceConversationResource(userRepository,
                marketplaceCardRepository, conversationRepository, messageRepository)).build();
    }

    @Given("I have received a message from another user.")
    public void i_have_received_a_message_from_another_user() throws IllegalUserArgumentException, IllegalAddressArgumentException, IllegalMarketplaceCardArgumentException, IllegalMessageContentException {
        Address address = new Address(
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
                Role.GLOBALAPPLICATIONADMIN);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2021, 1, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.now(),
                "Royal Gala Apples For Sale",
                "Fresh, wanting $3 a kg.");
        marketplaceCard.setId(1);

        conversation = new Conversation(
                anotherUser,
                user,
                marketplaceCard);
        conversation.setId(1);

        message1 = new Message(
                conversation,
                anotherUser,
                "Can I please have 5kg?");
        message1.setId(1);
    }

    @When("I try to delete the conversation containing the message.")
    public void i_try_to_delete_the_conversation_containing_the_message() throws Exception {
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(conversationRepository.findById(conversation.getId()))
                .willReturn(Optional.ofNullable(conversation));

        response = mvc.perform(delete(String.format("/users/conversation/%d", conversation.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn()
                .getResponse();
    }

    @Then("The message is successfully deleted.")
    public void the_message_is_successfully_deleted() {
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}

