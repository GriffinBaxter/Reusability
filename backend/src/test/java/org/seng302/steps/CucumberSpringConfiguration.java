package org.seng302.steps;

import io.cucumber.spring.CucumberContextConfiguration;
import org.seng302.Main;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Class for configuring Cucumber and Spring to work together
 */
@CucumberContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = {Main.class})
public class CucumberSpringConfiguration {

}
