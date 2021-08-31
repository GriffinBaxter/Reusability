package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.seng302.model.Product;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface ProductRepositoryCustom {

    Page<Product> findAllProductsBySingleField(List<String> names, Integer businessId, String field, Pageable pageable) throws Exception;

    Page<Product> findAllProductsByAllFields(List<String> names, Integer businessId, Pageable pageable);
}
