package org.seng302.business.product;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * ProductRepository test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ProductRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Page<Product> foundProductList;

    /**
     * Tests that when there are products in the database with the given business ID then
     * the list of product payloads returned contains the products with that business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenFindExistingProductsByExistingBusinessIdThenReturnProductList() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        foundProductList = productRepository.findProductsByBusinessId(business.getId(), null);

        // then
        assertThat(foundProductList.isEmpty()).isFalse();
        assertThat(foundProductList.get().findFirst().get().getProductId()).isEqualTo("PROD");
        assertThat(foundProductList.get().findFirst().get().getName()).isEqualTo("Beans");
        assertThat(foundProductList.get().findFirst().get().getDescription()).isEqualTo("Description");
        assertThat(foundProductList.get().findFirst().get().getManufacturer()).isEqualTo("Manufacturer");
        assertThat(foundProductList.get().findFirst().get().getRecommendedRetailPrice()).isEqualTo(20.00);
        assertThat(foundProductList.get().findFirst().get().getCreated()).isNotNull();
        assertThat(foundProductList.get().findFirst().get().getBarcode()).isNull();
    }

    /**
     * Tests that when there are no products in the database with the given business ID then
     * the list of product payloads returned is empty.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenFindNonExistingProductsByExistingBusinessIdThenReturnEmptyProductList() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        // when
        foundProductList = productRepository.findProductsByBusinessId(business.getId(), null);

        // then
        assertThat(foundProductList.get().findAny()).isEmpty();
    }

    /**
     * Tests that when trying to retrieve products with a non-existing business ID then
     * the list of product payloads returned is empty.
     */
    @Test
    void whenFindProductsByNonExistingBusinessIdThenDontReturnProductPayload() {
        // when
        foundProductList = productRepository.findProductsByBusinessId(1, null);

        // then
        assertThat(foundProductList.get().findAny()).isEmpty();
    }

    /**
     * Tests that when there is a product with the given product ID and business ID in the database
     * then the product is returned.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenFindExistingProductByExistingIdAndExistingBusinessIdThenReturnProduct() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getProductId()).isEqualTo("PROD");
        assertThat(foundProduct.get().getName()).isEqualTo("Beans");
        assertThat(foundProduct.get().getDescription()).isEqualTo("Description");
        assertThat(foundProduct.get().getManufacturer()).isEqualTo("Manufacturer");
        assertThat(foundProduct.get().getRecommendedRetailPrice()).isEqualTo(20.00);
        assertThat(foundProduct.get().getCreated()).isNotNull();
        assertThat(foundProduct.get().getBarcode()).isNull();
    }

    /**
     * Tests that when there is a product with the given product ID in the database but
     * the business ID provided doesn't match, then the product isn't returned.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenFindExistingProductByExistingIdAndNonExistingBusinessIdThenDontReturnProduct() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId(product.getProductId(), 0);

        // then
        assertThat(foundProduct).isEmpty();
    }

    /**
     * Tests that when there is a product with the given business ID in the database but
     * the product ID provided doesn't match, then the product isn't returned.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenFindExistingProductByNonExistingIdAndExistingBusinessIdThenDontReturnProduct() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PRO", business.getId());

        // then
        assertThat(foundProduct).isEmpty();
    }


    /**
     * Tests that when we delete a product by its product ID and business ID the product is deleted from
     * the database.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenDeletingProductWithValidProductAndBusinessIDs() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD", business.getId());
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct).isEmpty();
    }


    /**
     * Tests that when trying to delete a invalid product id with a valid id. No errors are thrown, and other products are not effected.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenDeletingProductWithInvalidProductIdAndValidBusinessId() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD1", business.getId());
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct).isPresent();
    }


    /**
     * Tests when deleting a product with a invalid product id and invalid business id, no errors thrown and other products are not effected.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenDeletingProductWithValidProductIdAndInvalidBusinessId() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD", business.getId()+1);
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct).isPresent();
    }


    /**
     * Tests that when we delete a product by its invalid product ID and invalid business ID. No errors are thrown and other products are no effected.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenDeletingProductWithInvalidProductIdAndInvalidBusinessId() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD1", business.getId()+1);
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct).isPresent();
    }

    /**
     * Tests that we retrieve all products without pagination with a valid business ID.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenRetrievingAllProductsWithValidBusinessID() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        Product product2 = new Product(
                "APPL",
                business,
                "Apple",
                "Description",
                "Manufacturer",
                30.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        List<Product> foundProducts = productRepository.findAllByBusinessId(business.getId());

        // then
        assertThat(foundProducts.isEmpty()).isFalse();
    }

    /**
     * Tests that when we retrieve all products without pagination with an invalid business ID that no errors are thrown.
     *
     * @throws Exception Exception error
     */
    @Test
    void whenRetrievingAllProductsWithInValidBusinessID() throws Exception {
        // given
        Address address = new Address(
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
        User user = new User(
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
        entityManager.persist(user);
        entityManager.flush();
        Business business = new Business(
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
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        Product product2 = new Product(
                "APPL",
                business,
                "Apple",
                "Description",
                "Manufacturer",
                30.00,
                ""
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        List<Product> foundProducts = productRepository.findAllByBusinessId(20000);

        // then
        assertThat(foundProducts.isEmpty()).isTrue();
    }


}
