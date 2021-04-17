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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
public class ProductRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private List<ProductPayload> foundProductPayloadList;

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
                "90210"
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
        foundProductPayloadList = productRepository.findProductsByBusinessId(business.getId());

        // then
        assertThat(foundProductPayloadList.isEmpty()).isFalse();
        assertThat(foundProductPayloadList.get(0).getId()).isEqualTo("PROD");
        assertThat(foundProductPayloadList.get(0).getName()).isEqualTo("Beans");
        assertThat(foundProductPayloadList.get(0).getDescription()).isEqualTo("Description");
        assertThat(foundProductPayloadList.get(0).getManufacturer()).isEqualTo("Manufacturer");
        assertThat(foundProductPayloadList.get(0).getRecommendedRetailPrice()).isEqualTo(20.00);
        assertThat(foundProductPayloadList.get(0).getCreated()).isEqualTo(LocalDateTime.of(
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
                "90210"
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
        foundProductPayloadList = productRepository.findProductsByBusinessId(business.getId());

        // then
        assertThat(foundProductPayloadList.isEmpty()).isTrue();
    }

    /**
     * Tests that when trying to retrieve products with a non-existing business ID then
     * the list of product payloads returned is empty.
     */
    @Test
    public void whenFindProductsByNonExistingBusinessIdThenDontReturnProductPayload() {
        // when
        foundProductPayloadList = productRepository.findProductsByBusinessId(1);

        // then
        assertThat(foundProductPayloadList.isEmpty()).isTrue();
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
                "90210"
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
                "90210"
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
                "90210"
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
}
