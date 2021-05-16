package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import org.seng302.address.Address;
import org.seng302.address.AddressRepository;
import org.seng302.user.User;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.mock;

public class CardCreationStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private MarketplaceCardRepository marketplaceCardRepository;

    private MockHttpServletResponse response;
    private User user;
    private Address address;
    private MarketplaceCard card;

    @Before
    public void createMockMvc() {
        userRepository = mock(UserRepository.class);
        addressRepository = mock(AddressRepository.class);
        marketplaceCardRepository = mock(MarketplaceCardReposistory.class);

        this.mvc = MockMvcBuilders.standaloneSetup(new MarketplaceCardResource(
                marketplaceCardRepository, userRepository, addressRepository))
                .build();
    }

    @Given("I am logged in.")
    public void i_am_logged_in() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }


}
