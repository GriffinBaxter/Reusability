package org.seng302.business;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
public class BusinessRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private  BusinessRepository businessRepository;

    private Optional<Business> found;

    /**
     * Tests that a business is returned when calling findBusinessById() with an existing id
     */
    @Test
    public void whenFindByExistingId_thenReturnABusiness() throws Exception {
        // given
        Business business = new Business("example name",
                                        "some text",
                                        "11 example rd",
                                        BusinessType.RETAIL_TRADE,
                                        LocalDateTime.now());

        entityManager.persist(business);
        entityManager.flush();

        found = businessRepository.findBusinessById(business.getId());

        // then
        assertThat(found.isPresent()).isTrue();

        assertThat(business.getId()).isEqualTo(found.get().getId());
        assertThat(business.getName()).isEqualTo(found.get().getName());
        assertThat(business.getDescription()).isEqualTo(found.get().getDescription());
        assertThat(business.getAddress()).isEqualTo(found.get().getAddress());
        assertThat(business.getBusinessType()).isEqualTo(found.get().getBusinessType());

    }

    /**
     * Tests that no user is returned when calling findByEmail() with a non-existing email
     */
    @Test
    public void whenFindByNonExistingId_thenDontReturnBusiness() throws Exception {
        // given
        Business business = new Business("example name",
                "some text",
                "11 example rd",
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now());

        entityManager.persist(business);
        entityManager.flush();

        // when
        found = businessRepository.findBusinessById(Integer.valueOf("0"));

        // then
        assertThat(found.isEmpty()).isTrue();
    }



}
