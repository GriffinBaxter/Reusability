package org.seng302.model.repository;

import org.seng302.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {

    /**
     * Search for products for a business by selected fields
     * @param search list of values to search for
     * @param fields list of fields to search ("id", "name", "manufacturer", "description")
     * @param businessId id of business
     * @param pageable Pageable object
     * @return Page of Products by search criteria
     */
    Page<Product> findAllProductsByBusinessIdAndIncludedFields(List<String> search, List<String> fields, Integer businessId, Pageable pageable);
}
