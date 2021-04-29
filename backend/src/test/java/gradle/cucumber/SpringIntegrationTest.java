package gradle.cucumber;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.seng302.main.Main;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@CucumberContextConfiguration
@SpringBootTest(classes = {Main.class})
public class SpringIntegrationTest {
    // executeGet implementation
}
