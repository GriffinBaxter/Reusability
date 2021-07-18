package org.seng302.main;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.seng302.utils.SearchUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Tests for the SearchUtils class.
 * This includes tests for the parser which converts the Search Query to a List of names.
 */
class SearchUtilsTests {

    /* ---------------------- AND's --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery contains an AND
     * statement.
     */
    @Test
    void convertSearchQueryToNamesWithOneAndTest_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Brink AND Food";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Brink Food");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains two ANDs.
     */
    @Test
    void convertSearchQueryToNamesWithTwoAnds_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big AND Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Big Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains three ANDs.
     */
    @Test
    void convertSearchQueryToNamesWithThreeAnds_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big AND Crunchy AND Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Big Crunchy Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- No Logical Operator (Space) --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery contains one word.
     */
    @Test
    void convertSearchQueryToNamesWithOneWordTest_ExpectListContainingOneWordTest() {
        String searchQuery = "Brink";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Brink");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains two words separated by a space (no logical operator).
     */
    @Test
    void convertSearchQueryToNamesWithTwoWords_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Brink Food";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Brink Food");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains three words separated by spaces (no logical operator).
     */
    @Test
    void convertSearchQueryToNamesWithWords_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big Red Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Big Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- Mix of AND's and No Logical Operators --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains an AND and no logical operator (a space).
     */
    @Test
    void convertSearchQueryToNamesWithOneAndAndNoLogicalOperator_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big AND Red Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Big Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery
     * contains two ANDs and no logical operator (a space).
     */
    @Test
    void convertSearchQueryToNamesWithTwoAndsAndNoLogicalOperator_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big Crunchy AND Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("Big Crunchy Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- OR's --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing the two separate names when the searchQuery
     * contains an OR statement.
     */
    @Test
    void convertSearchQueryToNamesWithOneOr_ExpectListContainingSeparateWordsTest() {
        String searchQuery = "Plant OR House";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Plant", "House");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the three separate names when the searchQuery
     * contains two OR's.
     */
    @Test
    void convertSearchQueryToNamesWithTwoOrs_ExpectListContainingSeparateWordsTest() {
        String searchQuery = "Big OR Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the four separate names when the searchQuery
     * contains three OR's.
     */
    @Test
    void convertSearchQueryToNamesWithThreeOrs_ExpectListContainingSeparateWordsTest() {
        String searchQuery = "Big OR Crunchy OR Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Crunchy", "Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- Mix of OR's and No Logical Operators --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing two names when the searchQuery
     * contains an OR and no logical operator (a space).
     */
    @Test
    void convertSearchQueryToNamesWithOneOrAndNoLogicalOperator_ExpectListContainingTwoWordsTest() {
        String searchQuery = "Big OR Red Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing three names when the searchQuery
     * contains two OR's and no logical operator (a space).
     */
    @Test
    void convertSearchQueryToNamesWithTwoOrsAndNoLogicalOperator_ExpectListContainingThreeWordsTest() {
        String searchQuery = "Big Crunchy OR Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Crunchy", "Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- Mix of OR's and AND's --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing the first two words combined and the last word
     * separate when the searchQuery contains two words with AND between them, followed by OR and another word.
     */
    @Test
    void convertSearchQueryToNamesWithOneAndAndOneOr_ExpectListContainingFirstTwoWordsConcatenatedAndLastWordSeparateTest() {
        String searchQuery = "Big AND Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the first word and the last two words combined
     * when the searchQuery contains two words with OR between them, followed by AND and another word.
     */
    @Test
    void convertSearchQueryToNamesWithOneOrAndOneAnd_ExpectListContainingFirstWordSeparateAndLastTwoWordsConcatenatedTest() {
        String searchQuery = "Big OR Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the first two words combined and the last two words combined
     * when the searchQuery contains two words with AND between them, followed by OR and another two word with AND
     * between them.
     */
    @Test
    void convertSearchQueryToNamesWithAndThenOrThenAnd_ExpectListContainingTwoWordsSeparatedTest() {
        String searchQuery = "Big AND Crunchy OR Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Crunchy", "Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    /* ---------------------- Exact Matches --------------------- */

    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word in the query string that has the
     * same case.
     */
    @Test
    void convertSearchQueryToNameWithExactMatchOneWord_ExpectListContainingExactWordTest() {
        String searchQuery = "\"Big\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("\"Big\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the exact words in the query string that have the
     * same case.
     */
    @Test
    void convertSearchQueryToNameWithExactMatchThreeWords_ExpectListContainingExactWordsTest() {
        String searchQuery = "\"Big Red Apple\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = new ArrayList<>();
        expectedNames.add("\"Big Red Apple\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word concatenated with the
     * case-insensitive-word when the search string is an exact word followed by AND then a non-exact word.
     */
    @Test
    void convertSearchQueryToNameWithExactMatchAndNonExactMatch_ExpectListContainingOneWordOfExactConcatenatedWithNonExactWordTest() {
        String searchQuery = "\"Big\" AND Red";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\" Red");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the case-insensitive-word concatenated with the
     * exact match word when the search string is a non-exact word followed by AND then an exact word.
     */
    @Test
    void convertSearchQueryToNameWithNonExactMatchAndExactMatch_ExpectListContainingOneWordOfNonExactConcatenatedWithExactWordTest() {
        String searchQuery = "Big AND \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big \"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word separate from the
     * case-insensitive-word when the search string is an exact word followed by OR then a non-exact word.
     */
    @Test
    void convertSearchQueryToNameWithExactMatchOrNonExactMatch_ExpectListContainingTwoSeparateWordsTest() {
        String searchQuery = "\"Big\" OR Red";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\"", "Red");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing the case-insensitive-word separate from the
     * exact match word when the search string is a non-exact word followed by OR then an exact word.
     */
    @Test
    void convertSearchQueryToNameWithNonExactMatchOrExactMatch_ExpectListContainingTwoSeparateWordsTest() {
        String searchQuery = "Big OR \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "\"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing two exact match words when the search string
     * is two exact words separated by OR.
     */
    @Test
    void convertSearchQueryToNameWithOneExactMatchOrOneExactMatch_ExpectListContainingBothExactMatchWordsTest() {
        String searchQuery = "\"Big\" OR \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\"", "\"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing combined exact match words when the search string
     * is two exact words separated by AND.
     */
    @Test
    void convertSearchQueryToNameWithOneExactMatchAndOneExactMatch_ExpectListContainingCombinedExactMatchWordsTest() {
        String searchQuery = "\"Big\" AND \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\" \"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // exact match containing OR
    /**
     * Test to see whether searchQuery is parsed into a list containing exact match phrase when the search string
     * is one exact match phrase contained a word followed by OR. Note that OR is within the "".
     */
    @Test
    void convertSearchQueryToNameOneExactMatchPhraseContainingOr_ExpectListContainingPhraseTest() {
        String searchQuery = "\"Big OR\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big OR\"");
        Assertions.assertEquals(expectedNames, names);
    }

    /**
     * Test to see whether searchQuery is parsed into a list containing exact match phrase when the search string
     * is one exact match phrase contained a word followed by AND. Note that AND is within the "".
     */
    @Test
    void convertSearchQueryToNameOneExactMatchPhraseContainingAnd_ExpectListContainingPhraseTest() {
        String searchQuery = "\"Big AND\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big AND\"");
        Assertions.assertEquals(expectedNames, names);
    }

}
