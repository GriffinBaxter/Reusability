package org.seng302.business;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.address.Address;
import org.seng302.user.Role;
import org.seng302.user.User;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.*;

/**
 * BusinessAccount test class
 */
public class BusinessTest {

    private static User user;
    private static Address address;

    @BeforeAll
    public static void before() throws Exception {
        address = new Address(
                "3/24",
                "Ilam Road",
                "Christchurch",
                "Canterbury",
                "New Zealand",
                "90210"
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
    }

    @Test
    public void TestInvalidName(){
        try{
            Business businessAccount = new Business(
                    user.getId(),
                    "",
                    "some text",
                    address,
                    BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                    LocalDateTime.now(),
                    user
            );
        }catch (Exception e){
            assertEquals("Invalid business name.", e.getMessage());
        }
    }

    @Test
    public void TestInvalidAddress(){
        try{
            Business businessAccount = new Business(
                    user.getId(),
                    "name",
                    "some text",
                    null,
                    BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                    LocalDateTime.now(),
                    user
            );
        }catch (Exception e){
            assertEquals("Invalid address", e.getMessage());
        }
    }

    @Test
    public void TestOptionalFields() throws Exception {
        Business businessAccount = new Business(
                user.getId(),
                "name",
                "",
                address,
                BusinessType.ACCOMMODATION_AND_FOOD_SERVICES,
                LocalDateTime.now(),
                user
        );
        assertNull(businessAccount.getDescription());
    }

    /**
     * Test to see whether the list of administrators for a business are updated as well as the list of businesses
     * administered by a user are updated when a user becomes a new administrator for that business.
     * @throws Exception
     */
    @Test
    public void testAddAdministrators() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "bio",
                "test@example.com",
                LocalDate.of(2021, 1, 1).minusYears(13),
                "123456789",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 1, 1),
                        LocalTime.of(0, 0)),
                Role.USER);
        business.addAdministrators(user);
        assertEquals(business, user.getBusinessesAdministeredObjects().get(0));
    }

    /**
     * Test to see whether the list of administrators for a business are updated as well as the list of businesses
     * administered by a user are updated when a user is removed as an administrator for that business.
     * @throws Exception
     */
    @Test
    public void testRemoveAdministrators() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        business.removeAdministrators(user);

        assertThat(business.getAdministrators().isEmpty()).isTrue();
        assertThat(user.getBusinessesAdministeredObjects().isEmpty()).isTrue();
    }

    /**
     * Test to see when a user is one of administrator, the function return true
     * @throws Exception business or user creat fail
     */
    @Test
    public void testAnUserIsAdministrator() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        business.addAdministrators(user);
        assertTrue(business.isAnAdministratorOfThisBusiness(user));
    }

    /**
     * Test to see when a user is not one of administrator, the function return false
     * @throws Exception business or user creat fail
     */
    @Test
    public void testAnUserIsNotAdministrator() throws Exception {
        Business business = new Business(
                user.getId(),
                "name",
                "description",
                address,
                BusinessType.RETAIL_TRADE,
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                user
        );
        User user = new User("first",
                "last",
                "middle",
                "nick",
                "biography",
                "email@email.com",
                LocalDate.of(2020, 2, 2).minusYears(13),
                "0271316",
                address,
                "Password123!",
                LocalDateTime.of(LocalDate.of(2021, 2, 2),
                        LocalTime.of(0, 0)),
                Role.USER);

        assertFalse(business.isAnAdministratorOfThisBusiness(user));
    }
}
