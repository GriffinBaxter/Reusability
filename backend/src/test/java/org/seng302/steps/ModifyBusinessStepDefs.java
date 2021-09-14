package org.seng302.steps;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import static org.assertj.core.api.Assertions.assertThat;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.BusinessResource;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.User;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import org.seng302.view.outgoing.AddressPayload;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModifyBusinessStepDefs {

    private MockMvc mvc;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AddressRepository addressRepository;

    private Business business;

    private User user;

    private User anotherUser;

    private Address address;

    private Address anotherAddress;

    private String payloadJson;

    private MockHttpServletResponse response;

    @Before
    public void createMockMvc() throws Exception{
        addressRepository = mock(AddressRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new BusinessResource(businessRepository, userRepository, addressRepository)).build();
    }

    @Given("I am logged in as a primary admin of a business")
    public void iAmLoggedInAsAPrimaryAdminOfABusiness() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        anotherAddress = new Address("123", "new", "new", "new", "new","123","new");

        user = new User(
                "user",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "user@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        anotherUser = new User(
                "another",
                "Doe",
                "S",
                "Generic",
                "Biography",
                "another@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(2);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.CHARITABLE_ORGANISATION,
                LocalDateTime.now(),
                user,
                "#",
                "123"
        );
    }

    @When("I try to modify all the attributes of a business")
    public void iTryToModifyAllTheAttributesOfABusiness() throws Exception{
        String session = user.getSessionUUID();
        Cookie cookie = new Cookie("JSESSIONID", session);
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.of(user));
        when(userRepository.findById(anotherUser.getId())).thenReturn(Optional.of(anotherUser));
        when(businessRepository.findBusinessById(business.getId())).thenReturn(Optional.of(business));
        AddressPayload newAddress = new AddressPayload(anotherAddress.getStreetNumber(), anotherAddress.getStreetName(), anotherAddress.getCity(), anotherAddress.getRegion(),
                anotherAddress.getCountry(), anotherAddress.getPostcode(), anotherAddress.getSuburb());
        payloadJson = "{" +
                "\"primaryAdministratorId\":" + anotherUser.getId() + "," +
                "\"name\":\"" + "new" + "\"," +
                "\"description\":\"" + "new" + "\"," +
                "\"address\":" + newAddress + "," +
                "\"businessType\":\"" + "ACCOMMODATION AND FOOD SERVICES" + "\"," +
                "\"currencySymbol\":\"" + "$" + "\"," +
                "\"currencyCode\":\"" + "NZD" + "\"" +
                "}";

        response = mvc.perform(put(String.format("/businesses/%d", business.getId())).cookie(cookie).content(payloadJson).contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
    }


    @Then("The business is modified")
    public void theBusinessIsModified() throws Exception{
        Business newBusiness = new Business(anotherUser.getId(), "new", "new", anotherAddress, BusinessType.ACCOMMODATION_AND_FOOD_SERVICES, business.getCreated(), anotherUser, "$", "NZD");
        assertThat(newBusiness).hasToString(business.toString());
    }
}
