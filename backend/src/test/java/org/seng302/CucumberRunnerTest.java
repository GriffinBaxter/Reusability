package org.seng302;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Class which sets up the Cucumber options that are used when the tests are run.
 * The cucumber options are specified to format the test report for readability, to state the location of the tests'
 * steps and the associated features.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"}, // How to format test report, "pretty" is good for human eyes
        glue = {"org.seng302.steps"}, // Where to look for your tests' steps
        features = {"src/test/resources/features/"}, // Where to look for your features
        publish = true
)
public class CucumberRunnerTest {
}
