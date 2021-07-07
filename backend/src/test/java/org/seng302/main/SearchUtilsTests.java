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
     * Test to see whether searchQuery is parsed into a list containing one name when the searchQuery contains an AND
     * statement.
     */
    @Test
    void convertSearchToNamesWithOneAnd_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Brink AND Food";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Brink Food");
        Assertions.assertEquals(expectedNames, names);
    }

    // one OR
    /**
     * Test to see whether searchQuery is parsed into a list containing the two separate names when the searchQuery
     * contains an OR statement.
     */
    @Test
    void convertSearchToNamesWithOneOr_ExpectListContainingSeparateWordsTest() {
        String searchQuery = "Plant OR House";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Plant", "House");
        Assertions.assertEquals(expectedNames, names);
    }

    // three ANDs, no ORs
    /**
     * Test to see whether searchQuery is parsed into a list containing the one name when the searchQuery
     * contains three ANDs and no ORs.
     */
    @Test
    void convertSearchToNamesWithThreeAndsAndNoOrs_ExpectListContainingConcatenatedWordTest() {
        String searchQuery = "Big AND Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    // three ORs, no ANDs
    /**
     * Test to see whether searchQuery is parsed into a list containing the one name when the searchQuery
     * contains three ANDs and no ORs.
     */
    @Test
    void convertSearchToNamesWithThreeOrsAndNoAnds_ExpectListContainingSeparateWordsTest() {
        String searchQuery = "Big OR Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    // one AND, then one OR
    /**
     * Test to see whether searchQuery is parsed into a list containing the first two words combined and the last word
     * separate when the searchQuery contains two words with AND between them, followed by OR and another word.
     */
    @Test
    void convertSearchToNamesWithOneAndAndOneOr_ExpectListContainingFirstTwoWordsConcatenatedAndLastWordSeparateTest() {
        String searchQuery = "Big AND Red OR Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Red", "Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    // one OR, then one AND
    /**
     * Test to see whether searchQuery is parsed into a list containing the first word and the last two words combined
     * when the searchQuery contains two words with OR between them, followed by AND and another word.
     */
    @Test
    void convertSearchToNamesWithOneOrAndOneAnd_ExpectListContainingFirstWordSeparateAndLastTwoWordsConcatenatedTest() {
        String searchQuery = "Big OR Red AND Apple";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "Red Apple");
        Assertions.assertEquals(expectedNames, names);
    }

    // one exact match with one word
    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word in the query string that has the
     * same case.
     */
    @Test
    void convertSearchToNameWithExactMatchOneWord_ExpectListContainingExactWordTest() {
        String searchQuery = "\"Big\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // one exact match with three words
    /**
     * Test to see whether searchQuery is parsed into a list containing the exact words in the query string that have the
     * same case.
     */
    @Test
    void convertSearchToNameWithExactMatchThreeWords_ExpectListContainingExactWordsTest() {
        String searchQuery = "\"Big Red Apple\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big Red Apple\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // one exact match, followed by AND and non-exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word concatenated with the
     * case-insensitive-word when the search string is an exact word followed by AND then a non-exact word.
     */
    @Test
    void convertSearchToNameWithExactMatchAndNonExactMatch_ExpectListContainingOneWordOfExactConcatenatedWithNonExactWordTest() {
        String searchQuery = "\"Big\" AND Red";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\" Red");
        Assertions.assertEquals(expectedNames, names);
    }

    // non-exact match, followed by AND and exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing the case-insensitive-word concatenated with the
     * exact match word when the search string is a non-exact word followed by AND then an exact word.
     */
    @Test
    void convertSearchToNameWithNonExactMatchAndExactMatch_ExpectListContainingOneWordOfNonExactConcatenatedWithExactWordTest() {
        String searchQuery = "Big AND \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big \"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // one exact match, followed by OR and non-exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing the exact word separate from the
     * case-insensitive-word when the search string is an exact word followed by OR then a non-exact word.
     */
    @Test
    void convertSearchToNameWithExactMatchOrNonExactMatch_ExpectListContainingTwoSeparateWordsTest() {
        String searchQuery = "\"Big\" OR Red";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\"", "Red");
        Assertions.assertEquals(expectedNames, names);
    }

    // non-exact match, followed by OR and exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing the case-insensitive-word separate from the
     * exact match word when the search string is a non-exact word followed by OR then an exact word.
     */
    @Test
    void convertSearchToNameWithNonExactMatchOrExactMatch_ExpectListContainingTwoSeparateWordsTest() {
        String searchQuery = "Big OR \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big", "\"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // no ANDs and ORs, and non-exact match single word
    /**
     * Test to see whether searchQuery is parsed into a list containing the case-insensitive-word when the search string
     * is a non-exact word.
     */
    @Test
    void convertSearchToNameWithNonExactMatch_ExpectListContainingWordTest() {
        String searchQuery = "Big";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big");
        Assertions.assertEquals(expectedNames, names);
    }

    // no ANDs and ORs, and non-exact match two words
    /**
     * Test to see whether searchQuery is parsed into a list containing two case-insensitive words when the search string
     * is two non-exact words. It is assumed that there is an AND separating these then.
     */
    @Test
    void convertSearchToNameWithTwoNonExactMatchWords_ExpectListContainingCombinedWordTest() {
        String searchQuery = "Big Red";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("Big Red");
        Assertions.assertEquals(expectedNames, names);
    }

    // exact match, OR, exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing two exact match words when the search string
     * is two exact words separated by OR.
     */
    @Test
    void convertSearchToNameWithOneExactMatchOrOneExactMatch_ExpectListContainingBothExactMatchWordsTest() {
        String searchQuery = "\"Big\" OR \"Red\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big\"", "\"Red\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // exact match, AND, exact match
    /**
     * Test to see whether searchQuery is parsed into a list containing combined exact match words when the search string
     * is two exact words separated by AND.
     */
    @Test
    void convertSearchToNameWithOneExactMatchAndOneExactMatch_ExpectListContainingCombinedExactMatchWordsTest() {
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
    void convertSearchToNameOneExactMatchPhraseContainingOr_ExpectListContainingPhraseTest() {
        String searchQuery = "\"Big OR\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big OR\"");
        Assertions.assertEquals(expectedNames, names);
    }

    // exact match containing AND
    /**
     * Test to see whether searchQuery is parsed into a list containing exact match phrase when the search string
     * is one exact match phrase contained a word followed by AND. Note that AND is within the "".
     */
    @Test
    void convertSearchToNameOneExactMatchPhraseContainingAnd_ExpectListContainingPhraseTest() {
        String searchQuery = "\"Big AND\"";
        List<String> names = SearchUtils.convertSearchQueryToNames(searchQuery);
        List<String> expectedNames = Arrays.asList("\"Big AND\"");
        Assertions.assertEquals(expectedNames, names);
    }

}
