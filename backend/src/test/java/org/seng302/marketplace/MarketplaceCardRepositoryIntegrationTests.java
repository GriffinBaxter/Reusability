package org.seng302.marketplace;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.Main;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.repository.MarketplaceCardRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * MarketplaceCardRepository test class.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class MarketplaceCardRepositoryIntegrationTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MarketplaceCardRepository marketplaceCardRepository;

    private Optional<MarketplaceCard> foundMarketplaceCard;

    private Page<MarketplaceCard> foundMarketplaceCards;

    private List<MarketplaceCard> foundMarketplaceCardsList;
    
    private Integer creatorId;
    private int creatorId1;
    private int creatorId2;

    private MarketplaceCard marketplaceCard;
    private MarketplaceCard marketplaceCard2;
    private MarketplaceCard marketplaceCard3;
    private MarketplaceCard marketplaceCard4;
    private MarketplaceCard marketplaceCard5;

    private List<MarketplaceCard> marketplaceCards;

    /**
     * Sets up data for testing
     */
    @BeforeEach
    void beforeEach() throws Exception {
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

        Address address2 = new Address(
                "111",
                "Grant Road",
                "Invercargill",
                "Southland",
                "New Zealand",
                "9879",
                "Otatara"
        );
        entityManager.persist(address2);
        entityManager.flush();

        // User
        User user = new User("testfirst", "testlast", "testmiddle", "testnick",
                "testbiography", "testemail@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316", address, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user);
        entityManager.flush();

        // User 2
        User user2 = new User("testfirstII", "testlastII", "testmiddleII", "testnickII",
                "testbiographyII", "testemail2@email.com", LocalDate.of(2020, 2, 2).minusYears(13),
                "6415216", address2, "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2), LocalTime.of(0, 0)),
                Role.USER
        );
        entityManager.persist(user2);
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
                user2.getId(),
                user2,
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

        marketplaceCards = List.of(marketplaceCard, marketplaceCard2, marketplaceCard3, marketplaceCard4, marketplaceCard5);

        creatorId1 = user.getId();
        creatorId2 = user2.getId();
    }

    /**
     * Tests that a (correct) marketplace card is returned when calling findById() with an existing id
     */
    @Test
    void whenFindByExistingId_thenReturnMarketplaceCard() {

        // when
        foundMarketplaceCard = marketplaceCardRepository.findById(marketplaceCard.getId());

        // then
        assertThat(foundMarketplaceCard).isPresent();

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
    void whenFindByNonExistingId_thenDontReturnMarketplaceCard() {

        // when
        foundMarketplaceCard = marketplaceCardRepository.findById(0);

        // then
        assertThat(foundMarketplaceCard).isEmpty();
    }

    /**
     * Tests that the correct marketplace cards are returned when calling findAllBySection() with a valid section
     */
    @Test
    void whenFindBySection_thenReturnMarketplaceCardsWithSection() {
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
    void whenNoCardsInSectionAndFindBySection_thenReturnNoMarketplaceCards() {
        // given
        Sort sortBy = Sort.by(Sort.Order.desc("created").ignoreCase());
        Pageable pageable = PageRequest.of(0, 10, sortBy);

        // when
        foundMarketplaceCards = marketplaceCardRepository.findAllBySection(Section.EXCHANGE, pageable);

        // then
        assertThat(foundMarketplaceCards.getNumberOfElements()).isZero();
    }


    // ------------------------ Get All (Ordering + Pagination) ------------------------

    /**
     * Tests Ordering by Created Descending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    void whenFindAllMarketplaceCardsBySection_thenReturnCreatedOrderedCardsDescending() {
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
    void whenFindAllMarketplaceCardsBySection_thenReturnCreatedOrderedCardsAscending() {
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
    void whenFindAllMarketplaceCardsBySection_thenReturnTitleOrderedCardsDescending() {
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
    void whenFindAllMarketplaceCardsBySection_thenReturnTitleOrderedCardsAscending() {
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
     * Tests Ordering by Address Ascending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    void whenFindAllMarketplaceCardsBySection_thenReturnAddressOrderedCardsAscending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 1;
        Sort sortBy = Sort.by(Sort.Order.asc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.asc("creator.homeAddress.city").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // Expected suburb
        String suburb = "Ilam";

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        assertThat(cardPage.getContent().get(0).getCreator().getHomeAddress().getSuburb()).isEqualTo(suburb);

    }

    /**
     * Tests Ordering by Address Descending for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    void whenFindAllMarketplaceCardsBySection_thenReturnAddressOrderedCardsDescending() throws Exception {
        // given
        int pageNo = 0;
        int pageSize = 1;
        Sort sortBy = Sort.by(Sort.Order.desc("creator.homeAddress.suburb").ignoreCase()).and(Sort.by(Sort.Order.desc("creator.homeAddress.city").ignoreCase()));
        Pageable pageable = PageRequest.of(pageNo, pageSize, sortBy);

        // Expected suburb
        String suburb = "Otatara";

        // when
        Page<MarketplaceCard> cardPage = marketplaceCardRepository.findAllBySection(Section.FORSALE, pageable);

        // then
        assertThat(cardPage.getContent().get(0).getCreator().getHomeAddress().getSuburb()).isEqualTo(suburb);
    }

    /**
     * Tests Paging for getting all Marketplace Cards by Section (findAllBySection)
     */
    @Test
    void whenFindAllMarketplaceCardsBySection_thenReturnPagedCards() {
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

    // -----------------------------Find All Market Card-----------------------------

    /**
     * Test finding all cards.
     */
    @Test
    void whenFindAll_thenReturnAllCard(){
        List<MarketplaceCard> marketplaceCards = marketplaceCardRepository.findAll();
        assertThat(marketplaceCards.size()).isEqualTo(marketplaceCards.size());
    }

    // ----------------------------- Find By Creator Id -----------------------------

    /**
     * Tests finding cards created by a given user (creator ID 1).
     */
    @Test
    void whenFindByCreatorId1_thenReturnCreatorsCards() {
        // given
        creatorId = creatorId1;
        
        // when
        foundMarketplaceCardsList = marketplaceCardRepository.findMarketplaceCardByCreatorId(creatorId);
        
        // then
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard)).isTrue();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard2)).isTrue();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard4)).isTrue();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard5)).isTrue();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard3)).isFalse();
    }

    /**
     * Tests finding cards created by a given user (creator ID 2).
     */
    @Test
    void whenFindByCreatorId2_thenReturnCreatorsCards() {
        // given
        creatorId = creatorId2;
        
        // when
        foundMarketplaceCardsList = marketplaceCardRepository.findMarketplaceCardByCreatorId(creatorId);

        // then
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard3)).isTrue();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard)).isFalse();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard2)).isFalse();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard4)).isFalse();
        assertThat(foundMarketplaceCardsList.contains(marketplaceCard5)).isFalse();
    }

    /**
     * Tests attempting to find cards created by a non-existent user (creator ID 0).
     */
    @Test
    void whenFindByCreatorId0_thenReturnEmptyList() {
        // given
        creatorId = 0;

        // when
        foundMarketplaceCardsList = marketplaceCardRepository.findMarketplaceCardByCreatorId(creatorId);

        // then
        assertThat(foundMarketplaceCardsList.isEmpty()).isTrue();
    }
}
