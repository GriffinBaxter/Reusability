package org.seng302.business.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;
import org.seng302.model.repository.ProductRepository;
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
 * ProductRepositoryCustomImpl test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class CustomProductRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

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
                user
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
                user
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
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFields ProductId --------------------------

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by productId field when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_productId_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Be");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");

        List<String> fields = new ArrayList<>();
        fields.add("id");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by productId field when multiple queries exist
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_productId_MultipleQueries_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Be");
        strings.add("pro");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");
        expectedIds.add("PROD");

        List<String> fields = new ArrayList<>();
        fields.add("id");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isEqualTo(2);
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by productId field when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_productId_ProductsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");

        List<String> fields = new ArrayList<>();
        fields.add("id");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFields Name --------------------------

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by name field when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_name_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Product");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("PROD");

        List<String> fields = new ArrayList<>();
        fields.add("name");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by name field when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_name_ProductsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");

        List<String> fields = new ArrayList<>();
        fields.add("name");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFields Manufacturer --------------------------

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by manufacturer field when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_manufacturer_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("New");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("DUCT");
        expectedIds.add("PROD");

        List<String> fields = new ArrayList<>();
        fields.add("manufacturer");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by manufacturer field when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_manufacturer_ProductsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");

        List<String> fields = new ArrayList<>();
        fields.add("manufacturer");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFields Description --------------------------

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by description field when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_description_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Brand new");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("DUCT");

        List<String> fields = new ArrayList<>();
        fields.add("description");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by description field when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_description_ProductsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");

        List<String> fields = new ArrayList<>();
        fields.add("description");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }

    // -------------------------- findAllProductsByBusinessIdAndIncludedFields Multiple --------------------------

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by productID field when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_nameOrProductId_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Pro");
        strings.add("beans");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");
        expectedIds.add("PROD");

        List<String> fields = new ArrayList<>();
        fields.add("name");
        fields.add("id");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields by manufacturer and description fields when query exists
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_manufacturerDescription_ProductsExist() {
        List<String> strings = new ArrayList<>();
        strings.add("New");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("DUCT");
        expectedIds.add("PROD");

        List<String> fields = new ArrayList<>();
        fields.add("manufacturer");
        fields.add("description");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i).getProductId()).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findAllProductsByBusinessIdAndIncludedFields for product with name Product by all other fields
     * Returns empty list
     */
    @Test
    void whenFindAllProductsByBusinessIdAndIncludedFields_NotName_ProductsDoNotExist() {
        List<String> strings = new ArrayList<>();
        strings.add("Product");

        List<String> fields = new ArrayList<>();
        fields.add("manufacturer");
        fields.add("description");
        fields.add("id");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsByBusinessIdAndIncludedFields(strings, fields , business.getId(), pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }
}