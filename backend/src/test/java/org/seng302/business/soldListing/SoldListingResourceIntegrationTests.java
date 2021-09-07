package org.seng302.business.soldListing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.seng302.Main;
import org.seng302.controller.ListingResource;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.*;
import org.seng302.view.outgoing.SalesReportPayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class SoldListingResourceIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BusinessRepository businessRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private InventoryItemRepository inventoryItemRepository;

    @MockBean
    private ListingRepository listingRepository;

    @MockBean
    private SoldListingRepository soldListingRepository;

    @MockBean
    private ListingNotificationRepository listingNotificationRepository;

    @MockBean
    private SoldListingNotificationRepository soldListingNotificationRepository;

    @MockBean
    private BookmarkedListingMessageRepository bookmarkedListingMessageRepository;

    private MockHttpServletResponse response;

    private final ObjectMapper mapper = new ObjectMapper();

    private User dGAA;

    private User gAA;

    private User user;

    private User anotherUser;

    private Business business;

    private Business anotherBusiness;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;
    private Listing listing2;

    private SoldListing soldListing;
    private SoldListing soldListing2;

    private String expectedJSON;

    private final String expectedSoldListingsJSON = "[" +
            "{\"id\":%s," +
            "\"saleDate\":\"%s\"," +
            "\"listingDate\":\"%s\"," +
            "\"productId\":\"%s\"," +
            "\"quantity\":%d," +
            "\"price\":%.1f," +
            "\"bookmarks\":%d," +
            "\"customer\":%s}]";


    @BeforeAll
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

        dGAA = new User(
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
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        dGAA.setId(1);
        dGAA.setSessionUUID(User.generateSessionUUID());
        gAA = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        gAA.setId(2);
        gAA.setSessionUUID(User.generateSessionUUID());
        user = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        user.setId(3);
        user.setSessionUUID(User.generateSessionUUID());
        anotherUser = new User ("first",
                "last",
                "middle",
                "nick",
                "bio",
                "example@example.com",
                LocalDate.of(2000, 1, 1),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        anotherUser.setId(4);
        anotherUser.setSessionUUID(User.generateSessionUUID());

        business = new Business(
                user.getId(),
                "name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user
        );
        business.setId(1);

        anotherBusiness = new Business(
                user.getId(),
                "anotherName",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                user
        );
        anotherBusiness.setId(2);
        user.setBusinessesAdministeredObjects(List.of(business, anotherBusiness));

        product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        inventoryItem = new InventoryItem(
                product,
                "PROD",
                20,
                10.00,
                20.00,
                LocalDate.now().minusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(1)
        );
        inventoryItem.setId(1);

        LocalDateTime dateTime = LocalDateTime.now().plusDays(1);

        listing = new Listing(
                inventoryItem,
                3,
                10.5,
                "more info",
                LocalDateTime.now().minusDays(10),
                dateTime
        );
        listing.setId(1);

        listing2 = new Listing(
                inventoryItem,
                3,
                25.75,
                "more info",
                LocalDateTime.now().minusDays(10),
                dateTime
        );
        listing2.setId(1);

        soldListing = new SoldListing(
                business,
                anotherUser,
                listing.getCreated(),
                product.getProductId(),
                listing.getQuantity(),
                listing.getPrice(),
                0);
        soldListing.setId(1);
        soldListing.setSaleDate(LocalDateTime.of(2021, Month.FEBRUARY, 25, 0, 0));

        soldListing2 = new SoldListing(
                business,
                anotherUser,
                listing.getCreated(),
                product.getProductId(),
                listing.getQuantity(),
                listing.getPrice(),
                0);
        soldListing2.setId(2);
        soldListing2.setSaleDate(LocalDateTime.of(2021, Month.MARCH, 5, 0, 0));

        this.mvc = MockMvcBuilders.standaloneSetup(new ListingResource(
                listingRepository, inventoryItemRepository, productRepository, businessRepository, userRepository, soldListingRepository, listingNotificationRepository, soldListingNotificationRepository, bookmarkedListingMessageRepository))
                .build();
    }

    /**
     * Tests a user can retrieve an Page of Sold Listings when they exist for the business and user is a business admin
     * @throws Exception Exception for mvc
     */
    @Test
    void canRetrieveSoldListingsWhenSoldListingsExistForBusiness() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // when
        List<SoldListing> list = List.of(soldListing);
        Page<SoldListing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("saleDate"));
        Pageable paging = PageRequest.of(0, 10, sort);

        when(soldListingRepository.findAllByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests a user can retrieve an empty Page of Sold Listings when they don't exist for the business and user is a business admin
     * @throws Exception Exception for mvc
     */
    @Test
    void canRetrieveSoldListingsWhenNoSoldListingsExistForBusiness() throws Exception {
        // Given
        given(businessRepository.findBusinessById(anotherBusiness.getId())).willReturn(Optional.ofNullable(anotherBusiness));

        expectedJSON = "[]";
        // when
        List<SoldListing> list = List.of();
        Page<SoldListing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("saleDate"));
        Pageable paging = PageRequest.of(0, 10, sort);

        when(soldListingRepository.findAllByBusinessId(anotherBusiness.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", anotherBusiness.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests a user cant retrieve a Page of Sold Listings when user is not a business admin
     * @throws Exception Exception for mvc
     */
    @Test
    void cantRetrieveSoldListingsWhenUserIsNotBusinessAdmin() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        // when
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID())).thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", business.getId()))
                .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests a user cant retrieve a Page of Sold Listings when business doesn't exist
     * @throws Exception Exception for mvc
     */
    @Test
    void cantRetrieveSoldListingsWhenSoldListingsExistForBusinessButUserIsNotBusinessAdmin() throws Exception {
        // Given
        given(businessRepository.findBusinessById(10)).willReturn(Optional.empty());

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", 10))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests a user cant retrieve a Page of Sold Listings when page is invalid
     * @throws Exception Exception for mvc
     */
    @Test
    void cantRetrieveSoldListingsWhenPageIsInvalid() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        // when
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", business.getId()))
                .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                .queryParam("page", "t"))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests a user can retrieve an Page of Sold Listings when they exist for the business and user is a GAA
     * @throws Exception Exception for mvc
     */
    @Test
    void canRetrieveSoldListingsWhenSoldListingsExistForBusinessAndUserIsGAA() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // when
        List<SoldListing> list = List.of(soldListing);
        Page<SoldListing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("saleDate"));
        Pageable paging = PageRequest.of(0, 10, sort);

        when(soldListingRepository.findAllByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", business.getId()))
                .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    /**
     * Tests a user can retrieve an Page of Sold Listings when they exist for the business and user is a DGAA
     * @throws Exception Exception for mvc
     */
    @Test
    void canRetrieveSoldListingsWhenSoldListingsExistForBusinessAndUserIsDGAA() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // when
        List<SoldListing> list = List.of(soldListing);
        Page<SoldListing> pagedResponse = new PageImpl<>(list);
        Sort sort = Sort.by(Sort.Order.desc("saleDate"));
        Pageable paging = PageRequest.of(0, 10, sort);

        when(soldListingRepository.findAllByBusinessId(business.getId(), paging)).thenReturn(pagedResponse);

        when(userRepository.findBySessionUUID(dGAA.getSessionUUID())).thenReturn(Optional.ofNullable(dGAA));
        response = mvc.perform(get(String.format("/businesses/%d/soldListings", business.getId()))
                .cookie(new Cookie("JSESSIONID", dGAA.getSessionUUID())))
                .andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).isEqualTo(expectedJSON);
    }

    // ---------------------------------------- SALES REPORT TESTS ----------------------------------------

    /**
     * Tests a user can retrieve a Sales Report with multiple sold listings when they are an admin of the business
     * @throws Exception MockMvc
     */
    @Test
    void canRetrieveSalesReportWithMultipleSoldListingsWhenUserIsBusinessAdmin() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing, soldListing2));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isNull();
        assertThat(responseList.get(0).getTotalSales()).isEqualTo(2);
        assertThat(responseList.get(0).getTotalRevenue()).isEqualTo(soldListing.getPrice() + soldListing2.getPrice());
    }

    /**
     * Tests a user can retrieve a Sales Report when they are a GAA
     * @throws Exception MockMvc
     */
    @Test
    void canRetrieveSalesReportWhenUserIsGAA() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(gAA.getSessionUUID())).thenReturn(Optional.ofNullable(gAA));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", gAA.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isNull();
        assertThat(responseList.get(0).getTotalSales()).isEqualTo(1);
        assertThat(responseList.get(0).getTotalRevenue()).isEqualTo(soldListing.getPrice());
    }

    /**
     * Tests a user cannot retrieve a Sales Report when they are not an admin of the business
     * @throws Exception MockMvc
     */
    @Test
    void cannotRetrieveSalesReportWhenUserIsNotBusinessAdmin() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(anotherUser.getSessionUUID()))
                .thenReturn(Optional.ofNullable(anotherUser));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", anotherUser.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }

    /**
     * Tests a user cannot retrieve a Sales Report when they are not logged in
     * @throws Exception MockMvc
     */
    @Test
    void cannotRetrieveSalesReportWhenUserIsNotLoggedIn() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", "0"))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    /**
     * Tests a user cannot retrieve a Sales Report when the business does not exist
     * @throws Exception MockMvc
     */
    @Test
    void cannotRetrieveSalesReportWhenBusinessDoesNotExist() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", 0))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_ACCEPTABLE.value());
    }

    /**
     * Tests a user cannot retrieve a Sales Report when the "from date" is after the "to date"
     * @throws Exception MockMvc
     */
    @Test
    void cannotRetrieveSalesReportWhenFromDateIsAfterToDate() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 5, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-05-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Total"))
                .andReturn().getResponse();

        // Then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Tests a user can retrieve a Sales Report when they are an admin of the business and the granularity is set to
     * "Yearly"
     * @throws Exception MockMvc
     */
    @Test
    void canRetrieveSalesReportWhenUserIsBusinessAdminAndGranularityIsYearly() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 1, 20, 0, 0),
                LocalDateTime.of(2021, 3, 30, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Yearly"))
                .andReturn().getResponse();

        // Then
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isEqualTo("2021");
        assertThat(responseList.get(0).getTotalSales()).isEqualTo(1);
        assertThat(responseList.get(0).getTotalRevenue()).isEqualTo(soldListing.getPrice());
    }

    /**
     * Tests a user can retrieve a Sales Report when they are an admin of the business and the granularity is set to
     * "Monthly"
     * @throws Exception MockMvc
     */
    @Test
    void canRetrieveSalesReportWhenUserIsBusinessAdminAndGranularityIsMonthly() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 2, 1, 0, 0),
                LocalDateTime.of(2021, 2, 28, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-01-20T00:00")
                        .param("toDate", "2021-03-30T00:00")
                        .param("granularity", "Monthly"))
                .andReturn().getResponse();

        // Then
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isEqualTo("January 2021");
        assertThat(responseList.get(1).getGranularityName()).isEqualTo("February 2021");
        assertThat(responseList.get(2).getGranularityName()).isEqualTo("March 2021");
        assertThat(responseList.get(1).getTotalSales()).isEqualTo(1);
        assertThat(responseList.get(1).getTotalRevenue()).isEqualTo(soldListing.getPrice());
    }

    /**
     * Tests a user can retrieve a Sales Report when they are an admin of the business and the granularity is set to
     * "Daily"
     * @throws Exception MockMvc
     */
    @Test
    void canRetrieveSalesReportWhenUserIsBusinessAdminAndGranularityIsDaily() throws Exception {
        // Given
        given(businessRepository.findBusinessById(business.getId())).willReturn(Optional.ofNullable(business));
        given(soldListingRepository.findAllByBusinessIdAndSaleDateBetween(
                business.getId(),
                LocalDateTime.of(2021, 2, 25, 0, 0),
                LocalDateTime.of(2021, 2, 25, 0, 0).with(LocalTime.MAX)
        )).willReturn(List.of(soldListing));

        expectedJSON = String.format(expectedSoldListingsJSON, soldListing.getId(),
                soldListing.getSaleDate(), soldListing.getListingDate(),
                soldListing.getProductId(), soldListing.getQuantity(),
                soldListing.getPrice(), soldListing.getBookmarks(), soldListing.getCustomer().toUserPayloadSecure());

        // When
        when(userRepository.findBySessionUUID(user.getSessionUUID())).thenReturn(Optional.ofNullable(user));
        response = mvc.perform(get(String.format("/businesses/%d/salesReport", business.getId()))
                        .cookie(new Cookie("JSESSIONID", user.getSessionUUID()))
                        .param("fromDate", "2021-02-24T00:00")
                        .param("toDate", "2021-02-26T00:00")
                        .param("granularity", "Daily"))
                .andReturn().getResponse();

        // Then
        List<SalesReportPayload> responseList = mapper.readValue(
                response.getContentAsString(), new TypeReference<>(){}
        );
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseList.get(0).getGranularityName()).isEqualTo("24 February 2021");
        assertThat(responseList.get(1).getGranularityName()).isEqualTo("25 February 2021");
        assertThat(responseList.get(2).getGranularityName()).isEqualTo("26 February 2021");
        assertThat(responseList.get(1).getTotalSales()).isEqualTo(1);
        assertThat(responseList.get(1).getTotalRevenue()).isEqualTo(soldListing.getPrice());
    }
}
