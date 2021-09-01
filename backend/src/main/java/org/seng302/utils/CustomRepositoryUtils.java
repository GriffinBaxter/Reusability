package org.seng302.utils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class contains methods needed by the Custom Repository Classes
 */
public class CustomRepositoryUtils {

    private CustomRepositoryUtils() {
        // not called
    }

    /**
     * Gets Predicates that contain only one field (ie Product name).
     *
     * @param names list of names to search
     * @param path path to required field
     * @param criteriaBuilder CriteriaBuilder object
     * @return ArrayList of Predicates
     *
     * Preconditions: path is valid
     *                names is not empty
     * Postconditions: List of predicates
     */
    public static ArrayList<Predicate> getPredicates(List<String> names, Path<String> path, CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(path, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(path), "%" + name.toUpperCase() + "%"));
            }
        }
        return predicates;
    }

}
