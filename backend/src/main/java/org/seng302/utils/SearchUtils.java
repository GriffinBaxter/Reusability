package org.seng302.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains methods needed by the searches for users and businesses.
 */
public class SearchUtils {

    private SearchUtils() {
        // not called
    }

    /**
     * Parses the search query to get a list of names which will be used to search for businesses.
     *
     * @param searchQuery criteria to search for businesses (business name).
     * @return a list of business names that were represented by the searchQuery (the searchQuery can contain AND, OR
     * and "" operators).
     *
     * Preconditions:  searchQuery is a string which can represent a query containing business names e.g.
     *                 New World OR Countdown
     * Postconditions: A list of names from the parsed searchQuery.
     */
    public static List<String> convertSearchQueryToNames(String searchQuery) {
        List<String> names = new ArrayList<>();

        // If search query is empty then all businesses should be be returned.
        // This done by using LIKE(%"empty string"%) in the where clause of the query which retrieves the businesses.
        if (searchQuery.equals("")) {
            names.add("");
            return names;
        }

        // A list of words/operators present in the search query.
        List<String> tokens = Arrays.asList(searchQuery.split(" "));

        // If there is a space or AND operator between two words in the search query then
        // the words should be concatenated.
        String concatName = "";

        String previousToken = "";

        for (int i = 0; i < tokens.size(); i++) {

            String currentToken = tokens.get(i);

            // Conditions
            boolean endOfTokens = (i == (tokens.size() - 1));
            boolean startOfTokens = (i == 0);
            boolean andOperator = (previousToken.equalsIgnoreCase("AND"));
            boolean orOperator = (previousToken.equalsIgnoreCase("OR"));

            // 1. First token is added to concatenated name.
            // 2. If words are separated by AND operator then words should be concatenated.
            // 3. If previous token is an OR then the currently concatenated name is added to names and set to current token.
            // 4. If previous token is another word then the words should be concatenated since there is no logical operator.
            if (startOfTokens) {
                concatName = currentToken;
            } else if (andOperator) {
                concatName += " " + currentToken;
            } else if (orOperator) {
                names.add(concatName);
                concatName = currentToken;
            } else if (!(currentToken.equalsIgnoreCase("AND")) && !(currentToken.equalsIgnoreCase("OR"))) {
                concatName += " " + currentToken;
            }

            // If all tokens have be viewed then add the current contacted name to names.
            if (endOfTokens && (concatName.length() > 0)) {
                names.add(concatName);
            }
            previousToken = currentToken;
        }
        return names;
    }
}
