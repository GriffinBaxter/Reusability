package org.seng302.bookmarkedListingMessage;

import org.apache.tomcat.jni.Local;
import org.junit.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.seng302.exceptions.*;
import org.seng302.model.*;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.enums.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;


/**
 * BookmarkedListingMessage test class.
 * This class tests the validation for BookmarkedListingMessage.
 */
class BookmarkedListingMessageTests {

    private static String description;
    private static Listing listing;
    private static InventoryItem inventoryItem;
    private static Product product;
    private static Business business;
    private static Address address;
    private static User user;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @BeforeAll
    static void before() throws IllegalProductArgumentException, IllegalAddressArgumentException, IllegalUserArgumentException, IllegalBusinessArgumentException, IllegalInventoryItemArgumentException, IllegalListingArgumentException {
        description = "The listing 'Whittakers Chocolate - Hazelnut' has been bookmarked";

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

        business = new Business(
                1,
                "Countdown",
                "Main competitor of New World.",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );

        product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                "9400547002634"
        );

        inventoryItem = new InventoryItem(
                product,
                product.getProductId(),
                4,
                6.5,
                21.99,
                null,
                null,
                null,
                LocalDate.of(2022, 1,1)
        );

        listing = new Listing(
                inventoryItem,
                3,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
    }

    /**
     * Test to see whether an IllegalBookmarkedListingMessageArgumentException is thrown when trying to create a bookmarked
     * listing message with description being an empty string (length of description less than the min length of 2).
     */
    @Test
    void isIllegalBookmarkedListingMessageArgumentExceptionThrownWhenDescriptionIsEmptyStringTest() {
        try {
            BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage("", LocalDateTime.now(), listing);
            Assertions.fail("BookmarkedListingMessageArgumentException was expected to be thrown.");
        } catch (IllegalBookmarkedListingMessageArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalBookmarkedListingMessageArgumentException is thrown when trying to create a bookmarked
     * listing message with the length of description being greater than the max length.
     */
    @Test
    void isIllegalBookmarkedListingMessageArgumentExceptionThrownWhenDescriptionLengthIsGreaterThanMaxLengthTest() {
        String description = "a".repeat(601); // max length = 600
        try {
            BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage(description, LocalDateTime.now(), listing);
            Assertions.fail("IllegalBookmarkedListingMessageArgumentException was expected to be thrown.");
        } catch (IllegalBookmarkedListingMessageArgumentException e) {
            Assertions.assertEquals("Invalid description", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalBookmarkedListingMessageArgumentException is thrown when trying to create a bookmarked
     * listing message when created is null.
     */
    @Test
    void isIllegalBookmarkedListingMessageArgumentExceptionThrownWhenCreatedIsNullTest() {
        try {
            BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage("Description", null, listing);
            Assertions.fail("IllegalBookmarkedListingMessageArgumentException was expected to be thrown.");
        } catch (IllegalBookmarkedListingMessageArgumentException e) {
            Assertions.assertEquals("Created date is required", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalBookmarkedListingMessageArgumentException is thrown when trying to create a bookmarked
     * listing message when listing is null.
     */
    @Test
    void isIllegalKeywordNotificationExceptionThrownWhenKeywordIsNullTest() {
        try {
            BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage("Description", LocalDateTime.now(), null);
            Assertions.fail("IllegalBookmarkedListingMessageArgumentException was expected to be thrown.");
        } catch (IllegalBookmarkedListingMessageArgumentException e) {
            Assertions.assertEquals("Listing is required", e.getMessage());
        }
    }

    /**
     * Test to see whether BookmarkedListingMessage is successfully created when all data is valid
     */
    @Test
    void isBookmarkedListingMessageCreatedWhenValidDataIsRepliedTest() throws IllegalBookmarkedListingMessageArgumentException {
        LocalDateTime created = LocalDateTime.of(2000, 6, 21, 0, 0, 0);

        BookmarkedListingMessage bookmarkedListingMessage = new BookmarkedListingMessage("Description", created, listing);
        Assertions.assertEquals("Description", bookmarkedListingMessage.getDescription());
        Assertions.assertEquals(created, bookmarkedListingMessage.getCreated());
        Assertions.assertEquals(listing, bookmarkedListingMessage.getListing());
    }
}
