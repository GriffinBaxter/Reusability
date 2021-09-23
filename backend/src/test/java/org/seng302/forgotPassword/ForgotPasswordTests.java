package org.seng302.forgotPassword;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seng302.exceptions.IllegalForgotPasswordArgumentException;
import org.seng302.model.ForgotPassword;


import static org.junit.jupiter.api.Assertions.assertThrows;

class ForgotPasswordTests {

    private ForgotPassword forgotPassword;

    @Test
    void validTokenTest() throws IllegalForgotPasswordArgumentException {
        forgotPassword = new ForgotPassword(1);
        Assertions.assertTrue(forgotPassword.isValidToken());
    }

    @Test
    void invalidUserIdTest() {
        Integer userId = -5;
        assertThrows(IllegalForgotPasswordArgumentException.class, () -> new ForgotPassword(userId));
    }

    @Test
    void nullUserIdTest() {
        Integer userId = null;
        assertThrows(IllegalForgotPasswordArgumentException.class, () -> new ForgotPassword(userId));
    }

    @Test
    void testValuesSetTest() throws IllegalForgotPasswordArgumentException {
        Integer userId = 1;
        forgotPassword = new ForgotPassword(userId);

        Assertions.assertEquals(1, forgotPassword.getUserId());
        Assertions.assertNotNull(forgotPassword.getExpiry());
        Assertions.assertNotNull(forgotPassword.getToken());
    }
}
