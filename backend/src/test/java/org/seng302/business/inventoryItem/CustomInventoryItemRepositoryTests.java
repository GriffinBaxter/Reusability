package org.seng302.business.inventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.InventoryItemRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * InventoryItemRepositoryCustom test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class CustomInventoryItemRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    private Address address;

    private User user;

    private Business business;
    private Business anotherBusiness;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private List<Product> products;

    private InventoryItem inventoryItem1;
    private InventoryItem inventoryItem2;
    private InventoryItem inventoryItem3;
    private InventoryItem inventoryItem4;
    private InventoryItem inventoryItem5;
    private List<InventoryItem> inventoryItems;

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
                "9300675024235"
        );
        product2 = new Product(
                "BEAN",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                20.00,
                "9310072000015"
        );
        product3 = new Product(
                "BEAN3",
                anotherBusiness,
                "Beans",
                "Description",
                "A Manufacturer",
                11.00,
                "9310434001759"
        );
        product4 = new Product(
                "DUCT",
                business,
                "Duct-Tape",
                "Brand new Description",
                "A New Manufacturer",
                10.00,
                "9310434001759"
        );

        products = List.of(product1, product2, product3, product4);
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

        inventoryItem5 = new InventoryItem(product4,
                "DUCT",
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
    }

    /**
     * Tests findInventoryItemsByBarcodeAndBusinessId when the barcode exists for an inventory item for the business
     * Returns page of valid inventory items
     */
    @Test
    void whenFindInventoryItemsByBarcodeAndBusinessId_barcodeExistsForBusiness() {
        String barcode = "9300675024235";

        String expectedProductId = "APPLE";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, business.getId(), pageable);

        // then
        assertThat(inventoryItemPage.getContent().size()).isNotZero();
        for (int i = 0; i < inventoryItemPage.getContent().size(); i++){
            assertThat(inventoryItemPage.getContent().get(i).getProduct().getProductId()).isEqualTo(expectedProductId);
        }
    }

    /**
     * Tests findInventoryItemsByBarcodeAndBusinessId when the barcode exists for multiple inventory item for the business
     * Returns page of valid inventory items
     */
    @Test
    void whenFindInventoryItemsByBarcodeAndBusinessId_MultipleInventoryItems_barcodeExistsForBusiness() {
        String barcode = "9310434001759";

        String expectedProductId = "DUCT";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, business.getId(), pageable);

        // then
        assertThat(inventoryItemPage.getContent().size()).isEqualTo(2);
        for (int i = 0; i < inventoryItemPage.getContent().size(); i++){
            assertThat(inventoryItemPage.getContent().get(i).getProduct().getProductId()).isEqualTo(expectedProductId);
        }
    }

    /**
     * Tests findInventoryItemsByBarcodeAndBusinessId when the barcode exists for a inventory item for the business and a different business
     * Returns page of valid inventory items
     */
    @Test
    void whenFindInventoryItemsByBarcodeAndBusinessId_barcodeExistsForTwoBusinesses() {
        String barcode = "9310434001759";

        String expectedProductId = "BEAN3";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, anotherBusiness.getId(), pageable);

        // then
        assertThat(inventoryItemPage.getContent().size()).isNotZero();
        for (int i = 0; i < inventoryItemPage.getContent().size(); i++){
            assertThat(inventoryItemPage.getContent().get(i).getProduct().getProductId()).isEqualTo(expectedProductId);
        }
    }

    /**
     * Tests findInventoryItemsByBarcodeAndBusinessId when the barcode doesn't exist for a inventory item for the business
     * Returns empty page
     */
    @Test
    void whenFindInventoryItemsByBarcodeAndBusinessId_barcodeNotExist() {
        String barcode = "9310434001359";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, business.getId(), pageable);

        // then
        assertThat(inventoryItemPage.getContent().size()).isZero();
    }

    /**
     * Tests findInventoryItemsByBarcodeAndBusinessId when the barcode exists for a inventory item but the business doesn't
     * Returns empty page
     */
    @Test
    void whenFindInventoryItemsByBarcodeAndBusinessId_businessNotExist() {
        String barcode = "9310434001759";

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryItemPage = inventoryItemRepository.findInventoryItemsByBarcodeAndBusinessId(barcode, 99999, pageable);

        // then
        assertThat(inventoryItemPage.getContent().size()).isZero();
    }
}
