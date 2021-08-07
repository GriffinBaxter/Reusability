package org.seng302.business.listing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalListingArgumentException;
import org.seng302.model.Address;
import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.seng302.model.InventoryItem;
import org.seng302.model.Product;
import org.seng302.model.Listing;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

/**
 * Test class for Listing
 * This class includes tests for validation of listings.
 */
class ListingTests {

    private Address address;

    private User user;

    private Business business;

    private Product product;

    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210",
                "Ilam"
        );
        user = new User(
                "first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, Month.JANUARY, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, Month.JANUARY, 1), LocalTime.of(0, 0)),
                Role.USER
        );
        business = new Business(
                user.getId(),
                "business name",
                "some text",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        business.setId(1);
        product = new Product(
                "PROD",
                business,
                "Beans",
                "Description",
                "Manufacturer",
                20.00,
                ""
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
    }

    /**
     * when price not exist and quantity less then inventory quantity
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void testCalculatePriceWhenPriceNotExistAndQuantityLessThenInventoryQuantity() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                3,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
        Assertions.assertEquals(19.50, listing.getPrice(), 0.0);
    }

    /**
     * when price not exist and quantity equal to inventory quantity
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void testCalculatePriceWhenPriceNotExistAndQuantityEqualToInventoryQuantity() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                4,
                null,
                "more info",
                LocalDateTime.now(),
                null
        );
        Assertions.assertEquals(21.99, listing.getPrice(), 0.0);
    }

    /**
     * when price exist and quantity equal to inventory quantity
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void testCalculatePriceWillBeOverriddenWhenPriceExist() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                4,
                30.00,
                "more info",
                LocalDateTime.now(),
                null
        );
        Assertions.assertEquals(30.00, listing.getPrice(), 0.0);
    }

    /**
     * when price exist and quantity less then inventory quantity
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void testCalculatePriceWillBeOverriddenWhenPriceExistEvenQuantityNotEqualToInventory() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                "more info",
                LocalDateTime.now(),
                null
        );
        Assertions.assertEquals(30.00, listing.getPrice(), 0.0);
    }

    /**
     * Test an exception is thrown when no inventoryItem exists (null).
     */
    @Test
    void TestInvalidInventoryItem() {
        try {
            Listing listing = new Listing(
                    null,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    null
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid inventory item", e.getMessage());
        }
    }

    /**
     * Test an exception is thrown when created doesn't exist (null).
     */
    @Test
    void TestInvalidCreated() {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    null,
                    null
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid creation date", e.getMessage());
        }
    }

    /**
     * Test an exception is thrown when closes date occurs before current date.
     */
    @Test
    void TestInvalidClosingDate() {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    LocalDateTime.of(2000, 10, 11, 0,0)
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid closing date.", e.getMessage());
        }
    }

    /**
     * Test closes date is set to expiry date of inventory time when not set (null)
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void TestNoClosingDateSet() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    null
            );
        Assertions.assertEquals(listing.getCloses(), LocalDateTime.of(inventoryItem.getExpires(), LocalTime.of(0, 0)));
    }

    /**
     * Test closes date is not overridden to expiry date of inventory item when supplied to constructor.
     * @throws IllegalListingArgumentException when trying to create listing with invalid data.
     */
    @Test
    void TestClosingDateSupplied() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(listing.getCloses(), LocalDateTime.of(2022, 1, 1, 0, 0));
    }

    // ********************************* MORE INFO **************************************

    /**
     * Test to see whether an IllegalListingArgumentException is thrown when trying to create a listing with
     * the length of more info greater than the max length.
     */
    @Test
    void isIllegalListingArgumentExceptionThrownWhenMoreInfoLengthGreaterThanMaxLengthTest() {
        String string = "A";
        String moreInfo = string.repeat(601); // max length = 600

        try {
            Listing listing = new Listing(
                    inventoryItem,
                    3,
                    30.00,
                    moreInfo,
                    LocalDateTime.now(),
                    LocalDateTime.of(2022, 1, 1, 0, 0)
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid more info", e.getMessage());
        }
    }

    /**
     * Test to see whether a listing is successfully created when more info contains symbols and length is
     * equal to max length.
     */
    @Test
    void isListingSuccessfullyCreatedWhenMoreInfoContainsSymbolsAndLengthEqualsMaxLength() throws IllegalListingArgumentException {
        String string = "Willing t0 accept low3r offers !^#9p40*$"; // 40 characters x 15 = 600
        String moreInfo = string.repeat(15); // max length = 600

        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                moreInfo,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertNotNull(listing);
        Assertions.assertEquals(moreInfo, listing.getMoreInfo());
    }

    /**
     * Test to see whether a listing is successfully created when more info is empty.
     */
    @Test
    void isListingSuccessfullyCreatedWhenMoreInfoIsEmpty() throws IllegalListingArgumentException {
        String moreInfo = "";

        Listing listing = new Listing(
                inventoryItem,
                3,
                30.00,
                moreInfo,
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertNotNull(listing);
        Assertions.assertEquals(moreInfo, listing.getMoreInfo());
    }

    // ********************************** QUANTITY **************************************

    /**
     * Test to see whether an IllegalListingArgumentException is thrown when trying to create a listing with
     * quantity equal zero.
     */
    @Test
    void isIllegalListingArgumentExceptionThrownWhenQuantityEqualsZero() {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    0,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    LocalDateTime.of(2022, 1, 1, 0, 0)
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid quantity", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalListingArgumentException is thrown when trying to create a listing with
     * quantity greater than inventory item quantity.
     */
    @Test
    void isIllegalListingArgumentExceptionThrownWhenQuantityGreaterThanInventoryItemQuantity() {
        try {
            Listing listing = new Listing(
                    inventoryItem,
                    inventoryItem.getQuantity() + 1,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    LocalDateTime.of(2022, 1, 1, 0, 0)
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid quantity", e.getMessage());
        }
    }

    /**
     * Test to see whether an IllegalListingArgumentException is thrown when trying to create a listing with
     * a quantity greater than remaining inventory item quantity due to other listings existing.
     */
    @Test
    void isIllegalListingArgumentExceptionThrownWhenQuantityGreaterThanRemainingInventoryItemQuantity() throws IllegalListingArgumentException {
        Listing anotherListing = new Listing(
                inventoryItem,
                3,
                99.99,
                "More info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1,1, 0, 0)
        );
        inventoryItem.addListing(anotherListing);

        try {
            Listing listing = new Listing(
                    inventoryItem,
                    2,
                    30.00,
                    "more info",
                    LocalDateTime.now(),
                    LocalDateTime.of(2022, 1, 1, 0, 0)
            );
        } catch (IllegalListingArgumentException e) {
            Assertions.assertEquals("Invalid quantity", e.getMessage());
        }
    }

    /**
     * Test to see whether a listing is successfully created when other listings exist, but inventory item quantity
     * is not exceeded.
     */
    @Test
    void isListingSuccessfullyCreatedWhenOtherListingsExist() throws IllegalListingArgumentException {
        Listing anotherListing = new Listing(
                inventoryItem,
                2,
                99.99,
                "More info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1,1, 0, 0)
        );
        inventoryItem.addListing(anotherListing);

        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertNotNull(listing);
        Assertions.assertEquals(2, listing.getQuantity());
    }

    /**
     * Test that product name will be correctly add to listing
     */
    @Test
    void isProductNameBeenCurrentlyAdd_WhenListingCreat() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(product.getName(), listing.getProductName());
    }

    /**
     * Test that seller's business type will be correctly add to listing
     */
    @Test
    void isBusinessTypeBeenCurrentlyAdd_WhenListingCreat() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(business.getBusinessType(), listing.getBusinessType());
    }

    /**
     * Test that seller's name will be correctly add to listing
     */
    @Test
    void isSellerNameBeenCurrentlyAdd_WhenListingCreat() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(business.getName(), listing.getSellerName());
    }

    /**
     * Test that seller's country will be correctly add to listing
     */
    @Test
    void isSellerCountryBeenCurrentlyAdd_WhenListingCreat() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(business.getAddress().getCountry(), listing.getCountry());
    }

    /**
     * Test that seller's city will be correctly add to listing (country exist)
     */
    @Test
    void isSellerCityBeenCurrentlyAdd_WhenListingCreat() throws IllegalListingArgumentException {
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertEquals(business.getAddress().getCity(), listing.getCity());
    }

    /**
     * Test that seller's city will be correctly add to listing (country exist)
     */
    @Test
    void isSellerCityBeenCurrentlyAdd_WhenListingCreatAndBusinessCityNotExist() throws IllegalListingArgumentException {
        address.setCity(null);
        business.setAddress(address);
        product.setBusiness(business);
        inventoryItem.setProduct(product);
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertNull(listing.getCity());
    }

    /**
     * Test to see whether the addToANewUserBookMark function can successfully add new user to bookmarked list
     */
    @Test
    void testANewUserSuccessfullyBeenBookMarkCurrentListing() throws IllegalListingArgumentException {
        // Given
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertTrue(listing.getBookMarkedListings().isEmpty());

        // When
        listing.addToANewUserBookMark(user);

        // Then
        Assertions.assertEquals(1, listing.getBookMarkedListings().size());
        Assertions.assertEquals(user, listing.getBookMarkedListings().get(0));
    }

    /**
     * Test to see whether the addToANewUserBookMark function will not add exist user again to bookmarked list
     */
    @Test
    void testAExistUserCanNotBookMarkCurrentListingAgain() throws IllegalListingArgumentException {
        // Given
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        listing.addToANewUserBookMark(user);
        Assertions.assertEquals(1, listing.getBookMarkedListings().size());
        Assertions.assertEquals(user, listing.getBookMarkedListings().get(0));

        // When
        listing.addToANewUserBookMark(user);

        // Then
        Assertions.assertEquals(1, listing.getBookMarkedListings().size());
    }

    /**
     * Test to see whether the removeFromAUserBookMark function will successfully remove given user if its exist.
     */
    @Test
    void testAExistListingSuccessfullyBeenRemoveFromBookMark() throws IllegalListingArgumentException {
        // Given
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        listing.addToANewUserBookMark(user);
        Assertions.assertEquals(1, listing.getBookMarkedListings().size());
        Assertions.assertEquals(user, listing.getBookMarkedListings().get(0));

        // When
        listing.removeFromAUserBookMark(user);

        // Then
        Assertions.assertTrue(listing.getBookMarkedListings().isEmpty());
    }

    /**
     * Test is isBookmarked function return true when user marked current listing
     */
    @Test
    void testIsBookmarkedReturnTure_whenItMarked() throws IllegalListingArgumentException {
        // Given
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        listing.addToANewUserBookMark(user);
        Assertions.assertEquals(1, listing.getBookMarkedListings().size());
        Assertions.assertEquals(user, listing.getBookMarkedListings().get(0));

        //When
        Assertions.assertTrue(listing.isBookmarked(user));
    }

    /**
     * Test is isBookmarked function return false when user do not mark current listing
     */
    @Test
    void testIsBookmarkedReturnTure_whenItNotMarked() throws IllegalListingArgumentException {
        // Given
        Listing listing = new Listing(
                inventoryItem,
                2,
                30.00,
                "more info",
                LocalDateTime.now(),
                LocalDateTime.of(2022, 1, 1, 0, 0)
        );
        Assertions.assertTrue(listing.getBookMarkedListings().isEmpty());

        //When
        Assertions.assertFalse(listing.isBookmarked(user));
    }
}
