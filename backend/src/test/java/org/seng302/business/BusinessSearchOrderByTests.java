package org.seng302.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.User;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.BusinessRepository;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BusinessSearchOrderBy test class - specifically for testing the searching business by name and/or business type
 * features of the UserRepository class.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class BusinessSearchOrderByTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusinessRepository businessRepository;

    private User user;
    private Address address;
    private Address anotherAddress;
    private Address anotherAnotherAddress;
    private Business searchBusiness1;
    private Business searchBusiness2;
    private Business searchBusiness3;
    private Business searchBusiness4;
    private Business searchBusiness5;
    private Business searchBusiness6;
    private Business searchBusiness7;
    private Business searchBusiness8;
    private Business searchBusiness9;
    private Business searchBusiness10;
    private List<Business> searchBusinesses;
    private List<String> names = new ArrayList<>();

    /**
     * Creates and inserts all businesses for testing.
     * Ideally this would be BeforeAll.
     * BeforeEach works but will replace all businesses before each test. Only functional difference when testing is that they will have new IDs.
     * @throws Exception Any exception.
     */
    @BeforeEach
    void setup() throws Exception {

        address = new Address("325", "Citlalli Track", "New Lois", "Heard Island and McDonald Islands", "HM", "Antarctica", "Pingu");
        entityManager.persist(address);
        entityManager.flush();

        anotherAddress = new Address("3396", "Bertram Parkway", "Central", "Central Otago", "New Zealand", "1111", "Wanaka");
        entityManager.persist(anotherAddress);
        entityManager.flush();

        anotherAnotherAddress = new Address("14798", "Terry Highway", "Queenstown-Lakes", "District", "New Zealand", "2982", "Frankton");
        entityManager.persist(anotherAnotherAddress);
        entityManager.flush();

        user = new User(
                "Johnny",
                "Doe",
                "Pete",
                "Aldeniz",
                "Biography",
                "email@email.com",
                LocalDate.of(2000, 2, 2),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);

        entityManager.persist(user);
        entityManager.flush();

        searchBusiness1 = new Business(
                1,
                "Christchurch General Store",
                "Description",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );

        searchBusiness2 = new Business(
                1,
                "Wanaka General Store",
                "Description 2",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );

        searchBusiness3 = new Business(
                1,
                "Auckland General Store",
                "Description 3",
                anotherAddress,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );

        searchBusiness4 = new Business(
                1,
                "Wellington General Store",
                "Description 4",
                anotherAddress,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );

        searchBusiness5 = new Business(
                1,
                "Dunedin General Store",
                "Description 5",
                anotherAnotherAddress,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );

        searchBusiness6 = new Business(
                1,
                "Queenstown General Store",
                "Description 6",
                anotherAnotherAddress,
                BusinessType.CHARITABLE_ORGANISATION,
                LocalDateTime.now(),
                user
        );

        searchBusiness7 = new Business(
                1,
                "Ashburton General Store",
                "Description 7",
                address,
                BusinessType.CHARITABLE_ORGANISATION,
                LocalDateTime.now(),
                user
        );

        searchBusiness8 = new Business(
                1,
                "Gisborne General Store",
                "Description 8",
                address,
                BusinessType.NON_PROFIT_ORGANISATION,
                LocalDateTime.now(),
                user
        );

        searchBusiness9 = new Business(
                1,
                "Gore General Store",
                "Description 9",
                anotherAddress,
                BusinessType.NON_PROFIT_ORGANISATION,
                LocalDateTime.now(),
                user
        );

        searchBusiness10 = new Business(
                1,
                "Picton General Store",
                "Description 10",
                anotherAnotherAddress,
                BusinessType.NON_PROFIT_ORGANISATION,
                LocalDateTime.now(),
                user
        );


        searchBusinesses = List.of(searchBusiness1, searchBusiness2, searchBusiness3, searchBusiness4, searchBusiness5,
                searchBusiness6, searchBusiness7, searchBusiness8, searchBusiness9, searchBusiness10);

        for (Business searchBusiness: searchBusinesses) {
            entityManager.persist(searchBusiness);
        }
        entityManager.flush();
    }

    /**
     * Tests that the search functionality will order businesses by name in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllBusinesses_thenReturnNameOrderedBusinessesAscendingTest() {
        // given
        int pageNo = 0;
        int pageSize = 10;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNames = new ArrayList<>();
        orderedNames.add("Ashburton General Store");
        orderedNames.add("Auckland General Store");
        orderedNames.add("Christchurch General Store");
        orderedNames.add("Dunedin General Store");
        orderedNames.add("Gisborne General Store");
        orderedNames.add("Gore General Store");
        orderedNames.add("Picton General Store");
        orderedNames.add("Queenstown General Store");
        orderedNames.add("Wanaka General Store");
        orderedNames.add("Wellington General Store");

        // when
        Page<Business> businessPage = businessRepository.findAll(pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
        }
    }

    /**
     * Tests that the search functionality will order businesses by name in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnNameOrderedBusinessesDescendingTest() {
        // given
        int pageNo = 0;
        int pageSize = 10;
        Sort sortBy = Sort.by(Sort.Order.desc("name").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNames = new ArrayList<>();

        orderedNames.add("Wellington General Store");
        orderedNames.add("Wanaka General Store");
        orderedNames.add("Queenstown General Store");
        orderedNames.add("Picton General Store");
        orderedNames.add("Gore General Store");
        orderedNames.add("Gisborne General Store");
        orderedNames.add("Dunedin General Store");
        orderedNames.add("Christchurch General Store");
        orderedNames.add("Auckland General Store");
        orderedNames.add("Ashburton General Store");

        // when
        Page<Business> businessPage = businessRepository.findAllBusinessesByNames(names, pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
        }
    }

    /**
     * Tests that the search functionality will order businesses by address in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnAddressOrderedBusinessesAscendingTest() {
        // given
        int pageNo = 0;
        int pageSize = 10;
        Sort sortBy = Sort.by(Sort.Order.desc("address.street_name").ignoreCase())
                .and(Sort.by(Sort.Order.desc("address.suburb").ignoreCase()))
                .and(Sort.by(Sort.Order.desc("address.city").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("address.region").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("address.country").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedAddress = new ArrayList<>();

        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");
        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");
        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");

        // when
        Page<Business> businessPage = businessRepository.findAllBusinessesByNames(names, pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getAddress().toOneLineString()).isEqualTo(orderedAddress.get(i));
        }
    }

    /**
     * Tests that the search functionality will order businesses by address in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnAddressOrderedBusinessesDescendingTest() {
        // given
        int pageNo = 0;
        int pageSize = 10;
        Sort sortBy = Sort.by(Sort.Order.desc("address.street_name").ignoreCase())
                .and(Sort.by(Sort.Order.desc("address.suburb").ignoreCase()))
                .and(Sort.by(Sort.Order.desc("address.city").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("address.region").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("address.country").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedAddress = new ArrayList<>();

        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");
        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");
        orderedAddress.add("14798, Terry Highway, Queenstown-Lakes, District, New Zealand, 2982, Frankton");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("325, Citlalli Track, New Lois, Heard Island and McDonald Islands, HM, Antarctica, Pingu");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");
        orderedAddress.add("3396, Bertram Parkway, Central, Central Otago, New Zealand, 1111, Wanaka");

        // when
        Page<Business> businessPage = businessRepository.findAllBusinessesByNames(names, pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getAddress().toOneLineString()).isEqualTo(orderedAddress.get(i));
        }
    }

    /**
     * Tests that the search functionality will return paginated results correctly when the page is not full with
     * businesses.
     */
    @Test
    void whenFindAllBusinesses_thenReturnPageHalfFullTest() {
        // given
        int pageNo = 0;
        // Page size 20 means page will be half full with the default 10 businesses inserted.
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<Business> businessPage = businessRepository.findAll(pageable);

        // then
        assertThat(businessPage.getTotalElements()).isEqualTo(10);
        for (int i = 0; i < searchBusinesses.size(); i++) {
            assertThat(businessPage.getContent().get(i)).isEqualTo(searchBusinesses.get(i));
        }
    }

    /**
     * Tests that the search functionality will return an empty page when given a filter value
     * that does not match anything in the database.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnEmptyPageTest() {
        // given
        int pageNo = 0;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        List<String> names = Arrays.asList("ThisValueDoesNotExist");

        // when
        Page<Business> businessPage = businessRepository.findAllBusinessesByNames(names, pageable);

        // then
        assertThat(businessPage.getTotalElements()).isZero();
        assertThat(businessPage.getTotalPages()).isZero();
    }

    /**
     * Tests that the search functionality will return pages other than the first one with correct businesses.
     */
    @Test
    void whenFindAllBusinesses_thenReturnPagesFromTwoOnwardTest() {
        // given
        int pageSize = 2;

        // when
        Page<Business> businessPage2 = businessRepository.findAll(PageRequest.of(1, pageSize));
        Page<Business> businessPage3 = businessRepository.findAll(PageRequest.of(2, pageSize));
        Page<Business> businessPage4 = businessRepository.findAll(PageRequest.of(3, pageSize));
        Page<Business> businessPage5 = businessRepository.findAll(PageRequest.of(4, pageSize));

        // then
        assertThat(businessPage2.getTotalPages()).isEqualTo(5);
        assertThat(businessPage2.getContent().get(0)).isEqualTo(searchBusinesses.get(2));
        assertThat(businessPage2.getContent().get(1)).isEqualTo(searchBusinesses.get(3));
        assertThat(businessPage3.getContent().get(0)).isEqualTo(searchBusinesses.get(4));
        assertThat(businessPage3.getContent().get(1)).isEqualTo(searchBusinesses.get(5));
        assertThat(businessPage4.getContent().get(0)).isEqualTo(searchBusinesses.get(6));
        assertThat(businessPage4.getContent().get(1)).isEqualTo(searchBusinesses.get(7));
        assertThat(businessPage5.getContent().get(0)).isEqualTo(searchBusinesses.get(8));
        assertThat(businessPage5.getContent().get(1)).isEqualTo(searchBusinesses.get(9));
    }

    /**
     * Tests that the search functionality will return the page correctly when the page is full.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnFullPageTest() {
        // given
        int pageNo = 0;
        // Page size 8 means tested page will be full as there are 10 total values
        int pageSize = 8;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<Business> businessPage = businessRepository.findAll(pageable);

        // then
        assertThat(businessPage.getTotalPages()).isEqualTo(2);
        assertThat(businessPage.getSize()).isEqualTo(8);
        for (int i = 0; i < businessPage.getSize(); i++) {
            assertThat(businessPage.getContent().get(i)).isEqualTo(searchBusinesses.get(i));
        }
    }

    /**
     * Tests that the search functionality ordering works across pages, not just within a single page.
     *  I.e. That data is ordered 'globally' from all results in the database,
     *      not just the few values that are returned are correctly ordered.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnGloballyOrderedBusinessesTest() {
        // given
        int pageNo = 1;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Business> businessPage = businessRepository.findAll(pageable);

        // then
        assertThat(businessPage.getTotalPages()).isEqualTo(4);
        assertThat(businessPage.getSize()).isEqualTo(3);
        assertThat(businessPage.getContent().get(0).getName()).isEqualTo("Dunedin General Store");
        assertThat(businessPage.getContent().get(1).getName()).isEqualTo("Gisborne General Store");
        assertThat(businessPage.getContent().get(2).getName()).isEqualTo("Gore General Store");
    }
}
