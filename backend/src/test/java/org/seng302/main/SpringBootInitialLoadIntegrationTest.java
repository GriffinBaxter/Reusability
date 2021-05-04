package org.seng302.main;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@DataJpaTest
@ContextConfiguration(classes = {Main.class})
@Sql({"/data-h2.sql"})
public class SpringBootInitialLoadIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testLoadUserData() {
        assertEquals(9, userRepository.findAll().size());
    }
}