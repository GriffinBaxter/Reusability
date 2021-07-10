package org.seng302.steps;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.Cookie;
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

    private Address address1;
    private User user1;

    private Address address2;
    private User user2;

    private MockHttpServletResponse response;

    private Business business;

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

        userRepository = mock(UserRepository.class);
        businessRepository = mock(BusinessRepository.class);

        this.businessMVC = MockMvcBuilders.standaloneSetup(new BusinessResource(
                businessRepository, userRepository, addressRepository)).build();
        this.userMVC = MockMvcBuilders.standaloneSetup(new UserResource(userRepository, addressRepository)).build();
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

        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        List<Business> businesses = new ArrayList<>();
        businesses.add(business);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNames(names, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNames(names, paging)).isNotEmpty();
    }

    @When("I enter the full name {string} and search for businesses")
    public void i_enter_the_full_name_and_search_for_businesses(String name) throws Exception {
        List<String> names = new ArrayList<>();
        names.add(name);

        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        List<Business> businesses = new ArrayList<>();
        businesses.add(business);

        when(businessRepository.findAllBusinessesByNames(names, paging)).thenReturn(new PageImpl<>(businesses, paging, businesses.size()));

        response = businessMVC.perform(get("/businesses/search")
                .param("searchQuery", name)
                .cookie(new Cookie("JSESSIONID", user1.getSessionUUID()))).andReturn().getResponse();
    }

    @Given("there exists businesses with names {string} and {string}")
    public void there_exists_businesses_with_names_and(String name1, String name2) throws IllegalBusinessArgumentException {
        Business business1 = new Business(
                user1.getId(),
                name1,
                "description",
                address1,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0, 0)),
                user1
        );

        Business business2 = new Business(
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

        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        List<Business> businesses = new ArrayList<>();
        businesses.add(business1);
        businesses.add(business2);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNames(names, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNames(names, paging)).isNotEmpty();
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

        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        List<Business> businesses = new ArrayList<>();
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

        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable paging = PageRequest.of(pageNo, pageSize, sortBy);

        List<Business> businesses = new ArrayList<>();
        businesses.add(business);

        List<String> names = new ArrayList<>();
        names.add(name);

        given(userRepository.findBySessionUUID(user1.getSessionUUID())).willReturn(Optional.ofNullable(user1));
        given(businessRepository.findAllBusinessesByNamesAndType(names, businessType, paging)).willReturn(new PageImpl<>(businesses, paging, businesses.size()));

        assertThat(businessRepository.findAllBusinessesByNamesAndType(names, businessType, paging)).isNotEmpty();
    }
}
