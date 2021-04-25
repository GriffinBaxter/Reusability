package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.address.Address;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class BusinessRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private  BusinessRepository businessRepository;

    private Optional<Business> found;

    private List<Business> listFound;

    private static Address address;

    private static User user;

    @BeforeAll
    public static void before() throws Exception {
        user = new User("testfirst",
                "testlast",
                "testmiddle",
                "testnick",
                "testbiography",
                "testemail@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);
    }

    /**
     * Tests that a business is returned when calling findBusinessById() with an existing id
     */
    @Test
    public void whenFindByExistingId_thenReturnABusiness() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        entityManager.persist(address);
        entityManager.flush();

        Business business = new Business
                (user.getId(),
                "example name",
                "some text",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.now(),
                user
        );

        entityManager.persist(business);
        entityManager.flush();

        found = businessRepository.findBusinessById(business.getId());

        // then
        assertThat(found.isPresent()).isTrue();

        assertThat(business.getId()).isEqualTo(found.get().getId());
        assertThat(business.getName()).isEqualTo(found.get().getName());
        assertThat(business.getDescription()).isEqualTo(found.get().getDescription());
        assertThat(business.getAddress().toString()).isEqualTo(found.get().getAddress().toString());
        assertThat(business.getBusinessType()).isEqualTo(found.get().getBusinessType());

    }

    /**
     * Tests that no user is returned when calling findBusinessById() with a non-existing id
     */
    @Test
    public void whenFindByNonExistingId_thenDontReturnBusiness() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        entityManager.persist(address);
        entityManager.flush();

        Business business = new Business(user.getId(),
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
        found = businessRepository.findBusinessById(Integer.valueOf("0"));

        // then
        assertThat(found.isEmpty()).isTrue();
    }

    /**
     * Tests that a list which contain an user is returned when calling findBusinessesByName() with a existing name
     */
    @Test
    public void whenFindByExistingName_thenReturnAListContainBusinesses() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        entityManager.persist(address);
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
        listFound = businessRepository.findBusinessesByName("example name");

        // then
        assertThat(business.equals(listFound.get(0))).isTrue();
    }

    /**
     * Tests that a empty list is returned when calling findBusinessesByName() with a non-existing name
     */
    @Test
    public void whenFindByNonExistingName_thenReturnAEmptyList() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        entityManager.persist(address);
        entityManager.flush();

        Business business = new Business(user.getId(),
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
        listFound = businessRepository.findBusinessesByName("");

        // then
        assertThat(listFound.isEmpty()).isTrue();
    }

    /**
     * Tests that a list which contain an user is returned when calling findBusinessesByAddress() with a
     * existing address
     */
    @Test
    public void whenFindByExistingAddress_thenReturnAListContainBusinesses() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );
        entityManager.persist(address);
        entityManager.flush();

        Business business = new Business(user.getId(),
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
        listFound = businessRepository.findBusinessesByAddress(address);

        // then
        assertThat(business.equals(listFound.get(0))).isTrue();
    }

    /**
     * Tests that a list which contain an user is returned when calling findBusinessesByAddress() with a
     * existing address
     */
    @Test
    public void whenFindByNonExistingAddress_thenReturnAEmptyList() throws Exception {
        // given
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        entityManager.persist(address);
        entityManager.flush();

        Address nonExistentAddress = new Address(
                "2/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
        );

        entityManager.persist(nonExistentAddress);
        entityManager.flush();

        Business business = new Business(user.getId(),
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
        listFound = businessRepository.findBusinessesByAddress(nonExistentAddress);

        // then
        assertThat(listFound.isEmpty()).isTrue();
    }
}
