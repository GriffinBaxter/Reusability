package org.seng302.marketplace;

import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.user.Role;
import org.seng302.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.Assert.assertEquals;

/**
 * Keyword test class.
 */
class KeywordTests {

    /**
     * Tests that an invalid name for a keyword (greater than max length) throws an error.
     * @exception Exception thrown if there is an error creating a new entity.
     */
    @Test
    void TestKeywordNameExceedingMaxLengthThrowsError() throws Exception {
        Address address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        User user = new User("testfirst",
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
        MarketplaceCard card = new MarketplaceCard(
                user.getId(),
                user,
                Section.FORSALE,
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                "Hayley's Birthday",
                "Come join Hayley and help her celebrate her birthday!"
        );
        try {
            String name = "A";
            Keyword keyword = new Keyword (name.repeat(50), LocalDateTime.now(), card);
        } catch (Exception e) {
            assertEquals("Invalid name", e.getMessage());
        }
    }

}
