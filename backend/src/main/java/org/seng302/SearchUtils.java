package org.seng302;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains methods needed by the searches for users and businesses.
 */
public class SearchUtils {

    /**
     * Deconstructs the search query to get a list of names which will be used to search for businesses.
     *
     * @param searchQuery criteria to search for businesses (business name).
     * @return a list of business names that were represented by the searchQuery (the searchQuery can contain complex queries).
     *
     * Preconditons:  searchQuery is a string which can represent a complex query containing business names e.g.
     *                "New" AND World OR Count.
     * Postconditions: A list of names from the deconstructed searchQuery.
     */
    public static List<String> convertSearchQueryToNames(String searchQuery) {
        List<String> names = new ArrayList<>();
        if (searchQuery.equals("")) {
            names.add("");
            return names;
        }
        List<String> searchQueryList = Arrays.asList(searchQuery.split(" "));
        String concatName = "";
        String previousValue = "";
        String nextValue = "";
        boolean quoteStart = false;
        boolean quoteEnd = false;
        for (int i = 0; i < searchQueryList.size(); i++) {
            String currentName = searchQueryList.get(i);
            boolean endOfList = (i == (searchQueryList.size() - 1));
            if (i < (searchQueryList.size() - 1)) {
                nextValue = searchQueryList.get(i + 1);
            }

            if (currentName.startsWith("\"")) {
                quoteStart = true;
            }
            if (currentName.endsWith("\"")) {
                quoteEnd = true;
            }

            if (i == 0 && !quoteStart) {
                concatName = currentName;
            } else if (i == 0 && quoteStart) {
                concatName = currentName;
            } else if (quoteStart && !quoteEnd) {
                concatName += " " + currentName;
            }
            if (quoteEnd && i != 0) {
                concatName += " " + currentName;
                names.add(concatName);
                concatName = "";
                quoteStart = false;
                quoteEnd = false;
            }

            if (previousValue.equals("AND")) {
                concatName += " " + currentName;
            } else if (previousValue.equals("OR") && !(endOfList)) {
                if (!(nextValue.equals("AND"))) {
                    names.add(concatName);
                    names.add(currentName);
                    concatName = "";
                } else {
                    names.add(concatName);
                    concatName = "";
                    concatName += currentName;
                }
            }
            if (previousValue.equals("OR") && (endOfList)) {
                if (concatName.length() > 0) {
                    names.add(concatName);
                }
                names.add(currentName);
                concatName = "";
            } else if (endOfList) {
                if (concatName.length() > 0) {
                    names.add(concatName);
                }
                concatName = "";
            }
            previousValue = currentName;
        }
        return names;
    }
}
