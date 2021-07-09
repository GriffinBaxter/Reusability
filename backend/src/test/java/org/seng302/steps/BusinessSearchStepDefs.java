package org.seng302.steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

public class BusinessSearchStepDefs extends CucumberSpringConfiguration {

    @Autowired
    private MockMvc mvc;


}
