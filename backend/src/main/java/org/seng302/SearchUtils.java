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
        System.out.println(searchQueryList);
        String currentName = searchQueryList.get(0);
        String previousOperator = "";
        for (int i = 0; i < searchQueryList.size(); i++) {
            String name = searchQueryList.get(i);
            if (previousOperator.equals("AND")) {
                currentName += " " + name;
            }
            if (previousOperator.equals("OR")) {
                names.add(currentName);
                names.add(name);
                currentName = "";
            }
            if (previousOperator != "AND" && previousOperator != "OR") {
                currentName += " " + name;
            }
            if (i == (searchQueryList.size() - 1)) {
                names.add(currentName);
            }
            previousOperator = name;
        }
        return names;
    }
}
