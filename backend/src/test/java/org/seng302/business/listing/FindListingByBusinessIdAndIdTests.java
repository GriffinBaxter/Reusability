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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * FindListingsByBusinessIdAndId test class - specifically for testing the pagination and ordering of the
 * FindListingsByBusinessIdAndId method in ListingRepository.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class FindListingByBusinessIdAndIdTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ListingRepository listingRepository;

    private Address address;

    private User user;

    private Business business;
    private Integer businessId;
    private Business anotherBusiness;
    private Integer anotherBusinessId;

    private Product product;

    private InventoryItem inventoryItem;

    private Listing listing;

    /**
     * Creates and inserts all entities needed for testing.
     * @exception Exception Any exception.
     */
    @BeforeEach
    void setup() throws Exception {
        // Given
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
                "example name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );
        business = entityManager.persist(business);
        businessId = business.getId();
        entityManager.flush();

        anotherBusiness = new Business(
                user.getId(),
                "example name 2",
                "some text 2",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );
        anotherBusiness = entityManager.persist(anotherBusiness);
        anotherBusinessId = anotherBusiness.getId();
        entityManager.flush();

        product = new Product(
                "APPLE",
                business,
                "Apple",
                "A Description",
                "Manufacturer",
                21.00,
                "9400547002634"
        );
        entityManager.persist(product);
        entityManager.flush();

        inventoryItem = new InventoryItem(product,
                "APPLE",
                45,
                6.5,
                21.99,
                LocalDate.of(2020, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 4, 25),
                LocalDate.of(2021, 12, 25));
        entityManager.persist(inventoryItem);
        entityManager.flush();

        listing = new Listing(inventoryItem,
                5,
                12.0,
                "",
                LocalDateTime.of(LocalDate.of(2020, 8, 1), LocalTime.of(0,0,0)),
                LocalDateTime.of(LocalDate.of(2022, 10, 1), LocalTime.of(0,0,0)));
        entityManager.persist(listing);
        entityManager.flush();

    }

    /**
     * Tests that the (correct) listing is returned when calling findByBusinessIdAndId() with an existing business Id and listing Id
     */
    @Test
    void whenFindListingByBusinessIdAndIdWithValidBusinessIdAndListingId_thenReturnListing() {

        // When
        Optional<Listing> retrievedListing = listingRepository.findListingByBusinessIdAndId(business.getId(), listing.getId());

        // Then
        assertThat(retrievedListing).isPresent();
        Listing foundListing = retrievedListing.get();
        assertThat(foundListing.getId()).isEqualTo(listing.getId());

    }

    /**
     * Tests that no listing is returned when calling findByBusinessIdAndId() with an existing business Id but a non-existent listing Id
     */
    @Test
    void whenFindListingByBusinessIdAndIdWithValidBusinessIdAndInvalidListingId_thenDontReturnListing() {

        // When
        Optional<Listing> retrievedListing = listingRepository.findListingByBusinessIdAndId(business.getId(), 999999);

        // Then
        assertThat(retrievedListing).isNotPresent();

    }

    /**
     * Tests that no listing is returned when calling findByBusinessIdAndId() with a non-existent business Id.
     */
    @Test
    void whenFindListingByBusinessIdAndIdWithInvalidBusinessId_thenDontReturnListing() {

        // When
        Optional<Listing> retrievedListing = listingRepository.findListingByBusinessIdAndId(999999, 999999);

        // Then
        assertThat(retrievedListing).isNotPresent();

    }

}
