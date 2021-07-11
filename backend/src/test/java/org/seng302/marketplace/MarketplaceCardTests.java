package org.seng302.marketplace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalAddressArgumentException;
import org.seng302.exceptions.IllegalMarketplaceCardArgumentException;
import org.seng302.model.Address;
import org.seng302.model.MarketplaceCard;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;
import org.seng302.model.User;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * MarketplaceCard test class.
 */
class MarketplaceCardTests {

    private static Address address;
    private static User user;

    @BeforeAll
    static void before() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
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
     * Tests that a valid MarketplaceCard can be successfully created and not throw an error.
     */
    @Test
    void TestMarketplaceCard_GivenValidData_SuccessfullyCreated() throws IllegalMarketplaceCardArgumentException {
        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        Assertions.assertEquals(marketplaceCard.getCreatorId(), user.getId());
        Assertions.assertEquals(marketplaceCard.getCreator(), user);
        Assertions.assertEquals(Section.FORSALE, marketplaceCard.getSection());
        Assertions.assertEquals(marketplaceCard.getCreated(), LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)));
        Assertions.assertEquals(marketplaceCard.getDisplayPeriodEnd(), LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 8), LocalTime.of(0, 0)));
        Assertions.assertEquals("Hayley's Birthday", marketplaceCard.getTitle());
        Assertions.assertEquals("Come join Hayley and help her celebrate her birthday!", marketplaceCard.getDescription());
    }

    /**
     * Tests that an invalid title throws an invalid title error.
     */
    @Test
    void TestMarketplaceCard_GivenInvalidTitle_ErrorThrown() {
        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    "",
                    "Come join Hayley and help her celebrate her birthday!"
            );
        } catch (IllegalMarketplaceCardArgumentException e) {
            Assertions.assertEquals("Invalid title", e.getMessage());
        }

    }

    /**
     * Tests that an invalid description throws an invalid description error.
     */
    @Test
    void TestMarketplaceCard_GivenInvalidDescription_ErrorThrown() {
        String string = "H";
        String description = string.repeat(301); // max length = 300
        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    "Hayley's Birthday",
                    description
            );
        } catch (IllegalMarketplaceCardArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Tests that the optional field of description is set to null when empty.
     */
    @Test
    void TestMarketplaceCard_GivenNoDescription_SuccessfullyCreated() throws IllegalMarketplaceCardArgumentException {
        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                ""
        );
        Assertions.assertNull(marketplaceCard.getDescription());
    }

    // *************************************************** TITLE *******************************************************

    /**
     * Test to see whether an IllegalMarketplaceCardArgumentException is thrown when trying to create a marketplace
     * card with the length of title less than the min length.
     */
    @Test
    void isIllegalMarketplaceCardArgumentExceptionThrownWhenTitleLengthLessThanMinLengthTest() {
        String title = ""; // min length = 1

        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    title,
                    ""
            );
        } catch (IllegalMarketplaceCardArgumentException e) {
            Assertions.assertEquals("Invalid title", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalMarketplaceCardArgumentException is thrown when trying to create a marketplace
     * card with the length of title greater than the max length.
     */
    @Test
    void isIllegalMarketplaceCardArgumentExceptionThrownWhenTitleLengthGreaterThanMaxLengthTest() {
        String string = "a";
        String title = string.repeat(51); // max length = 50

        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    title,
                    ""
            );
        } catch (IllegalMarketplaceCardArgumentException e) {
            Assertions.assertEquals("Invalid title", e.getMessage());
        }
    }

    /**
     * Test to see whether a marketplace card is successfully created when its title is of correct length and
     * contains numbers, symbols, and spaces.
     */
    @Test
    void isMarketplaceCardSuccessfullyCreatedWhenTitleContainsNumbersSymbolsAndSpacesTest() throws IllegalMarketplaceCardArgumentException {
        String title = "Hayley's 99th Birthday !@#$%^&*()_-+=[]{}|?<>,.";

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                title,
                ""
        );

        Assertions.assertNotNull(marketplaceCard);
        Assertions.assertEquals(title, marketplaceCard.getTitle());
    }

    /**
     * Test to see whether a marketplace card is successfully created when its title length is equal to the min length.
     */
    @Test
    void isMarketplaceCardSuccessfullyCreatedWhenTitleLengthEqualsMinLengthTest() throws IllegalMarketplaceCardArgumentException {
        String title = "Ha";

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                title,
                ""
        );

        Assertions.assertNotNull(marketplaceCard);
        Assertions.assertEquals(title, marketplaceCard.getTitle());
    }

    /**
     * Test to see whether a marketplace card is successfully created when its title length is equal to the max length.
     */
    @Test
    void isMarketplaceCardSuccessfullyCreatedWhenTitleLengthEqualsMaxLengthTest() throws IllegalMarketplaceCardArgumentException {
        String string = "H";
        String title = string.repeat(50); // max length = 50

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                title,
                ""
        );

        Assertions.assertNotNull(marketplaceCard);
        Assertions.assertEquals(title, marketplaceCard.getTitle());
    }

    // *********************************************** DESCRIPTION *****************************************************

    /**
     * Test to see whether an IllegalMarketplaceCardArgumentException is thrown when trying to create a marketplace
     * card with the length of description greater than the max length.
     */
    @Test
    void isIllegalMarketplaceCardArgumentExceptionThrownWhenDescriptionLengthGreaterThanMaxLengthTest() {
        String string = "a";
        String description = string.repeat(301); // max length = 300

        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    "Hayley's Birthday",
                    description
            );
        } catch (IllegalMarketplaceCardArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether marketplace card is successfully created when description is empty.
     */
    @Test
    void isMarketplaceCardSuccessfullyCreatedWhenDescriptionEmptyTest() throws IllegalMarketplaceCardArgumentException {
        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                ""
        );

        Assertions.assertNotNull(marketplaceCard);
        Assertions.assertNull(marketplaceCard.getDescription());
    }

    /**
     * Test to see whether marketplace card is successfully created when description equals max length
     * and contains numbers, spaces, and symbols.
     */
    @Test
    void isMarketplaceCardSuccessfullyCreatedWhenDescriptionLengthEqualsMaxLengthAndContainsNumberSpacesAndSymbolsTest()
            throws IllegalMarketplaceCardArgumentException {
        String string = "Hayley's 99th Birthday !@#$%^&*()_-+=[]{}|?<>,.   "; // 50 characters x 6 = 300
        String description = string.repeat(6); // max length = 300

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                description
        );

        Assertions.assertNotNull(marketplaceCard);
        Assertions.assertEquals(description, marketplaceCard.getDescription());
    }

    // ***************** extendDisplayPeriod ********************

    /**
     * Tests that when extendDisplayPeriod is called on a marketplace card, the displayPeriodEnd date is extended by one week.
     */
    @Test
    void extendDisplayPeriodTest() throws IllegalMarketplaceCardArgumentException {
        LocalDateTime creationDate = LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0));
        LocalDateTime originalDisplayPeriodEnd = creationDate.plusWeeks(1);
        LocalDateTime expectedNewDisplayPeriodEnd = originalDisplayPeriodEnd.plusWeeks(1);

        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                creationDate,
                "Hayley's Birthday",
                "Description"
        );

        marketplaceCard.extendDisplayPeriod();

        Assertions.assertNotEquals(originalDisplayPeriodEnd, marketplaceCard.getDisplayPeriodEnd());
        Assertions.assertEquals(expectedNewDisplayPeriodEnd, marketplaceCard.getDisplayPeriodEnd());
    }
}
