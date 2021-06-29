package org.seng302.business.inventoryItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.Product;
import org.seng302.Main;
import org.seng302.model.InventoryItem;
import org.seng302.model.repository.InventoryItemRepository;
import org.seng302.model.enums.Role;
import org.seng302.model.User;
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
 * FindInventoryItemsByBusinessId test class - specifically for testing the pagination and ordering of the
 * findInventoryItemsByBusinessId method in InventoryItemsRepository.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class FindInventoryItemsByBusinessIdTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    private Address address;

    private User user;

    private Business business;
    private Integer businessId;

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

        product1 = new Product(
                "APPLE",
                business,
                "Apple",
                "A Description",
                "Manufacturer",
                21.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        product2 = new Product(
                "APP-LE",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                20.00,
                LocalDateTime.of(LocalDate.of(2020, 1, 1),
                        LocalTime.of(0, 0))
        );
        product3 = new Product(
                "APP-LE3",
                business,
                "Beans",
                "Description",
                "A Manufacturer",
                11.00,
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        product4 = new Product(
                "DUCT",
                business,
                "Duct-Tape",
                "Brand new Description",
                "A New Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 1),
                        LocalTime.of(0, 0))
        );
        product5 = new Product(
                "PROD",
                business,
                "Product",
                "New Description",
                "New Manufacturer",
                10.00,
                LocalDateTime.of(LocalDate.of(2021, 2, 1),
                        LocalTime.of(1, 0))
        );

        products = List.of(product1, product2, product3, product4, product5);
        for (Product product : products) {
            entityManager.persist(product);
        }
        entityManager.flush();

        inventoryItem1 = new InventoryItem(product1,
                "APPLE",
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 04, 25),
                LocalDate.of(2021, 04, 25),
                LocalDate.of(2021, 04, 25),
                LocalDate.of(2021, 12, 25));

        inventoryItem2 = new InventoryItem(product2,
                "APP-LE",
                6,
                17.44,
                27.00,
                LocalDate.of(2021, 04, 25),
                LocalDate.of(2022, 04, 25),
                LocalDate.of(2022, 04, 25),
                LocalDate.of(2022, 12, 25));

        inventoryItem3 = new InventoryItem(product3,
                "APP-LE3",
                40,
                4.99,
                32.23,
                LocalDate.of(2021, 04, 25),
                LocalDate.of(2023, 04, 25),
                LocalDate.of(2023, 04, 25),
                LocalDate.of(2024, 12, 25));

        inventoryItem4 = new InventoryItem(product4,
                "DUCT",
                13,
                6.5,
                29.99,
                LocalDate.of(2021, 04, 25),
                LocalDate.of(2023, 04, 25),
                LocalDate.of(2023, 04, 25),
                LocalDate.of(2023, 12, 25));

        inventoryItem5 = new InventoryItem(product5,
                "PROD",
                4,
                6.5,
                21.99,
                LocalDate.of(2020, 05, 25),
                LocalDate.of(2022, 04, 25),
                LocalDate.of(2023, 04, 25),
                LocalDate.of(2023, 12, 25));

        inventoryItems = List.of(inventoryItem1, inventoryItem2, inventoryItem3, inventoryItem4, inventoryItem5);
        for (InventoryItem inventoryItem : inventoryItems) {
            entityManager.persist(inventoryItem);
        }
        entityManager.flush();
    }

    // -------------------------------------- order by product id tests --------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by product ID
     * in ascending order i.e. in alphabetical order, and a secondary sort of best before date from earliest to more recent,
     * and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessId_thenReturnProductIdOrderedProductsAscending() {

        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("productId").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by product ID
     * in descending order i.e. in reverse alphabetical order, and a secondary sort of best before date from earliest
     * to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnProductIdOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("productId").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APP-LE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // -------------------------------------- order by quantity tests ----------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by quantity
     * in ascending order i.e. from lowest to largest and a secondary sort of best before date from earliest
     * to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnQuantityOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("quantity").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Integer> orderedQuantities = new ArrayList<>();
        orderedQuantities.add(4);
        orderedQuantities.add(4);
        orderedQuantities.add(6);
        orderedQuantities.add(13);
        orderedQuantities.add(40);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getQuantity()).isEqualTo(orderedQuantities.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by quantity
     * in descending order i.e. from largest to smallest and a secondary sort of best before date from earliest
     * to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnQuantityOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("quantity").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Integer> orderedQuantities = new ArrayList<>();
        orderedQuantities.add(40);
        orderedQuantities.add(13);
        orderedQuantities.add(6);
        orderedQuantities.add(4);
        orderedQuantities.add(4);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getQuantity()).isEqualTo(orderedQuantities.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by price per item tests ------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by price per item
     * in ascending order i.e. in least expensive to more expensive order and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnPricePerItemOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("pricePerItem").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedPricePerItem = new ArrayList<>();
        orderedPricePerItem.add(4.99);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(17.44);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getPricePerItem()).isEqualTo(orderedPricePerItem.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by price per item
     * in descending order i.e. in most expensive to least expensive order and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnPricePerItemOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("pricePerItem").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedPricePerItem = new ArrayList<>();
        orderedPricePerItem.add(17.44);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(4.99);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getPricePerItem()).isEqualTo(orderedPricePerItem.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order inventory Items(price per item of one
     * inventory item is null) by price per item in descending order i.e. in most expensive to least expensive order
     * (null will be last) and a secondary sort of best before date from earliest to more recent, and a tertiary
     * sort of expiry date from earliest to more recent.
     */
    @Test
    void FindInventoryItemsByBusinessIdTests_WithPricePerItemOfOneInventoryItemIsNull() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        inventoryItem4.setPricePerItem(null);
        Sort sortBy = Sort.by(Sort.Order.desc("pricePerItem").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedPricePerItem = new ArrayList<>();
        orderedPricePerItem.add(17.44);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(6.5);
        orderedPricePerItem.add(4.99);
        orderedPricePerItem.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getPricePerItem()).isEqualTo(orderedPricePerItem.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by total price tests ---------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by total price
     * in ascending order i.e. in least expensive to more expensive order and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnTotalPriceOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("totalPrice").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedTotalPrices = new ArrayList<>();
        orderedTotalPrices.add(21.99);
        orderedTotalPrices.add(21.99);
        orderedTotalPrices.add(27.0);
        orderedTotalPrices.add(29.99);
        orderedTotalPrices.add(32.23);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getTotalPrice()).isEqualTo(orderedTotalPrices.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by total price
     * in descending order i.e. in most expensive to least expensive order and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnTotalPriceOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("totalPrice").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedTotalPrices = new ArrayList<>();
        orderedTotalPrices.add(32.23);
        orderedTotalPrices.add(29.99);
        orderedTotalPrices.add(27.0);
        orderedTotalPrices.add(21.99);
        orderedTotalPrices.add(21.99);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getTotalPrice()).isEqualTo(orderedTotalPrices.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order inventory Items(total price of one
     * inventory item is null) by total price in descending order i.e. in most expensive to least expensive order
     * (null will be last) and a secondary sort of best before date from earliest to more recent, and a tertiary
     * sort of expiry date from earliest to more recent.
     */
    @Test
    void FindInventoryItemsByBusinessIdTests_WithTotalPriceOfOneInventoryItemIsNull() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        inventoryItem2.setTotalPrice(null);
        Sort sortBy = Sort.by(Sort.Order.desc("totalPrice").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedTotalPrices = new ArrayList<>();
        orderedTotalPrices.add(32.23);
        orderedTotalPrices.add(29.99);
        orderedTotalPrices.add(21.99);
        orderedTotalPrices.add(21.99);
        orderedTotalPrices.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getTotalPrice()).isEqualTo(orderedTotalPrices.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by manufactured date tests ---------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the manufactured date
     * in ascending order i.e. in least recent to most recent and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnManufacturedOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("manufactured").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedManufacturedDates = new ArrayList<>();
        orderedManufacturedDates.add(LocalDate.of(2020, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2020, 05, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getManufactured()).isEqualTo(orderedManufacturedDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the manufactured date
     * in descending order i.e. in most recent to least recent and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnRecommendedRetailPriceOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("manufactured").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedManufacturedDates = new ArrayList<>();
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2020, 05, 25));
        orderedManufacturedDates.add(LocalDate.of(2020, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APPLE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getManufactured()).isEqualTo(orderedManufacturedDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order inventory Items(manufactured date of
     * one inventory item is null) by the manufactured date in descending order i.e. in most recent to least recent
     * order (null will be last) and a secondary sort of best before date from earliest to more recent, and a
     * tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void FindInventoryItemsByBusinessIdTests_WithManufacturedOfOneInventoryItemIsNull() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        inventoryItem3.setManufactured(null);
        Sort sortBy = Sort.by(Sort.Order.desc("manufactured").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedManufacturedDates = new ArrayList<>();
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2021, 04, 25));
        orderedManufacturedDates.add(LocalDate.of(2020, 05, 25));
        orderedManufacturedDates.add(LocalDate.of(2020, 04, 25));
        orderedManufacturedDates.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getManufactured()).isEqualTo(orderedManufacturedDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by sell by date tests --------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the sell by date
     * in ascending order i.e. least recent to most recent and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnSellByOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("sellBy").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedSellByDates = new ArrayList<>();
        orderedSellByDates.add(LocalDate.of(2021, 04, 25));
        orderedSellByDates.add(LocalDate.of(2022, 04, 25));
        orderedSellByDates.add(LocalDate.of(2022, 04, 25));
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getSellBy()).isEqualTo(orderedSellByDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the sell by date
     * in descending order i.e. most recent to least recent and a secondary sort of best before date
     * from earliest to more recent, and a tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnSellByOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("sellBy").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedSellByDates = new ArrayList<>();
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        orderedSellByDates.add(LocalDate.of(2022, 04, 25));
        orderedSellByDates.add(LocalDate.of(2022, 04, 25));
        orderedSellByDates.add(LocalDate.of(2021, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APPLE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);


        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getSellBy()).isEqualTo(orderedSellByDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order inventory Items(sell by date of one
     * inventory item is null) by the sell by date in descending order i.e. in most recent to least recent order
     * (null will be last) and a secondary sort of best before date from earliest to more recent, and a
     * tertiary sort of expiry date from earliest to more recent.
     */
    @Test
    void FindInventoryItemsByBusinessIdTests_WithSellByOfOneInventoryItemIsNull() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        inventoryItem2.setSellBy(null);
        Sort sortBy = Sort.by(Sort.Order.desc("sellBy").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedSellByDates = new ArrayList<>();
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        orderedSellByDates.add(LocalDate.of(2023, 04, 25));
        orderedSellByDates.add(LocalDate.of(2022, 04, 25));
        orderedSellByDates.add(LocalDate.of(2021, 04, 25));
        orderedSellByDates.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);


        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getSellBy()).isEqualTo(orderedSellByDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by best before date tests ----------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the best before date
     * in ascending order i.e. least recent to most recent and a secondary sort of expiry date from earliest to more
     * recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnBestBeforeOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("bestBefore").ignoreCase())
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedBestBeforeDates = new ArrayList<>();
        orderedBestBeforeDates.add(LocalDate.of(2021, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2022, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getBestBefore()).isEqualTo(orderedBestBeforeDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the best before date
     * in descending order i.e. most recent to least recent and a secondary sort of expiry date from earliest to
     * more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnBestBeforeOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("bestBefore").ignoreCase())
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedBestBeforeDates = new ArrayList<>();
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2022, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2021, 04, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getBestBefore()).isEqualTo(orderedBestBeforeDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order inventory Items(best before date of
     * one inventory item is null) by the best before date in descending order i.e. most recent to least recent
     * order (null will be last) and a secondary sort of expiry date from earliest to more recent.
     */
    @Test
    void FindInventoryItemsByBusinessIdTests_WithBestBeforeOfOneInventoryItemIsNull() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        inventoryItem3.setBestBefore(null);
        Sort sortBy = Sort.by(Sort.Order.desc("bestBefore").ignoreCase())
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedBestBeforeDates = new ArrayList<>();
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2023, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2022, 04, 25));
        orderedBestBeforeDates.add(LocalDate.of(2021, 04, 25));
        orderedBestBeforeDates.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getBestBefore()).isEqualTo(orderedBestBeforeDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ------------------------------------ order by expiry date tests ---------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the expiry date
     * in ascending order i.e. least recent to most recent and a secondary sort of expiry date from earliest to more
     * recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnExpiresOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("expires").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedExpiresDates = new ArrayList<>();
        orderedExpiresDates.add(LocalDate.of(2021, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2022, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2023, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2023, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2024, 12, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getExpires()).isEqualTo(orderedExpiresDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will order products by the expiry date
     * in descending order i.e. most recent to least recent and a secondary sort of expiry date from earliest to
     * more recent.
     */
    @Test
    void whenFindInventoryItemsByBusinessIdTests_thenReturnExpiresOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("expires").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("productId")));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDate> orderedExpiresDates = new ArrayList<>();
        orderedExpiresDates.add(LocalDate.of(2024, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2023, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2023, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2022, 12, 25));
        orderedExpiresDates.add(LocalDate.of(2021, 12, 25));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < inventoryPage.getContent().size(); i++) {
            assertThat(inventoryPage.getContent().get(i).getExpires()).isEqualTo(orderedExpiresDates.get(i));
            assertThat(inventoryPage.getContent().get(i).getProduct().getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    // ----------------------------------------- pagination tests --------------------------------------------------

    /**
     * Tests that the findInventoryItemsByBusinessId functionality will return paginated results correctly
     * when the page is not full with inventory items.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnPageHalfFull() {
        // given
        int pageNo = 0;
        int pageSize = 10;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        assertThat(inventoryPage.getContent()).isEqualTo(inventoryItems);

        assertThat(inventoryPage.getTotalElements()).isEqualTo(5);
        for (int i = 0; i < inventoryItems.size(); i++) {
            assertThat(inventoryPage.getContent().get(i)).isEqualTo(inventoryItems.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will return an empty page when given a
     * business ID that does not match anything in the database.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnEmptyPage() {
        // given
        int pageNo = 0;
        int pageSize = 20;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(1, pageable);

        // then
        assertThat(inventoryPage.getTotalElements()).isZero();
        assertThat(inventoryPage.getTotalPages()).isZero();
    }

    /**
     * Tests that the findProductsByBusinessId functionality will return pages other
     * than the first one with correct products.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnPagesFromTwoOnward() {
        // given
        int pageSize = 1;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        // when
        Page<InventoryItem> inventoryPage2 = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, PageRequest.of(1, pageSize, sortBy));
        Page<InventoryItem> inventoryPage3 = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, PageRequest.of(2, pageSize, sortBy));
        Page<InventoryItem> inventoryPage4 = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, PageRequest.of(3, pageSize, sortBy));
        Page<InventoryItem> inventoryPage5 = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, PageRequest.of(4, pageSize, sortBy));

        // then
        assertThat(inventoryPage2.getTotalPages()).isEqualTo(5);
        assertThat(inventoryPage2.getContent().get(0)).isEqualTo(inventoryItems.get(1));
        assertThat(inventoryPage3.getContent().get(0)).isEqualTo(inventoryItems.get(2));
        assertThat(inventoryPage4.getContent().get(0)).isEqualTo(inventoryItems.get(3));
        assertThat(inventoryPage5.getContent().get(0)).isEqualTo(inventoryItems.get(4));
    }

    /**
     * Tests that the findProductsByBusinessId functionality will return the page correctly when the
     * page is full.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnFullPage() {
        // given
        int pageNo = 0;
        int pageSize = 4;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);


        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        assertThat(inventoryPage.getTotalPages()).isEqualTo(2);
        assertThat(inventoryPage.getSize()).isEqualTo(4);
        for (int i = 0; i < inventoryPage.getSize(); i++) {
            assertThat(inventoryPage.getContent().get(i)).isEqualTo(inventoryItems.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality ordering works across pages,
     * not just within a single page.
     * i.e. That data is ordered 'globally' from all results in the database,
     * not just the few values that are returned are correctly ordered.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnGloballyOrderedProducts() {
        // given
        int pageNo = 1;
        int pageSize = 2;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase())
                .and(Sort.by(Sort.Order.asc("bestBefore").ignoreCase()))
                .and(Sort.by(Sort.Order.asc("expires").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<InventoryItem> inventoryPage = inventoryItemRepository.findInventoryItemsByBusinessId(businessId, pageable);

        // then
        assertThat(inventoryPage.getTotalPages()).isEqualTo(3);
        assertThat(inventoryPage.getSize()).isEqualTo(2);
        assertThat(inventoryPage.getContent().get(0).getProductId()).isEqualTo("APP-LE3");
        assertThat(inventoryPage.getContent().get(1).getProductId()).isEqualTo("DUCT");
    }
}
