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
    void whenFindAllBusinessesByNames_thenReturnNameOrderedBusinessesAscendingTest() {
        // given
        int pageNo = 0;
        int pageSize = 11;
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
        Page<Business> businessPage = businessRepository.findAllBusinessesByName("", pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
        }
    }

    /**
     * Tests that the search functionality will order businesses by name in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllBusinessesByName_thenReturnNameOrderedBusinessesDescending() {
        // given
        int pageNo = 0;
        int pageSize = 11;
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
        Page<Business> businessPage = businessRepository.findAllBusinessesByName("", pageable);

        // then
        for (int i = 0; i < businessPage.getContent().size(); i++) {
            assertThat(businessPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
        }
    }
}
