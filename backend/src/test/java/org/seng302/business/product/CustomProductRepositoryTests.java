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

    // -------------------------- findAllProductsBySingleField ProductId --------------------------

    /**
     * Tests findProductsBySingleField by productId field when query exists and no optional filters are used
     * Returns list of Products
     */
    @Test
    void whenFindAllProductsBySingleField_productId_ProductsExist() throws Exception {
        List<String> strings = new ArrayList<>();
        strings.add("Be");

        List<String> expectedIds = new ArrayList<>();
        expectedIds.add("BEAN");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsBySingleField(strings, 1, "productId", pageable);

        // then
        assertThat(productsPage.getContent().size()).isNotZero();
        for (int i = 0; i < productsPage.getContent().size(); i++){
            assertThat(productsPage.getContent().get(i)).isEqualTo(expectedIds.get(i));
        }
    }

    /**
     * Tests findProductsBySingleField by productId field when query doesn't exist
     * Returns empty list
     */
    @Test
    void whenFindProductsBySingleField_productId_ProductsDoNotExist() throws Exception {
        List<String> strings = new ArrayList<>();
        strings.add("ABCD");

        int pageNo = 0;
        int pageSize = 5;

        Sort sortBy = Sort.by(Sort.Order.asc("id"));

        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // when
        Page<Product> productsPage = productRepository.findAllProductsBySingleField(strings, 1, "productId", pageable);

        // then
        assertThat(productsPage.getContent().size()).isZero();
    }
}