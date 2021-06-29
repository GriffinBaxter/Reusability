package org.seng302.keyword;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.model.Address;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

/**
 * Keyword test class.
 */
class KeywordTests {

    private static Address address;
    private static User user;
    private static MarketplaceCard card;

    /**
     * Set up for all tests in KeywordTests
     * @throws Exception thrown if there is an error creating a new entity.
     */
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
        card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
    }

    /**
     * Tests that an invalid name for a keyword (greater than max length) throws an error.
     */
    @Test
    void TestKeywordNameExceedingMaxLengthThrowsError() {
        try {
            String name = "A";
            Keyword keyword = new Keyword (name.repeat(50), LocalDateTime.now(), card);
        } catch (Exception e) {
            Assertions.assertEquals("Invalid name", e.getMessage());
        }
    }

    /**
     * Tests that an invalid name for a keyword (less than min length) throws an error.
     */
    @Test
    void TestKeywordNameLessThanMinLengthThrowsError() {

        try {
            String name = "A";
            Keyword keyword = new Keyword (name, LocalDateTime.now(), card);
        } catch (Exception e) {
            Assertions.assertEquals("Invalid name", e.getMessage());
        }
    }

    /**
     * Tests than a valid name for a keyword creates a keyword
     */
    @Test
    void TestKeywordNameIsValid() throws Exception {
        String name = "TestKeyword";
        Keyword keyword = new Keyword (name, LocalDateTime.now(), card);
        Assertions.assertEquals(name, keyword.getName());
    }
}
