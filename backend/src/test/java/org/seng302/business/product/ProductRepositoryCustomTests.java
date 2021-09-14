package org.seng302.business.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.Main;
import org.seng302.model.Product;
import org.seng302.model.repository.ProductRepository;
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
 * ProductRepositoryCustomTests test class - specifically for testing the pagination and ordering of the
 * findAllProductsByBusinessIdAndIncludedFields and method in findAllProductsByBusinessIdAndIncludedFieldsAndBarcode.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ProductRepositoryCustomTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Address address;

    private User user;

    private Business business;
    private Business business2;
    private Integer businessId;

    private Product product1;
    private Product product2;
    private Product product3;
    private Product product4;
    private Product product5;
    private Product product6;

    private List<Product> searchProducts;

    /**
     * Creates and inserts all entities needed for testing.
     *  Any exception.
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
                user,
                "$",
                "NZD"
        );
        business = entityManager.persist(business);
        businessId = business.getId();
        entityManager.flush();

        business2 = new Business(
                user.getId(),
                "Business name",
                "Desc.",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user,
                "$",
                "NZD"
        );
        business2 = entityManager.persist(business2);
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
        product1.setCreated((LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0))));

        product2 = new Product(
                "APP-LE",
                business,
                "Apples",
                "Description",
                "A Manufacturer",
                20.00,
                "9415767624207"
        );
        product2.setCreated((LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0))));

        product3 = new Product(
                "APP-LE3",
                business,
                "Gala Apples",
                "Description",
                "A Manufacturer",
                null,
                "9310140001531"
        );
        product3.setCreated((LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0))));

        product4 = new Product(
                "DUCT",
                business,
                "Duct-Tape",
                "Brand new Description",
                "A New Manufacturer",
                10.00,
                "978020137962"
        );
        product4.setCreated(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(0, 0)));

        product5 = new Product(
                "PROD",
                business,
                "Product",
                "New Description",
                "",
                10.00,
                ""
        );
        product5.setCreated(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(1, 0)));


        product6 = new Product(
                "BAR",
                business2,
                "Bark",
                "Desc",
                "J&J",
                2.1,
                "9415767624207"
        );
        product6.setCreated(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(1, 0)));

        searchProducts = List.of(product1, product2, product3, product4, product5);
        for (Product product: searchProducts) {
            entityManager.persist(product);
        }

        entityManager.flush();
    }

    // --------------------------------- findAllProductsByBusinessIdAndIncludedFields ----------------------------------

    /**
     * Tests that the findProductsByBusinessId functionality will order products by product ID
     * in ascending order i.e. in alphabetical order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnProductIdOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by product ID
     * in descending order i.e. in reverse alphabetical order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnProductIdOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APP-LE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by name
     * in ascending order i.e. in alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnNameOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNames = new ArrayList<>();
        orderedNames.add("Apple");
        orderedNames.add("Apples");
        orderedNames.add("Duct-Tape");
        orderedNames.add("Gala Apples");
        orderedNames.add("Product");
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("PROD");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by name
     * in descending order i.e. in reverse alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnNameOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("name").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedNames = new ArrayList<>();
        orderedNames.add("Product");
        orderedNames.add("Gala Apples");
        orderedNames.add("Duct-Tape");
        orderedNames.add("Apples");
        orderedNames.add("Apple");
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getName()).isEqualTo(orderedNames.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by description
     * in ascending order i.e. in alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnDescriptionOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("description").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedDescriptions = new ArrayList<>();
        orderedDescriptions.add("A Description");
        orderedDescriptions.add("Brand new Description");
        orderedDescriptions.add("Description");
        orderedDescriptions.add("Description");
        orderedDescriptions.add("New Description");
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("PROD");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getDescription()).isEqualTo(orderedDescriptions.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by description
     * in descending order i.e. in reverse alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnDescriptionOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("description").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedDescriptions = new ArrayList<>();
        orderedDescriptions.add("New Description");
        orderedDescriptions.add("Description");
        orderedDescriptions.add("Description");
        orderedDescriptions.add("Brand new Description");
        orderedDescriptions.add("A Description");
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APPLE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getDescription()).isEqualTo(orderedDescriptions.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by manufacturer
     * in ascending order i.e. in alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnManufacturerOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("manufacturer").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedManufacturers = new ArrayList<>();
        orderedManufacturers.add(null);
        orderedManufacturers.add("A Manufacturer");
        orderedManufacturers.add("A Manufacturer");
        orderedManufacturers.add("A New Manufacturer");
        orderedManufacturers.add("Manufacturer");
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APPLE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getManufacturer()).isEqualTo(orderedManufacturers.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by manufacturer
     * in descending order i.e. in reverse alphabetical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnManufacturerOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("manufacturer").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<String> orderedManufacturers = new ArrayList<>();
        orderedManufacturers.add("Manufacturer");
        orderedManufacturers.add("A New Manufacturer");
        orderedManufacturers.add("A Manufacturer");
        orderedManufacturers.add("A Manufacturer");
        orderedManufacturers.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("PROD");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getManufacturer()).isEqualTo(orderedManufacturers.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by RRP
     * in ascending order i.e. in numerical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnRecommendedRetailPriceOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("recommendedRetailPrice").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedRecommendedRetailPrices = new ArrayList<>();
        orderedRecommendedRetailPrices.add(null);
        orderedRecommendedRetailPrices.add(10.00);
        orderedRecommendedRetailPrices.add(10.00);
        orderedRecommendedRetailPrices.add(20.00);
        orderedRecommendedRetailPrices.add(21.00);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APPLE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getRecommendedRetailPrice()).isEqualTo(orderedRecommendedRetailPrices.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by RRP
     * in descending order i.e. in reverse numerical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnRecommendedRetailPriceOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("recommendedRetailPrice").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<Double> orderedRecommendedRetailPrices = new ArrayList<>();
        orderedRecommendedRetailPrices.add(21.00);
        orderedRecommendedRetailPrices.add(20.00);
        orderedRecommendedRetailPrices.add(10.00);
        orderedRecommendedRetailPrices.add(10.00);
        orderedRecommendedRetailPrices.add(null);
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");
        orderedProductIds.add("APP-LE3");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getRecommendedRetailPrice()).isEqualTo(orderedRecommendedRetailPrices.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by created date and time
     * in ascending order i.e. in numerical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnCreatedOrderedProductsAscending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDateTime> orderedCreated = new ArrayList<>();
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(1, 0)));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("APP-LE");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("PROD");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getCreated()).isEqualTo(orderedCreated.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will order products by created date and time
     * in descending order i.e. in reverse numerical order and a secondary sort of product ID in ascending order.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnCreatedOrderedProductsDescending() {
        // given
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.desc("created").ignoreCase()).and(Sort.by(Sort.Order.asc("id").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);
        ArrayList<LocalDateTime> orderedCreated = new ArrayList<>();
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(1, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 2, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2021, 1, 1), LocalTime.of(0, 0)));
        orderedCreated.add(LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.of(0, 0)));
        ArrayList<String> orderedProductIds = new ArrayList<>();
        orderedProductIds.add("PROD");
        orderedProductIds.add("DUCT");
        orderedProductIds.add("APP-LE3");
        orderedProductIds.add("APPLE");
        orderedProductIds.add("APP-LE");

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(i).getCreated()).isEqualTo(orderedCreated.get(i));
            assertThat(productPage.getContent().get(i).getProductId()).isEqualTo(orderedProductIds.get(i));
        }
    }

    /**
     * Tests that the findProductsByBusinessId functionality will return paginated results correctly
     * when the page is not full with products.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnPageHalfFull() {
        // given
        int pageNo = 0;
        // Page size 10 means page will be half full with the default 5 products inserted
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        assertThat(productPage.getTotalElements()).isEqualTo(5);
        for (int i = 0; i < searchProducts.size(); i++) {
            assertThat(productPage.getContent().get(i)).isEqualTo(searchProducts.get(i));
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
        Page<Product> productPage = productRepository.findProductsByBusinessId(2, pageable);

        // then
        assertThat(productPage.getTotalElements()).isZero();
        assertThat(productPage.getTotalPages()).isZero();
    }

    /**
     * Tests that the findProductsByBusinessId functionality will return pages other
     * than the first one with correct products.
     */
    @Test
    void whenFindAllProductsByBusinessId_thenReturnPagesFromTwoOnward() {
        // given
        int pageSize = 1;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());

        // when
        Page<Product> productPage2 = productRepository.findProductsByBusinessId(businessId, PageRequest.of(1, pageSize, sortBy));
        Page<Product> productPage3 = productRepository.findProductsByBusinessId(businessId, PageRequest.of(2, pageSize, sortBy));
        Page<Product> productPage4 = productRepository.findProductsByBusinessId(businessId, PageRequest.of(3, pageSize, sortBy));
        Page<Product> productPage5 = productRepository.findProductsByBusinessId(businessId, PageRequest.of(4, pageSize, sortBy));

        // then
        assertThat(productPage2.getTotalPages()).isEqualTo(5);
        assertThat(productPage2.getContent().get(0)).isEqualTo(searchProducts.get(2));
        assertThat(productPage3.getContent().get(0)).isEqualTo(searchProducts.get(0));
        assertThat(productPage4.getContent().get(0)).isEqualTo(searchProducts.get(3));
        assertThat(productPage5.getContent().get(0)).isEqualTo(searchProducts.get(4));
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
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        assertThat(productPage.getTotalPages()).isEqualTo(2);
        assertThat(productPage.getSize()).isEqualTo(4);
        for (int i = 0; i < productPage.getSize(); i++) {
            assertThat(productPage.getContent().get(i)).isEqualTo(searchProducts.get(i));
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
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productPage = productRepository.findProductsByBusinessId(businessId, pageable);

        // then
        assertThat(productPage.getTotalPages()).isEqualTo(3);
        assertThat(productPage.getSize()).isEqualTo(2);
        assertThat(productPage.getContent().get(0).getProductId()).isEqualTo("APPLE");
        assertThat(productPage.getContent().get(1).getProductId()).isEqualTo("DUCT");
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFieldsAndBarcode -------------------------------

    /**
     * Tests the findAllProductsByBusinessIdAndIncludedFieldsAndBarcode method returns the correct Product
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFieldsAndBarcode_withValidBarcode_ReturnProduct() {
        // given
        List<String> search = List.of("Apple");
        List<String> fields = List.of("name");
        String barcode = "9300675024235";
        int pageNo = 0;
        int pageSize = 2;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productPage = productRepository.findAllProductsByBusinessIdAndIncludedFieldsAndBarcode(search, fields, business.getId(), pageable, barcode);

        // then
        assertThat(productPage.getContent().size()).isEqualTo(1);
        assertThat(productPage.getContent().get(0).getBarcode()).isEqualTo("9300675024235");
    }

    /**
     * Tests the findAllProductsByBusinessIdAndIncludedFieldsAndBarcode method returns an empty list if barcode doesn't
     * match the barcode of a product
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFieldsAndBarcode_withInvalidBarcode_ReturnNoProduct() {
        // given
        List<String> search = List.of("Apple");
        List<String> fields = List.of("name");
        String barcode = "111111111111111";
        int pageNo = 1;
        int pageSize = 2;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productPage = productRepository.findAllProductsByBusinessIdAndIncludedFieldsAndBarcode(search, fields, business.getId(), pageable, barcode);

        // then
        assertThat(productPage.getContent().size()).isZero();
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFieldsAndBarcode method only returns values for the current business and barcode
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFieldsAndBarcode_withValidBarcodeForTwoBusinesses_ReturnBusinessProduct() {
        // given
        List<String> search = List.of("Apple");
        List<String> fields = List.of("name");
        String barcode = "9415767624207";
        int pageNo = 0;
        int pageSize = 5;
        Sort sortBy = Sort.by(Sort.Order.asc("id").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productPage = productRepository.findAllProductsByBusinessIdAndIncludedFieldsAndBarcode(search, fields, business.getId(), pageable, barcode);

        // then
        assertThat(productPage.getContent().size()).isNotZero();
        for (int i = 0; i < productPage.getContent().size(); i++) {
            assertThat(productPage.getContent().get(0).getBarcode()).isEqualTo("9415767624207");
        }
    }

}
