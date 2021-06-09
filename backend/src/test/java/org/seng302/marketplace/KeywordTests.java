package org.seng302.marketplace;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.model.Address;
import org.seng302.model.*;
import org.seng302.model.enums.Role;
import org.seng302.model.enums.Section;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * Keyword test class.
 * Contains tests to check if exceptions are thrown when invalid data is supplied.
 */
class KeywordTests {

    private Keyword keyword;
    private Address address;
    private User user;
    private MarketplaceCard card;
    private String name;

    @BeforeEach
    void setup() throws Exception {
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
     * Test to see whether an IllegalKeywordArgumentException is thrown when trying to create a keyword with
     * the length of name less than the min length.
     */
    @Test
    void isIllegalKeywordArgumentExceptionThrownWhenNameLengthLessThanMinLengthTest() {
        try {
            keyword = new Keyword ("", LocalDateTime.now(), card);
        } catch (IllegalKeywordArgumentException e) {
            Assertions.assertEquals("Invalid name", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalKeywordArgumentException is thrown when trying to create a keyword with
     * the length of name greater than the max length.
     */
    @Test
    void isIllegalKeywordArgumentExceptionThrownWhenNameLengthGreaterThanMaxLengthTest() {
        try {
            name = "A";
            keyword = new Keyword (name.repeat(30), LocalDateTime.now(), card);
        } catch (IllegalKeywordArgumentException e) {
            Assertions.assertEquals("Invalid name", e.getMessage());
        }
    }

    /**
     * Test to see whether a keyword is successfully created when keyword name is of the correct length,
     * contains symbols.
     */
    @Test
    void isKeywordSuccessfullyCreatedWhenNameOfCorrectLengthAndContainsSymbolsTest() throws IllegalKeywordArgumentException {
        name = "Money Maker $$$'*!";
        keyword = new Keyword(name, LocalDateTime.now(), card);
        Assertions.assertNotNull(keyword);
        Assertions.assertEquals(name, keyword.getName());
    }

    /**
     * Test to see whether a keyword is successfully created when keyword name has same length of the min length of 2.
     */
    @Test
    void isKeywordSuccessfullyCreatedWhenNameLengthEqualsMinLengthTest() throws IllegalKeywordArgumentException {
        name = "Mo"; // min length = 2
        keyword = new Keyword(name, LocalDateTime.now(), card);
        Assertions.assertNotNull(keyword);
        Assertions.assertEquals(name, keyword.getName());
    }

    /**
     * Test to see whether a keyword is successfully created when keyword name has same length of the max length of 20.
     */
    @Test
    void isKeywordSuccessfullyCreatedWhenNameLengthEqualsMaxLengthTest() throws IllegalKeywordArgumentException {
        name = "Money Maker $$$'*!20"; // min length = 20
        keyword = new Keyword(name, LocalDateTime.now(), card);
        Assertions.assertNotNull(keyword);
        Assertions.assertEquals(name, keyword.getName());
    }


}
