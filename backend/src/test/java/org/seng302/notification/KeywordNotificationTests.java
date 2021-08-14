package org.seng302.notification;

import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.seng302.exceptions.IllegalKeywordArgumentException;
import org.seng302.exceptions.IllegalKeywordNotificationArgumentException;
import org.seng302.model.Keyword;
import org.seng302.model.KeywordNotification;
import java.time.LocalDateTime;


/**
 * KeywordNotification test class.
 * This class tests the validation for KeywordNotification.
 */
class KeywordNotificationTests {

    private static Keyword keyword;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeAll
    static void before() throws IllegalKeywordArgumentException {
        keyword = new Keyword("Name", LocalDateTime.now());
    }

    /**
     * Test to see whether an IllegalKeywordNotificationArgumentException is thrown when trying to create a keyword
     * notification with description being an empty string (length of description less than the min length of 2).
     */
    @Test
    void isIllegalKeywordNotificationExceptionThrownWhenDescriptionIsEmptyStringTest() {
        try {
            KeywordNotification keywordNotification = new KeywordNotification("", LocalDateTime.now(), keyword);
            Assertions.fail("IllegalKeywordNotificationArgumentException was expected to be thrown.");
        } catch (IllegalKeywordNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalKeywordNotificationArgumentException is thrown when trying to create a keyword
     * notification with the length of description being greater than the max length.
     */
    @Test
    void isIllegalKeywordNotificationExceptionThrownWhenDescriptionLengthIsGreaterThanMaxLengthTest() {
        String description = "a".repeat(601); // max length = 600
        try {
            KeywordNotification keywordNotification = new KeywordNotification(description, LocalDateTime.now(), keyword);
            Assertions.fail("IllegalKeywordNotificationArgumentException was expected to be thrown.");
        } catch (IllegalKeywordNotificationArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalKeywordNotificationArgumentException is thrown when trying to create a keyword
     * notification when created is null.
     */
    @Test
    void isIllegalKeywordNotificationExceptionThrownWhenCreatedIsNullTest() {
        try {
            KeywordNotification keywordNotification = new KeywordNotification("Description", null, keyword);
            Assertions.fail("IllegalKeywordNotificationArgumentException was expected to be thrown.");
        } catch (IllegalKeywordNotificationArgumentException e) {
            Assertions.assertEquals("Created date is required", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalKeywordNotificationArgumentException is thrown when trying to create a keyword
     * notification when keyword is null.
     */
    @Test
    void isIllegalKeywordNotificationExceptionThrownWhenKeywordIsNullTest() {
        try {
            KeywordNotification keywordNotification = new KeywordNotification("Description", LocalDateTime.now(), null);
            Assertions.fail("IllegalKeywordNotificationArgumentException was expected to be thrown.");
        } catch (IllegalKeywordNotificationArgumentException e) {
            Assertions.assertEquals("Keyword is required", e.getMessage());
        }
    }

    /**
     * Test to see whether KeywordNotification is successfully created when all data is valid
     */
    @Test
    void isKeywordNotificationCreatedWhenValidDataIsRepliedTest() throws IllegalKeywordNotificationArgumentException {
        LocalDateTime created = LocalDateTime.of(2000, 6, 21, 0, 0, 0);

        KeywordNotification keywordNotification = new KeywordNotification("Description", created, keyword);
        Assertions.assertEquals("Description", keywordNotification.getDescription());
        Assertions.assertEquals(created, keywordNotification.getCreated());
        Assertions.assertEquals(keyword, keywordNotification.getKeyword());
    }
}
