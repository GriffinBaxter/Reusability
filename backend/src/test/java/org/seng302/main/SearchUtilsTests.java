package org.seng302.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seng302.SearchUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Tests for the SearchUtils class.
 */
public class SearchUtilsTests {

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery contains an AND statement.
     */
    @Test
    void convertSearchToNamesWithOneAndTest() {
        String searchQuery = "Brink AND Food";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Brink Food");
        Assertions.assertEquals(expectedNames, names);
    }
}
