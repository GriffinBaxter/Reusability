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
        for (int i = 0; i < searchQueryList.size(); i++) {
            String currentName = searchQueryList.get(i);
            if (i == 0) {
                concatName = currentName;
            } else if (previousValue.equals("AND")) {
                concatName += " " + currentName;
            }
            if (i == searchQueryList.size() - 1) {
                names.add(concatName);
            }
            previousValue = currentName;
        }
        return names;
    }
}
