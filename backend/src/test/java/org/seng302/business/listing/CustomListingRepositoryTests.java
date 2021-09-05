package org.seng302.business.listing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ListingRepositoryCustomImpl test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class CustomListingRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListingRepository listingRepository;

    private Address address;

    private User user;

    private Business business;
    private Business anotherBusiness;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private List<Product> products;

    private InventoryItem inventoryItem1;
    private InventoryItem inventoryItem2;
    private InventoryItem inventoryItem3;
    private InventoryItem inventoryItem4;
    private InventoryItem inventoryItem5;
    private List<InventoryItem> inventoryItems;

    private Listing listing1;
    private Listing listing2;
    private Listing listing3;
    private Listing listing4;
    private Listing listing5;
    private List<Listing> listings;

    /**
     * Creates and inserts all entities needed for testing.
     * Any exception.
     */
    @BeforeEach
    void setup() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        entityManager.persist(address);
        entityManager.flush();
        Address anotherAddress = new Address(
                "100",
                "Ilam Road",
                "Invercargill",
                "Southland",
                "New Zealand",
                "90210",
                "Georgetown"
        );
        entityManager.persist(anotherAddress);
        entityManager.flush();
        user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        user = entityManager.persist(user);
        entityManager.flush();
        business = new Business(
                user.getId(),
                "Bus1ness",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        business = entityManager.persist(business);
        entityManager.flush();

        anotherBusiness = new Business(
                user.getId(),
                "Business2",
                "some text 2",
                anotherAddress,
                BusinessType.NON_PROFIT_ORGANISATION,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        anotherBusiness = entityManager.persist(anotherBusiness);
        entityManager.flush();

        product1 = new Product(
                "APPLE",
                business,
                "Apple",
                "A Description",
                "Manufacturer",
                21.00,
                "9400547002634"
        );
        product2 = new Product(
                "BEAN",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                20.00,
                "9400547002634"
        );
        product3 = new Product(
                "BEAN3",
                anotherBusiness,
                "Beans",
                "Description",
                "A Manufacturer",
                11.00,
                "9400547002634"
        );
        product4 = new Product(
                "DUCT",
                business,
                "Duct-Tape",
                "Brand new Description",
                "A New Manufacturer",
                10.00,
                "9400547002634"
        );
        product5 = new Product(
                "PROD",
                business,
                "Product",
                "New Description",
                "New Manufacturer",
                10.00,
                "9400547002627"
        );

        products = List.of(product1, product2, product3, product4, product5);
        for (Product product : products) {
            entityManager.persist(product);
        }
        entityManager.flush();

        inventoryItem1 = new InventoryItem(product1,
                "APPLE",
                45,
                6.5,
                21.99,
                LocalDate.of(2020, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 12, 25));

        inventoryItem2 = new InventoryItem(product2,
                "BEAN",
                6,
                17.44,
                27.00,
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2022, 4, 25),
                LocalDate.of(2022, 4, 25),
                LocalDate.of(2022, 12, 25));

        inventoryItem3 = new InventoryItem(product3,
                "BEAN3",
                40,
                4.99,
                32.23,
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2023, 4, 25),
                LocalDate.of(2023, 4, 25),
                LocalDate.of(2024, 12, 25));

        inventoryItem4 = new InventoryItem(product4,
                "DUCT",
                13,
                6.5,
                29.99,
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2023, 4, 25),
                LocalDate.of(2023, 4, 25),
                LocalDate.of(2023, 12, 25));

        inventoryItem5 = new InventoryItem(product5,
                "PROD",
                40,
                6.5,
                21.99,
                LocalDate.of(2020, 5, 25),
                LocalDate.of(2022, 4, 25),
                LocalDate.of(2023, 4, 25),
                LocalDate.of(2023, 12, 25));

        inventoryItems = List.of(inventoryItem1, inventoryItem2, inventoryItem3, inventoryItem4, inventoryItem5);
        for (InventoryItem inventoryItem : inventoryItems) {
            entityManager.persist(inventoryItem);
        }

        entityManager.flush();
        listing1 = new Listing(inventoryItem1,
                5,
                12.0,
                "",
                LocalDateTime.of(LocalDate.of(2020, 8, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));

        listing2 = new Listing(inventoryItem2,
                4,
                1.2,
                "",
                LocalDateTime.of(LocalDate.of(2020, 10, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 4, 1), LocalTime.of(0,0,0)));

        listing3 = new Listing(inventoryItem3,
                3,
                1.50,
                "",
                LocalDateTime.of(LocalDate.of(2021, 3, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 3, 1), LocalTime.of(0,0,0)));

        listing4 = new Listing(inventoryItem4,
                2,
                11.20,
                "",
                LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2021, 12, 1), LocalTime.of(0,0,0)));

        listing5 = new Listing(inventoryItem5,
                1,
                15.20,
                "",
                LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));

        listings = List.of(listing1, listing2, listing3, listing4, listing5);
        for (Listing listing : listings) {
            entityManager.persist(listing);
        }
        entityManager.flush();
    }

    // -------------------------- findAllListingsByProductName --------------------------

    /**
     * Tests findAllListingsByProductName when query exists and no optional filters are used
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_QueriedListingsExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Be");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");
        expectedIds.add("BEAN3");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllListingsByProductName when query exists and no optional filters are used
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_MultipleQueriedListingsExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Be");
        strings.add("B");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");
        expectedIds.add("BEAN3");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllListingsByProductName when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindAllListingsByProductName_QueriedListingsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");


        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isZero();
    }

    /**
     * Tests findAllListingsByProductName when query is empty and Business Type is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_BusinessTypeExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Be");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, List.of(BusinessType.RETAIL_TRADE), null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and minimum price is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_MinimumPriceExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        Double minimumPrice = 10.0;

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, minimumPrice, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getPrice()).isGreaterThanOrEqualTo(minimumPrice);
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and maximum price is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_MaximumPriceExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        Double maximumPrice = 10.0;

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, maximumPrice, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getPrice()).isLessThanOrEqualTo(maximumPrice);
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and minimum and maximum price is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_PriceRangeExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        Double minimumPrice = 10.0;
        Double maximumPrice = 12.0;

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, minimumPrice, maximumPrice, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getPrice()).isLessThanOrEqualTo(maximumPrice);
            assertThat(listingsPage.getContent().get(i).getPrice()).isGreaterThanOrEqualTo(minimumPrice);
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and from date is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_FromDateExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0,0,0);

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, fromDate, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getCloses().getYear()).isGreaterThanOrEqualTo(fromDate.getYear());
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and to date is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_ToDateExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        LocalDateTime toDate = LocalDateTime.of(2022, 1, 1, 0,0,0);

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, null, toDate, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getCloses().getYear()).isLessThanOrEqualTo(toDate.getYear());
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and from and to date is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_DateRangeExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        LocalDateTime toDate = LocalDateTime.of(2022, 12, 31, 23,59,59);
        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0,0,0);

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, fromDate, toDate, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getCloses().getYear()).isEqualTo(toDate.getYear());
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and barcode is set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_BarcodeExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        String barcode = "9400547002634";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, null, null, null, null, null, barcode);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBarcode()).isEqualTo(barcode);
        }
    }

    /**
     * Tests findAllListingsByProductName when query is empty and all optional filters are set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_OptionalFiltersExists() {
        List<String> strings = new ArrayList<>();
        strings.add("");

        LocalDateTime toDate = LocalDateTime.of(2022, 12, 31, 23,59,59);
        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0,0,0);

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, List.of(BusinessType.RETAIL_TRADE), 5.0, 50.0, fromDate, toDate, "9400547002627");

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getCloses().getYear()).isEqualTo(toDate.getYear());
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getBusinessType()).isEqualTo(BusinessType.RETAIL_TRADE);
            assertThat(listingsPage.getContent().get(i).getPrice()).isBetween(5.0, 50.0);
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBarcode()).isEqualTo("9400547002627");
        }
    }

    /**
     * Tests findAllListingsByProductName when query exists and all optional filters are set
     * Returns list of Listings
     */
    @Test
    void whenFindAllListingsByProductName_whenQueryExistsAndOptionalFiltersExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Apple");

        LocalDateTime toDate = LocalDateTime.of(2022, 12, 31, 23,59,59);
        LocalDateTime fromDate = LocalDateTime.of(2022, 1, 1, 0,0,0);

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByProductName(strings, pageable, List.of(BusinessType.RETAIL_TRADE), 5.0, 50.0, fromDate, toDate, "9400547002634");

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getCloses().getYear()).isEqualTo(toDate.getYear());
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getBusinessType()).isEqualTo(BusinessType.RETAIL_TRADE);
            assertThat(listingsPage.getContent().get(i).getPrice()).isBetween(5.0, 50.0);
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBarcode()).isEqualTo("9400547002634");
        }
    }

    // -------------------------- findAllListingsByLocation --------------------------

    /**
     * Tests findAllListingsByLocation when finding a suburb
     */
    @Test
    void whenFindAllListingsByLocation_SuburbExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Georgetown");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByLocation(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getAddress().getSuburb()).isEqualTo(strings.get(0));
        }
    }

    /**
     * Tests findAllListingsByLocation when finding a city
     */
    @Test
    void whenFindAllListingsByLocation_CityExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Christchurch");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByLocation(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getAddress().getCity()).isEqualTo(strings.get(0));
        }
    }

    /**
     * Tests findAllListingsByLocation when finding a country
     */
    @Test
    void whenFindAllListingsByLocation_CountryExists() {
        List<String> strings = new ArrayList<>();
        strings.add("New Zealand");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByLocation(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getAddress().getCountry()).isEqualTo(strings.get(0));
        }
    }

    /**
     * Tests findAllListingsByLocation with multiple search strings
     */
    @Test
    void whenFindAllListingsByLocation_MultipleQueriesExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Georgetown");
        strings.add("Invercargill");
        strings.add("Australia");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByLocation(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getAddress().getSuburb()).isEqualTo(strings.get(0));
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getAddress().getCity()).isEqualTo(strings.get(1));

        }
    }

    // -------------------------- findAllListingsByBusinessName --------------------------

    /**
     * Tests findAllListingsByBusinessName when query exists
     */
    @Test
    void whenFindAllListingsByBusinessName_QueryExists() {
        List<String> strings = new ArrayList<>();
        strings.add("Bus1ness");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByBusinessName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getName()).isEqualTo(strings.get(0));
        }
    }

    /**
     * Tests findAllListingsByBusinessName with multiple existing queries
     */
    @Test
    void whenFindAllListingsByBusinessName_multipleQueriesExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Bus1ness");
        strings.add("idk");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByBusinessName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isNotZero();
        for (int i = 0; i < listingsPage.getContent().size(); i++){
            assertThat(listingsPage.getContent().get(i).getInventoryItem().getProduct().getBusiness().getName()).isEqualTo(strings.get(0));
        }
    }

    /**
     * Tests findAllListingsByBusinessName with non existent query
     */
    @Test
    void whenFindAllListingsByBusinessName_fakeQuery() {
        List<String> strings = new ArrayList<>();
        strings.add("idk");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Listing> listingsPage = listingRepository.findAllListingsByBusinessName(strings, pageable, null, null, null, null, null, null);

        // then
        assertThat(listingsPage.getContent().size()).isZero();
    }
}
