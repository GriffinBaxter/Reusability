package org.seng302.main;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.seng302.Authorization;
import org.seng302.model.Address;
import org.seng302.model.enums.Role;
import org.seng302.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.Assert.*;

/**
 * Authorization Test Class.
 * This class contains tests for the method isGAAorDGAA()
 */
class AuthorizationTests {

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
    }

    /**
     * Test that when a user has the role user the method isGAAorDGAA returns false.
     * @throws Exception thrown when a new user does not contain valid info.
     */
    @Test
    void testIsGAAOrDGAAReturnsFalseWhenUserHasRoleUser() throws Exception {
        user = new User("Thomas",
                "Wayne",
                "Gabriel",
                "Wayno",
                "Father of Bruce Wayne.",
                "wayno@email.com",
                LocalDate.of(2021, 05, 20).minusYears(65),
                "0275431316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 05, 20),
                        LocalTime.of(0, 0)),
                Role.USER);
        assertFalse(Authorization.isGAAorDGAA((user)));
    }

    /**
     * Test that when a user has the role GAA the method isGAAorDGAA returns true.
     * @throws Exception thrown when a new user does not contain valid info.
     */
    @Test
    void testIsGAAOrDGAAReturnsTrueWhenUserHasRoleGAA() throws Exception {
        user = new User("Thomas",
                "Wayne",
                "Gabriel",
                "Wayno",
                "Father of Bruce Wayne.",
                "wayno@email.com",
                LocalDate.of(2021, 05, 20).minusYears(65),
                "0275431316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 05, 20),
                        LocalTime.of(0, 0)),
                Role.GLOBALAPPLICATIONADMIN);
        assertTrue(Authorization.isGAAorDGAA((user)));
    }

    /**
     * Test that when a user has the role DGAA the method isGAAorDGAA returns true.
     * @throws Exception thrown when a new user does not contain valid info.
     */
    @Test
    void testIsGAAOrDGAAReturnsTrueWhenUserHasRoleDGAA() throws Exception {
        user = new User("Thomas",
                "Wayne",
                "Gabriel",
                "Wayno",
                "Father of Bruce Wayne.",
                "wayno@email.com",
                LocalDate.of(2021, 05, 20).minusYears(65),
                "0275431316",
                address,
                "Testpassword123!",
                LocalDateTime.of(LocalDate.of(2021, 05, 20),
                        LocalTime.of(0, 0)),
                Role.DEFAULTGLOBALAPPLICATIONADMIN);
        assertTrue(Authorization.isGAAorDGAA((user)));
    }
}
