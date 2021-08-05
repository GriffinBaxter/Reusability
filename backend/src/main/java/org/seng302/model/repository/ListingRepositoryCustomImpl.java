package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.seng302.model.enums.BusinessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ListingRepositoryCustomImpl implements ListingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;
    
    
    @Override
    public Page<Listing> findAllListingsByProductName(
            List<String> names, Pageable pageable,
            BusinessType businessType,
            Double minimumPrice, Double maximumPrice,
            LocalDateTime fromDate, LocalDateTime toDate
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> query = criteriaBuilder.createQuery(Listing.class);

        Root<Listing> listing = query.from(Listing.class);

        Path<String> namePath = listing.get("inventoryItem").get("product").get("name");
        Path<String> businessTypePath = listing.get("inventoryItem").get("product").get("business").get("businessType");

        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(namePath, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(namePath), "%" + name.toUpperCase() + "%"));
            }
        }
        
        ArrayList<Predicate> predicateList = new ArrayList<>();
        
        predicateList.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
        
        if (businessType != null) {
            // where businessType = type
            Predicate predicateForBusinessType = criteriaBuilder.equal(businessTypePath, businessType);
            predicateList.add(predicateForBusinessType);
        }
        if (minimumPrice != null) {
            Predicate predicateForMinimumPrice = criteriaBuilder.greaterThanOrEqualTo(listing.get("price"), minimumPrice);
            predicateList.add(predicateForMinimumPrice);
        }
        if (maximumPrice != null) {
            Predicate predicateForMaximumPrice = criteriaBuilder.lessThanOrEqualTo(listing.get("price"), maximumPrice);
            predicateList.add(predicateForMaximumPrice);
        }
        if (fromDate != null) {
            Predicate predicateForFromDate = criteriaBuilder.greaterThanOrEqualTo(
                    listing.get("closes").as(LocalDateTime.class), fromDate
            );
            predicateList.add(predicateForFromDate);
        }
        if (toDate != null) {
            Predicate predicateForToDate = criteriaBuilder.lessThanOrEqualTo(
                    listing.get("closes").as(LocalDateTime.class), toDate
            );
            predicateList.add(predicateForToDate);
        }

        // the where clause of the query
        query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicates.size()])));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), listing, criteriaBuilder));

        // this query fetches the businesses as per the page limit
        List<Listing> listings = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();
        
        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Listing> listingRootCount = countQuery.from(Listing.class);
        countQuery.select(criteriaBuilder.count(listingRootCount)).where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));
        
        // fetches the count of all businesses as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        
        return new PageImpl<>(listings, pageable, count);

    }

    @Override
    public Page<Listing> findAllListingsByLocation(List<String> names, Pageable pageable, BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate) {
        return null;
    }

    @Override
    public Page<Listing> findAllListingsByBusinessName(List<String> names, Pageable pageable, BusinessType businessType, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate) {
        return null;
    }
}