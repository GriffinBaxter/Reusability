package org.seng302.business;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

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
    public void whenFindExistingProductsByExistingIdThenReturnProductList() throws Exception {
        // given
        Business business = new Business("example name",
                "some text",
                "11 example rd",
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now()
        );
        entityManager.persist(business);
        entityManager.flush();

        Product product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
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
    public void whenFindNonExistingProductsByExistingIdThenReturnEmptyProductList() throws Exception {
        // given
        Business business = new Business("example name",
                "some text",
                "11 example rd",
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now()
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
    public void whenFindProductsByNonExistingIdThenDontReturnProductPayload() {
        // when
        foundProductPayloadList = productRepository.findProductsByBusinessId(1);

        // then
        assertThat(foundProductPayloadList.isEmpty()).isTrue();
    }
}
