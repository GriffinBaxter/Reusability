package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.utils.SearchUtils;
import org.seng302.controller.BusinessResource;
import org.seng302.controller.UserResource;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalBusinessArgumentException;
import org.seng302.exceptions.IllegalUserArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.User;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class BusinessSearchStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc businessMVC;

    @Autowired
    private MockMvc userMVC;

    @Autowired
    @MockBean
    private BusinessRepository businessRepository;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private AddressRepository addressRepository;

    @Autowired
    @MockBean
    private ConversationRepository conversationRepository;

    @Autowired
    @MockBean
    private MessageRepository messageRepository;

    private Address address1;
    private User user1;

    private Address address2;
    private User user2;

    private MockHttpServletResponse response;

    private Business business;

    private Integer pageNo;
    private Integer pageSize;
    private Sort sortBy;
    private Pageable paging;

    private List<Business> businesses;
    private Business business1;
    private Business business2;

    private final String expectedBusinessJson = "{\"id\":%d," +
            "\"administrators\":%s," +
            "\"primaryAdministratorId\":%d," +
            "\"name\":\"%s\"," +
            "\"description\":\"%s\"," +
            "\"address\":%s," +
            "\"businessType\":\"%s\"," +
            "\"created\":\"%s\""+
            "}";

    private final String expectedAdministratorJson = "[{\"id\":%d," +
            "\"firstName\":\"%s\"," +
            "\"lastName\":\"%s\"," +
            "\"middleName\":\"%s\"," +
            "\"nickname\":\"%s\"," +
            "\"bio\":\"%s\"," +
            "\"email\":\"%s\"," +
            "\"created\":\"%s\"," +
            "\"role\":\"%s\"," +
            "\"businessesAdministered\":[null]," +
            "\"dateOfBirth\":\"%s\"," +
            "\"phoneNumber\":\"%s\"," +
            "\"homeAddress\":%s}]";

    private String expectedUserJson;
    private String expectedJson;

    @Before
    public void setup() throws IllegalAddressArgumentException, IllegalUserArgumentException {
        address1 = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user1 = new User("Bob",
                "Smith",
                "Ben",
                "Bobby",
                "cool person",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address1,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user1.setId(1);
        user1.setSessionUUID(User.generateSessionUUID());

        address2 = new Address(
                "10",
                "Riccarton Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "8041",
                "Riccarton"
        );
        user2 = new User("Fred",
                "Wellington",
                "William",
                "Freddy",
                "cool person",
                "email2@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address2,
                "Qwerty123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
        user2.setId(2);
        user2.setSessionUUID(User.generateSessionUUID());

        // Paging Stuff
        pageNo = 0;
        pageSize = 5;
        sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        paging = PageRequest.of(pageNo, pageSize, sortBy);

        userRepository = mock(UserRepository.class);
        businessRepository = mock(BusinessRepository.class);

        this.businessMVC = MockMvcBuilders.standaloneSetup(new BusinessResource(
                businessRepository, userRepository, addressRepository)).build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(
                userRepository, addressRepository, conversationRepository, messageRepository)).build();
    }

    /* ------------------------------------------AC2------------------------------------------ */

    @Given("there exists a business with name {string}")
    public void there_exists_a_business_with_name(String name) throws IllegalBusinessArgumentException {
        business = new Business(
                user1.getId(),
                name,
                "description",
                address1,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user1
        );

        List<String> names = new ArrayList<>();
        names.add(name);

        businesses = new ArrayList<>();
        businesses.add(business);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNames(names, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNames(names, paging)).isNotEmpty();
    }

    @Given("there exists businesses with names {string} and {string}")
    public void there_exists_businesses_with_names_and(String name1, String name2) throws IllegalBusinessArgumentException {
        business1 = new Business(
                user1.getId(),
                name1,
                "description",
                address1,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user1
        );

        business2 = new Business(
                user2.getId(),
                name2,
                "description",
                address2,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user2
        );

        List<String> names = new ArrayList<>();
        names.add(name1);
        names.add(name2);

        businesses = new ArrayList<>();
        businesses.add(business1);
        businesses.add(business2);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNames(names, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNames(names, paging)).isNotEmpty();
    }

    @When("I enter the full name {string} and search for businesses")
    public void i_enter_the_full_name_and_search_for_businesses(String name) throws Exception {
        List<String> names = new ArrayList<>();
        names.add(name);

        when(businessRepository.findAllBusinessesByNames(names, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("searchQuery", name)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I enter the partial name {string} and search for businesses")
    public void i_enter_the_partial_name_and_search_for_businesses(String name) throws Exception {
        List<String> names = new ArrayList<>();
        names.add(name);

        when(businessRepository.findAllBusinessesByNames(names, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("searchQuery", name)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I search for businesses using a search query containing {string}")
    public void i_search_for_businesses_using_a_search_query_containing(String searchQuery) throws Exception {
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);

        when(businessRepository.findAllBusinessesByNames(names, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("searchQuery", searchQuery)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("I receive the business with name {string}")
    public void i_receive_the_business_with_name(String name) throws UnsupportedEncodingException {
        expectedUserJson = String.format(expectedAdministratorJson, user1.getId(), user1.getFirstName(), user1.getLastName(),
                user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(), user1.getCreated(), user1.getRole(),
                user1.getDateOfBirth(), user1.getPhoneNumber(), address1);
        expectedJson = "[" + String.format(expectedBusinessJson, business.getId(), expectedUserJson, business.getPrimaryAdministratorId(),
                name, business.getDescription(), business.getAddress(), business.getBusinessType(), business.getCreated()) + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Then("I receive the businesses with names {string} and {string}")
    public void i_receive_the_businesses_with_names_and(String name1, String name2) throws UnsupportedEncodingException {
        String expectedUserJson1 = String.format(expectedAdministratorJson, user1.getId(), user1.getFirstName(), user1.getLastName(),
                user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(), user1.getCreated(), user1.getRole(),
                user1.getDateOfBirth(), user1.getPhoneNumber(), address1);
        String expectedUserJson2 = String.format(expectedAdministratorJson, user2.getId(), user2.getFirstName(), user2.getLastName(),
                user2.getMiddleName(), user2.getNickname(), user2.getBio(), user2.getEmail(), user2.getCreated(), user2.getRole(),
                user2.getDateOfBirth(), user2.getPhoneNumber(), address2);

        String expectedBusinessJson1 = String.format(expectedBusinessJson, business1.getId(), expectedUserJson1, business1.getPrimaryAdministratorId(),
                name1, business1.getDescription(), business1.getAddress(), business1.getBusinessType(), business1.getCreated());
        String expectedBusinessJson2 = String.format(expectedBusinessJson, business2.getId(), expectedUserJson2, business2.getPrimaryAdministratorId(),
                name2, business2.getDescription(), business2.getAddress(), business2.getBusinessType(), business2.getCreated());

        expectedJson = "[" + expectedBusinessJson1 + "," + expectedBusinessJson2 + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    /* ------------------------------------------AC4------------------------------------------ */

    @Given("there exists a business with business type {string}")
    public void there_exists_a_business_with_business_type(String type) throws IllegalBusinessArgumentException {
        BusinessType businessType = BusinessType.valueOf(type);

        business = new Business(
                user1.getId(),
                "Test Business",
                "description",
                address1,
                businessType,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user1
        );

        businesses = new ArrayList<>();
        businesses.add(business);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findBusinessesByBusinessType(businessType, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findBusinessesByBusinessType(businessType, paging)).isNotEmpty();
    }

    @Given("there exists a business with name {string} and business type {string}")
    public void there_exists_a_business_with_name_and_business_type(String name, String type) throws IllegalBusinessArgumentException {
        BusinessType businessType = BusinessType.valueOf(type);

        business = new Business(
                user1.getId(),
                name,
                "description",
                address1,
                businessType,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user1
        );

        businesses = new ArrayList<>();
        businesses.add(business);

        List<String> names = new ArrayList<>();
        names.add(name);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNamesAndType(names, businessType, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNamesAndType(names, businessType, paging)).isNotEmpty();
    }

    @When("I search for businesses with business type {string}")
    public void i_search_for_businesses_with_business_type(String type) throws Exception {
        BusinessType businessType = BusinessType.valueOf(type);

        when(businessRepository.findBusinessesByBusinessType(businessType, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("businessType", type)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID())))
                .andReturn().getResponse();
    }

    @When("I search for businesses with name {string} and business type {string}")
    public void i_search_for_businesses_with_name_and_business_type(String name, String type) throws Exception {
        List<String> names = new ArrayList<>();
        names.add(name);

        BusinessType businessType = BusinessType.valueOf(type);

        when(businessRepository.findAllBusinessesByNamesAndType(names, businessType, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("searchQuery", name)
                .param("businessType", type)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID())))
                .andReturn().getResponse();
    }

    @Then("I receive the business with business type {string}")
    public void i_receive_the_business_with_business_type(String type) throws UnsupportedEncodingException {
        expectedUserJson = String.format(expectedAdministratorJson, user1.getId(), user1.getFirstName(), user1.getLastName(),
                user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(), user1.getCreated(), user1.getRole(),
                user1.getDateOfBirth(), user1.getPhoneNumber(), address1);
        expectedJson = "[" + String.format(expectedBusinessJson, business.getId(), expectedUserJson, business.getPrimaryAdministratorId(),
                business.getName(), business.getDescription(), business.getAddress(), BusinessType.valueOf(type), business.getCreated()) + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

    @Then("I receive the business with name {string} and business type {string}")
    public void i_receive_the_business_with_name_and_business_type(String name, String type) throws UnsupportedEncodingException {
        expectedUserJson = String.format(expectedAdministratorJson, user1.getId(), user1.getFirstName(), user1.getLastName(),
                user1.getMiddleName(), user1.getNickname(), user1.getBio(), user1.getEmail(), user1.getCreated(), user1.getRole(),
                user1.getDateOfBirth(), user1.getPhoneNumber(), address1);
        expectedJson = "[" + String.format(expectedBusinessJson, business.getId(), expectedUserJson, business.getPrimaryAdministratorId(),
                name, business.getDescription(), business.getAddress(), BusinessType.valueOf(type), business.getCreated()) + "]";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJson);
    }

}
