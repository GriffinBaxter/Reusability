package org.seng302.business.soldListing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.SoldListingRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class SoldListingRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SoldListingRepository soldListingRepository;

    private Address address;

    private User user;

    private Business business;
    private Integer businessId;
    private Business anotherBusiness;
    private Integer anotherBusinessId;

    private Product product;
    private Product anotherProduct;

    private InventoryItem inventoryItem;

    private Listing listing;

    private SoldListing soldListing;
    private SoldListing anotherSoldListing;

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

        anotherProduct = new Product(
                "BANANA",
                business,
                "Banana",
                "A Description",
                "Manufacturer",
                21.00,
                "9400547002634"
        );
        entityManager.persist(anotherProduct);
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

        soldListing = new SoldListing(
                business,
                user,
                LocalDateTime.now().minusDays(3),
                product.getId(),
                listing.getQuantity(),
                listing.getPrice(),
                0);
        entityManager.persist(soldListing);
        entityManager.flush();

        anotherSoldListing = new SoldListing(
                business,
                user,
                LocalDateTime.now().minusDays(3),
                anotherProduct.getId(),
                listing.getQuantity(),
                listing.getPrice(),
                0);
        entityManager.persist(anotherSoldListing);
        entityManager.flush();
    }

    /**
     * Test findAllByBusinessId when businessId is valid and Sold Listing exists
     */
    @Test
    void testFindAllByBusinessId_whenBusinessIdExistsAndSoldListingsExist() {
        // When
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("id")));
        Page<SoldListing> retrievedSoldListings = soldListingRepository.findAllByBusinessId(businessId, pageable);

        // Then
        assertThat(retrievedSoldListings.getContent().size()).isNotZero();
        for (Integer i = 0; i < retrievedSoldListings.getContent().size(); i++) {
            assertThat(retrievedSoldListings.getContent().get(i).getBusiness().getId()).isEqualTo(this.businessId);
        }
    }

    /**
     * Test findAllByBusinessId when businessId is valid but no Sold Listings exist
     */
    @Test
    void testFindAllByBusinessId_whenBusinessIdExistsButNoSoldListingsExist() {
        // When
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("id")));
        Page<SoldListing> retrievedSoldListings = soldListingRepository.findAllByBusinessId(anotherBusinessId, pageable);

        // Then
        assertThat(retrievedSoldListings.getContent().size()).isZero();
    }

    /**
     * Test findAllByBusinessId when businessId doesn't exist
     */
    @Test
    void testFindAllByBusinessId_whenBusinessIdDoesNotExist() {
        // When
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.asc("id")));
        Page<SoldListing> retrievedSoldListings = soldListingRepository.findAllByBusinessId(99999, pageable);

        // Then
        assertThat(retrievedSoldListings.getContent().size()).isZero();
    }

}
