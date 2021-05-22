package org.seng302.marketplace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.main.Main;
import org.seng302.user.Role;
import org.seng302.user.User;
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

    private Page<MarketplaceCard> foundMarketplaceCards;

    private MarketplaceCard marketplaceCard;
    private MarketplaceCard marketplaceCard2;
    private MarketplaceCard marketplaceCard3;
    private MarketplaceCard marketplaceCard4;
    private MarketplaceCard marketplaceCard5;

    /**
     * Sets up data for testing
     */
    @BeforeEach
    public void beforeEach() throws Exception {
        // Address
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

        // User
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        user = entityManager.persist(user);
        entityManager.flush();

        //Cards
        marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        entityManager.persist(marketplaceCard);
        entityManager.flush();

        marketplaceCard2 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2019, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Money",
                "I'm poor..."
        );
        entityManager.persist(marketplaceCard2);
        entityManager.flush();

        marketplaceCard3 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 21), LocalTime.of(0, 0)),
                "Your dignity",
                ""
        );
        entityManager.persist(marketplaceCard3);
        entityManager.flush();

        marketplaceCard4 = new MarketplaceCard(
                user.getId(),
                user,
                Section.WANTED,
                LocalDateTime.of(LocalDate.of(2021, Month.MAY, 18), LocalTime.of(0, 0)),
                "Ambo",
                "Been shot pls help"
        );
        entityManager.persist(marketplaceCard4);
        entityManager.flush();

        marketplaceCard5 = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 21), LocalTime.of(0, 0)),
                "WordArt Title's",
                ""
        );
        entityManager.persist(marketplaceCard5);
        entityManager.flush();
    }

    /**
     * Tests that a (correct) marketplace card is returned when calling findById() with an existing id
     */
    @Test
    public void whenFindByExistingId_thenReturnMarketplaceCard() throws Exception {

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

        // when
        foundMarketplaceCard = marketplaceCardRepository.findById(0);

        // then
        assertThat(foundMarketplaceCard.isEmpty()).isTrue();
    }

    /**
     * Tests that the correct marketplace cards are returned when calling findAllBySection() with a valid section
     */
    @Test
    public void whenFindBySection_thenReturnMarketplaceCardsWithSection() throws Exception {
        // given
        Sort sortBy = Sort.by(Sort.Order.desc("created").ignoreCase());
        Pageable pageable = PageRequest.of(0, 10, sortBy);

        // when
        foundMarketplaceCards = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        for (MarketplaceCard card: foundMarketplaceCards) {
            assertThat(card.getSection()).isEqualTo(Section.FORSALE);
        }
    }

    /**
     * Tests that no marketplace cards is returned when calling findAllBySection() with no cards in that section
     */
    @Test
    public void whenNoCardsInSectionAndFindBySection_thenReturnNoMarketplaceCards() throws Exception {
        // given
        Sort sortBy = Sort.by(Sort.Order.desc("created").ignoreCase());
        Pageable pageable = PageRequest.of(0, 10, sortBy);

        // when
        foundMarketplaceCards = marketplaceCardRepository.findAllBySection(Section.EXCHANGE, pageable);

        // then
        assertThat(foundMarketplaceCards.getNumberOfElements()).isEqualTo(0);
    }


    // ------------------------ Get All (Ordering + Pagination) ------------------------

    /**
     * Tests Ordering by Created Descending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    public void whenFindAllMarketplaceCardsBySection_thenReturnCreatedOrderedCardsDescending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.desc("created").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedCardTitles = new ArrayList<>();
        orderedCardTitles.add("WordArt Title's");
        orderedCardTitles.add("Your dignity");
        orderedCardTitles.add("Hayley's Birthday");

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        for (int i = 0; i < cardPage.getContent().size(); i++) {
            assertThat(cardPage.getContent().get(i).getTitle()).isEqualTo(orderedCardTitles.get(i));
        }
    }

    /**
     * Tests Ordering by Created Ascending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    public void whenFindAllMarketplaceCardsBySection_thenReturnCreatedOrderedCardsAscending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.asc("created").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedCardTitles = new ArrayList<>();
        orderedCardTitles.add("Hayley's Birthday");
        orderedCardTitles.add("WordArt Title's");
        orderedCardTitles.add("Your dignity");


        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        for (int i = 0; i < cardPage.getContent().size(); i++) {
            assertThat(cardPage.getContent().get(i).getTitle()).isEqualTo(orderedCardTitles.get(i));
        }
    }

    /**
     * Tests Ordering by Title Descending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    public void whenFindAllMarketplaceCardsBySection_thenReturnTitleOrderedCardsDescending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.desc("title").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedCardTitles = new ArrayList<>();
        orderedCardTitles.add("Your dignity");
        orderedCardTitles.add("WordArt Title's");
        orderedCardTitles.add("Hayley's Birthday");

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        for (int i = 0; i < cardPage.getContent().size(); i++) {
            assertThat(cardPage.getContent().get(i).getTitle()).isEqualTo(orderedCardTitles.get(i));
        }
    }

    /**
     * Tests Ordering by Title Ascending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    public void whenFindAllMarketplaceCardsBySection_thenReturnTitleOrderedCardsAscending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 3;
        Sort sortBy = Sort.by(Sort.Order.asc("title").ignoreCase());
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        ArrayList<String> orderedCardTitles = new ArrayList<>();
        orderedCardTitles.add("Hayley's Birthday");
        orderedCardTitles.add("WordArt Title's");
        orderedCardTitles.add("Your dignity");

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        for (int i = 0; i < cardPage.getContent().size(); i++) {
            assertThat(cardPage.getContent().get(i).getTitle()).isEqualTo(orderedCardTitles.get(i));
        }
    }

    /**
     * Tests Paging for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    public void whenFindAllMarketplaceCardsBySection_thenReturnPagedCards() throws Exception {
        // given
        int pageSize = 1;
        Sort sortBy = Sort.by(Sort.Order.desc("title").ignoreCase());

        Pageable pageable = PageRequest.of(0, pageSize, sortBy); // Page 1
        Pageable pageable2 = PageRequest.of(1, pageSize, sortBy); // Page 2
        Pageable pageable3 = PageRequest.of(2, pageSize, sortBy); // Page 3

        ArrayList<String> orderedCardTitles = new ArrayList<>();
        orderedCardTitles.add("Your dignity");
        orderedCardTitles.add("WordArt Title's");
        orderedCardTitles.add("Hayley's Birthday");

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);
        Page<MarketplaceCard> cardPage2 = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable2);
        Page<MarketplaceCard> cardPage3 = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable3);

        // then
        assertThat(cardPage.getContent().get(0).getTitle()).isEqualTo(orderedCardTitles.get(0));
        assertThat(cardPage2.getContent().get(0).getTitle()).isEqualTo(orderedCardTitles.get(1));
        assertThat(cardPage3.getContent().get(0).getTitle()).isEqualTo(orderedCardTitles.get(2));
    }
}
