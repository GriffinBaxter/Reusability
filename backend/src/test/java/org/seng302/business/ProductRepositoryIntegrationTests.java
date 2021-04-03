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
 * ProductRepository test class.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
public class ProductRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProductRepository productRepository;

    private List<ProductPayload> foundProductPayloadList;


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
                1,
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
        foundProductPayloadList = productRepository.findProductsByBusinessId(1);

        // then
        assertThat(foundProductPayloadList.isEmpty()).isFalse();
        assertThat(foundProductPayloadList.get(0).getProductCode()).isEqualTo("PROD");
        assertThat(foundProductPayloadList.get(0).getName()).isEqualTo("Beans");
        assertThat(foundProductPayloadList.get(0).getDescription()).isEqualTo("Description");
        assertThat(foundProductPayloadList.get(0).getManufacturer()).isEqualTo("Manufacturer");
        assertThat(foundProductPayloadList.get(0).getRecommendedRetailPrice()).isEqualTo(20.00);
        assertThat(foundProductPayloadList.get(0).getCreated()).isEqualTo(LocalDateTime.of(
                                                                        LocalDate.of(2021, 1, 1),
                                                                        LocalTime.of(0, 0)).toString());
    }

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

        foundProductPayloadList = productRepository.findProductsByBusinessId(1);

        assertThat(foundProductPayloadList.isEmpty()).isTrue();
    }

    @Test
    public void whenFindProductsByNonExistingIdThenDontReturnProductPayload() {
        foundProductPayloadList = productRepository.findProductsByBusinessId(1);

        assertThat(foundProductPayloadList.isEmpty()).isTrue();
    }
}
