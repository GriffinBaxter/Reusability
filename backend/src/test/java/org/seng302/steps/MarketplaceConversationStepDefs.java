package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.ListingResource;
import org.seng302.controller.MarketplaceConversationResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Conversation;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.User;
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
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class MarketplaceConversationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationRepository marketplaceConversationRepository;

    @Autowired
    @MockBean
    private MarketplaceConversationMessageRepository marketplaceConversationMessageRepository;

    private User instigator;
    private User receiver;
    private MarketplaceCard marketplaceCard;
    private Conversation conversation;

    private MockHttpServletResponse response;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardRepository.class);
        marketplaceConversationRepository = mock(MarketplaceConversationRepository.class);
        marketplaceConversationMessageRepository = mock(MarketplaceConversationMessageRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceConversationResource(userRepository, marketplaceCardRepository, marketplaceConversationRepository, marketplaceConversationMessageRepository)).build();
    }

    @Given("There exists a conversation with instigator {string} with id {int}, recipient {string} with id {int}, and marketplace card with title {string}.")
    public void thereExistsAConversationWithInstigatorWithIdRecipientWithIdAndMarketplaceCardWithTitle(String instigatorName, int instigatorId, String receiverName, int receiverId, String cardTitle) throws IllegalAddressArgumentException, IllegalUserArgumentException, IllegalMarketplaceCardArgumentException {
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
    }

    @When("The recipient with id {int} tries to retrieve their conversations.")
    public void theUserWithIdTriesToRetrieveTheirConversations(int id) throws Exception {
        given(userRepository.findBySessionUUID(receiver.getSessionUUID())).willReturn(Optional.ofNullable(receiver));

        when(marketplaceConversationRepository.findAllByInstigatorIdOrReceiverId_OrderByCreatedDesc(id, id)).thenReturn(List.of(conversation));

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

}
