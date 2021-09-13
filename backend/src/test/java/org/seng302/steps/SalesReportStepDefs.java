package org.seng302.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.seng302.controller.ListingResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.seng302.view.outgoing.SalesReportPayload;
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
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class SalesReportStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    @MockBean
    private UserRepository userRepository;

    @Autowired
    @MockBean
    private BusinessRepository businessRepository;

    @Autowired
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    @MockBean
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    @MockBean
    private ListingRepository listingRepository;

    @Autowired
    @MockBean
    private SoldListingRepository soldListingRepository;

    @Autowired
    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @Autowired
    @MockBean
    private SoldListingNotificationRepository soldListingNotificationRepository;

    @Autowired
    @MockBean
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    private SoldListing soldListing;

    private MockHttpServletResponse response;

    private final ObjectMapper mapper = new ObjectMapper();

    private String yearFromDate;
    private String yearToDate;
    private String monthFromDate;
    private String monthToDate;
    private String dayFromDate;
    private String dayToDate;
    private String customFromDate;
    private String customToDate;
    private String granularity;

    @Before
    public void createMockMvc() {
        listingRepository = mock(ListingRepository.class);
        inventoryItemRepository = mock(InventoryItemRepository.class);
        productRepository = mock(ProductRepository.class);
        businessRepository = mock(BusinessRepository.class);
        userRepository = mock(UserRepository.class);
        soldListingRepository = mock(SoldListingRepository.class);
        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository,
                soldListingRepository, listingNotificationRepository, soldListingNotificationRepository,
                bookmarkedListingMessageRepository
        )).build();
    }

    @Given("I am logged in as an administrator of an existing business.")
    public void iAmLoggedInAsAnAdministratorOfAnExistingBusiness() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );

        user = new User(
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
        user.setId(1);
        user.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user,
                "$",
                "NZD"
        );
        business.setId(1);
        
        product = new Product(
                "WATT-420-BEANS",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.99,
                "9400547002634"
        );
        
        inventoryItem = new InventoryItem(
                product,
                "WATT-420-BEANS",
                100,
                20.99,
                2099.00,
                LocalDate.of(2021, 1, 1),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2023, 1, 1),
                LocalDate.of(2024, 1, 1)
        );
        
        listing = new Listing(
                inventoryItem,
                10,
                55.55,
                "info",
                LocalDateTime.now(),
                null
        );
        
        soldListing = new SoldListing(
                business,
                user,
                LocalDateTime.of(2021, Month.JUNE, 11, 0, 0),
                listing.getInventoryItem().getProduct().getProductId(),
                listing.getQuantity(),
                listing.getPrice(),
                0
        );
        soldListing.setId(1);
        soldListing.setSaleDate(LocalDateTime.of(2021, Month.JULY, 20, 0, 0));
        
        user.setBusinessesAdministeredObjects(List.of(business));
        
        given(userRepository.findBySessionUUID(user.getSessionUUID())).willReturn(Optional.ofNullable(user));
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
    }

    @When("I select periods of sales reports for a single year, month, week and day.")
    public void iSelectPeriodsOfSalesReportsForASingleYearMonthWeekAndDay() {
        yearFromDate = "2021-01-01T00:00";
        yearToDate = "2021-12-31T00:00";

        monthFromDate = "2021-07-01T00:00";
        monthToDate = "2021-07-31T00:00";

        dayFromDate = "2021-07-20T00:00";
        dayToDate = "2021-07-20T00:00";
    }

    @Then("Sales reports are returned for the aforementioned periods.")
    public void salesReportsAreReturnedForTheAforementionedPeriods() throws Exception {
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", yearFromDate)
                        .param("toDate", yearToDate)
                        .param("granularity", "Total"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", monthFromDate)
                        .param("toDate", monthToDate)
                        .param("granularity", "Total"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());

        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", dayFromDate)
                        .param("toDate", dayToDate)
                        .param("granularity", "Total"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @When("I select a custom period for the sales report to start at {string} and end at {string}.")
    public void iSelectACustomPeriodForTheSalesReportToStartAtAndEndAt(String start, String end) {
        customFromDate = start;
        customToDate = end;
    }


    @Then("A sales report is returned for the custom period.")
    public void aSalesReportIsReturnedForTheCustomPeriod() throws Exception {
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", customFromDate)
                        .param("toDate", customToDate)
                        .param("granularity", "Total"))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @When("I select a granularity for the sales report.")
    public void iSelectAGranularityForTheSalesReport() {
        granularity = "Yearly";
    }

    @Then("A sales report is returned with the granularity.")
    public void aSalesReportIsReturnedWithTheGranularity() throws Exception {
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-04-20T00:00")
                        .param("toDate", "2021-09-30T00:00")
                        .param("granularity", granularity))
                .andReturn().getResponse();
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        String granularityName = "2021";

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isEqualTo(granularityName);
    }

    @When("I select a granularity of {string} for the sales report.")
    public void iSelectAGranularityOfForTheSalesReport(String granularityInput) {
        granularity = granularityInput;
    }

    @Then("A sales report is returned with the {string} granularity.")
    public void aSalesReportIsReturnedWithTheGranularity(String granularityInput) throws Exception {
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-04-20T00:00")
                        .param("toDate", "2021-09-30T00:00")
                        .param("granularity", granularityInput))
                .andReturn().getResponse();
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        List<String> granularityNames = List.of(
                "April 2021", "May 2021", "June 2021", "July 2021", "August 2021", "September 2021"
        );

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        for (int i = 0; i < responseList.size(); i++) {
            assertThat(responseList.get(i).getGranularityName()).isEqualTo(granularityNames.get(i));
        }
    }
}
