package org.seng302.marketplace;

import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
import org.seng302.user.UserPayload;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
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
 * MarketplaceCardRepository test class.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
public class MarketplaceCardRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    private Optional<MarketplaceCard> foundMarketplaceCard;

    /**
     * Tests that a (correct) marketplace card is returned when calling findById() with an existing id
     */
    @Test
    public void whenFindByExistingId_thenReturnMarketplaceCard() throws Exception {
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

        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        user = entityManager.persist(user);
        entityManager.flush();

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // when
        foundMarketplaceCard = marketplaceCardRepository.findById(marketplaceCard.getId());

        // then
        assertThat(foundMarketplaceCard.isPresent()).isTrue();

        assertThat(marketplaceCard.getId()).isEqualTo(foundMarketplaceCard.get().getId());
        assertThat(marketplaceCard.getCreator()).isEqualTo(foundMarketplaceCard.get().getCreator());
        assertThat(marketplaceCard.getSection()).isEqualTo(foundMarketplaceCard.get().getSection());
        assertThat(marketplaceCard.getCreated()).isEqualTo(foundMarketplaceCard.get().getCreated());
        assertThat(marketplaceCard.getDisplayPeriodEnd()).isEqualTo(foundMarketplaceCard.get().getDisplayPeriodEnd());
        assertThat(marketplaceCard.getTitle()).isEqualTo(foundMarketplaceCard.get().getTitle());
        assertThat(marketplaceCard.getDescription()).isEqualTo(foundMarketplaceCard.get().getDescription());
    }

    /**
     * Tests that no marketplace card is returned when calling findById() with a non-existing id
     */
    @Test
    public void whenFindByNonExistingId_thenDontReturnMarketplaceCard() throws Exception {
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

        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        user = entityManager.persist(user);
        entityManager.flush();

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        // when
        foundMarketplaceCard = marketplaceCardRepository.findById(0);

        // then
        assertThat(foundMarketplaceCard.isEmpty()).isTrue();
    }
}
