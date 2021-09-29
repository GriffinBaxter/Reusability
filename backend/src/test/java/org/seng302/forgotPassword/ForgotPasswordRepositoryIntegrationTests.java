package org.seng302.forgotPassword;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seng302.Main;
import org.seng302.model.ForgotPassword;
import org.seng302.model.repository.ForgotPasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ForgotPasswordRepository test class.
 */
@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@ActiveProfiles("test")
class ForgotPasswordRepositoryIntegrationTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;

    private ForgotPassword forgotPassword;

    @BeforeEach
    void beforeEach() throws Exception {
        forgotPassword = new ForgotPassword(1);
        entityManager.persist(forgotPassword);
        entityManager.flush();
    }

    /**
     * Tests the findByToken method when the token exists in the database
     * Returns ForgotPassword Object
     */
    @Test
    void testFindByToken_WhenValidToken_ReturnsForgetPassword() {
        Optional<ForgotPassword> foundForgotPassword = forgotPasswordRepository.findByToken(forgotPassword.getToken());

        assertThat(foundForgotPassword).isPresent();
    }

    /**
     * Tests the findByToken method when the token doesn't exist in the database
     * Returns empty Optional
     */
    @Test
    void testFindByToken_WhenInvalidToken_ReturnsEmptyOptional() {
        Optional<ForgotPassword> foundForgotPassword = forgotPasswordRepository.findByToken("123");

        assertThat(foundForgotPassword).isNotPresent();
    }
}
