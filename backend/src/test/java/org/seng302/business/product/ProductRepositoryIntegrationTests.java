package org.seng302.business.product;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.address.Address;
import org.seng302.business.Business;
import org.seng302.business.BusinessType;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * ProductRepository test class
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class ProductRepositoryIntegrationTests {

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
    public void whenFindExistingProductsByExistingBusinessIdThenReturnProductList() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                                LocalTime.of(0, 0))
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
        assertThat(foundProductList.get().findFirst().get().getCreated()).isEqualTo(LocalDateTime.of(
                    LocalDate.of(2021, 1, 1),
                    LocalTime.of(0, 0)).toString());
    }

    /**
     * Tests that when there are no products in the database with the given business ID then
     * the list of product payloads returned is empty.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenFindNonExistingProductsByExistingBusinessIdThenReturnEmptyProductList() throws Exception {
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
                user
        );
        entityManager.persist(business);
        entityManager.flush();

        // when
        foundProductList = productRepository.findProductsByBusinessId(business.getId(), null);

        // then
        assertThat(foundProductList.get().findAny().isEmpty()).isTrue();
    }

    /**
     * Tests that when trying to retrieve products with a non-existing business ID then
     * the list of product payloads returned is empty.
     */
    @Test
    public void whenFindProductsByNonExistingBusinessIdThenDontReturnProductPayload() {
        // when
        foundProductList = productRepository.findProductsByBusinessId(1, null);

        // then
        assertThat(foundProductList.get().findAny().isEmpty()).isTrue();
    }

    /**
     * Tests that when there is a product with the given product ID and business ID in the database
     * then the product is returned.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenFindExistingProductByExistingIdAndExistingBusinessIdThenReturnProduct() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId(product.getProductId(), business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isFalse();
        assertThat(foundProduct.get().getProductId()).isEqualTo("PROD");
        assertThat(foundProduct.get().getName()).isEqualTo("Beans");
        assertThat(foundProduct.get().getDescription()).isEqualTo("Description");
        assertThat(foundProduct.get().getManufacturer()).isEqualTo("Manufacturer");
        assertThat(foundProduct.get().getRecommendedRetailPrice()).isEqualTo(20.00);
        assertThat(foundProduct.get().getCreated()).isEqualTo(LocalDateTime.of(
                    LocalDate.of(2021, 1, 1),
                    LocalTime.of(0, 0)).toString());
    }

    /**
     * Tests that when there is a product with the given product ID in the database but
     * the business ID provided doesn't match, then the product isn't returned.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenFindExistingProductByExistingIdAndNonExistingBusinessIdThenDontReturnProduct() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId(product.getProductId(), 0);

        // then
        assertThat(foundProduct.isEmpty()).isTrue();
    }

    /**
     * Tests that when there is a product with the given business ID in the database but
     * the product ID provided doesn't match, then the product isn't returned.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenFindExistingProductByNonExistingIdAndExistingBusinessIdThenDontReturnProduct() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PRO", business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isTrue();
    }


    /**
     * Tests that when we delete a product by its product ID and business ID the product is deleted from
     * the database.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenDeletingProductWithValidProductAndBusinessIDs() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD", business.getId());
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isTrue();
    }


    /**
     * Tests that when trying to delete a invalid product id with a valid id. No errors are thrown, and other products are not effected.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenDeletingProductWithInvalidProductIdAndValidBusinessId() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD1", business.getId());
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isFalse();
    }


    /**
     * Tests when deleting a product with a invalid product id and invalid business id, no errors thrown and other products are not effected.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenDeletingProductWithValidProductIdAndInvalidBusinessId() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD", business.getId()+1);
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isFalse();
    }


    /**
     * Tests that when we delete a product by its invalid product ID and invalid business ID. No errors are thrown and other products are no effected.
     *
     * @throws Exception Exception error
     */
    @Test
    public void whenDeletingProductWithInvalidProductIdAndInvalidBusinessId() throws Exception {
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
                user
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
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0))
        );
        entityManager.persist(product);
        entityManager.flush();

        // when
        productRepository.deleteByIdAndBusinessId("PROD1", business.getId()+1);
        Optional<Product> foundProduct = productRepository.findProductByIdAndBusinessId("PROD", business.getId());

        // then
        assertThat(foundProduct.isEmpty()).isFalse();
    }


}
