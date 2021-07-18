package org.seng302.model.repository;

import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BusinessRepositoryCustom {

    /**
     * Search for businesses by business names.
     * @param names A list of business names.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching business results.
     *
     * Preconditions:  A non-null list of names to search for businesses.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching business results.
     */
    Page<Business> findAllBusinessesByNames(List<String> names, Pageable pageable);

    /**
     * Search for businesses by business names and business type.
     * @param names A list of business names.
     * @param businessType The type of a business to search for.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching business results.
     *
     * Preconditions:  A non-null list of names to search for businesses.
     *                 A non-null business type that has been converted from type String to type BusinessType.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching business results.
     */
    Page<Business> findAllBusinessesByNamesAndType(List<String> names, BusinessType businessType, Pageable pageable);
}


