package org.seng302.marketplace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.user.Role;
import org.seng302.user.User;
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
    void TestMarketplaceCard_GivenValidData_SuccessfullyCreated() throws Exception {
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
        Assertions.assertEquals(marketplaceCard.getSection(), Section.FORSALE);
        Assertions.assertEquals(marketplaceCard.getCreated(), LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)));
        Assertions.assertEquals(marketplaceCard.getDisplayPeriodEnd(), LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 8), LocalTime.of(0, 0)));
        Assertions.assertEquals(marketplaceCard.getTitle(), "Hayley's Birthday");
        Assertions.assertEquals(marketplaceCard.getDescription(), "Come join Hayley and help her celebrate her birthday!");
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
                    "a",
                    "Come join Hayley and help her celebrate her birthday!"
            );
        } catch (Exception e) {
            assertEquals("Invalid title", e.getMessage());
        }

    }

    /**
     * Tests that an invalid description throws an invalid description error.
     */
    @Test
    void TestMarketplaceCard_GivenInvalidDescription_ErrorThrown() {
        String string = "H";
        String description = string.repeat(1000);
        try {
            MarketplaceCard marketplaceCard = new MarketplaceCard(
                    user.getId(),
                    user,
                    Section.FORSALE,
                    LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                    "Hayley's Birthday",
                    description
            );
        } catch (Exception e) {
            assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Tests that the optional field of description is set to null when empty.
     */
    @Test
    void TestMarketplaceCard_GivenNoDescription_SuccessfullyCreated() throws Exception {
        MarketplaceCard marketplaceCard = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                ""
        );
        assertNull(marketplaceCard.getDescription());
    }

}