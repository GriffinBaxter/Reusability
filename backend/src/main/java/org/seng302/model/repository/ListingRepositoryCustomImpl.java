package org.seng302.model.repository;

import org.seng302.exceptions.FailedToDeleteListingException;
import org.seng302.exceptions.IllegalListingNotificationArgumentException;
import org.seng302.model.Listing;
import org.seng302.model.ListingNotification;
import org.seng302.model.enums.BusinessType;
import org.seng302.utils.CustomRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom Implementation of Listing Repository for searching
 */
public class ListingRepositoryCustomImpl implements ListingRepositoryCustom {

    @Autowired
    private EntityManager entityManager;


    private static final String BUSINESS_STRING = "business";

    private static final String INVENTORY_ITEM_STRING = "inventoryItem";

    private static final String PRODUCT_STRING = "product";

    private static final String CLOSES_STRING = "closes";

    /**
     * Search for listings by product name and optional filters.
     *
     * @param names        A list of product names.
     * @param pageable     A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @param businessTypes The types of businesses to search for. (Optional)
     * @param minimumPrice Lower end of prices to include in search. (Optional)
     * @param maximumPrice Higher end of prices to include in search. (Optional)
     * @param fromDate     Earlier end of close dates to include in search. (Optional)
     * @param toDate       Later end of close dates to include in search. (Optional)
     * @param barcode      The barcode to match to listings (Optional)
     * @return A Page object containing all matching listing results.
     *
     * Preconditions:  A non-null list of names to search for product names.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching listing results.
     */
    @Override
    public Page<Listing> findAllListingsByProductName(
            List<String> names, Pageable pageable,
            List<BusinessType> businessTypes,
            Double minimumPrice, Double maximumPrice,
            LocalDateTime fromDate, LocalDateTime toDate,
            String barcode
    ) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> query = criteriaBuilder.createQuery(Listing.class);
        Root<Listing> listing = query.from(Listing.class);

        Path<String> namePath = listing.get(INVENTORY_ITEM_STRING).get(PRODUCT_STRING).get("name");

        ArrayList<Predicate> predicates = CustomRepositoryUtils.getPredicates(names, namePath, criteriaBuilder);

        return getListings(pageable, businessTypes, minimumPrice, maximumPrice, fromDate, toDate, barcode, criteriaBuilder, query, listing, predicates);
    }

    /**
     * Search for listings by location and optional filters.
     *
     * @param locations    A list of locations
     * @param pageable     A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @param businessTypes The types of businesses to search for. (Optional)
     * @param minimumPrice Lower end of prices to include in search. (Optional)
     * @param maximumPrice Higher end of prices to include in search. (Optional)
     * @param fromDate     Earlier end of close dates to include in search. (Optional)
     * @param toDate       Later end of close dates to include in search. (Optional)
     * @param barcode      The barcode to match to listings (Optional)
     * @return A Page object containing all matching listing results.
     *
     * Preconditions:  A non-null list of locations to search for.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching listing results.
     */
    @Override
    public Page<Listing> findAllListingsByLocation(List<String> locations, Pageable pageable, List<BusinessType> businessTypes, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate, String barcode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> query = criteriaBuilder.createQuery(Listing.class);

        Root<Listing> listing = query.from(Listing.class);

        Path<String> addressPath = listing.get(INVENTORY_ITEM_STRING).get(PRODUCT_STRING).get(BUSINESS_STRING).get("address");

        ArrayList<Predicate> predicates = new ArrayList<>();
        for (String location : locations) {
            if (location.startsWith("\"") && location.endsWith("\"")) {
                location = location.replace("\"", "");
                predicates.add(criteriaBuilder.equal(addressPath.get("suburb"), location));
                predicates.add(criteriaBuilder.equal(addressPath.get("region"), location));
                predicates.add(criteriaBuilder.equal(addressPath.get("city"), location));
                predicates.add(criteriaBuilder.equal(addressPath.get("country"), location));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(addressPath.get("suburb")), "%" + location.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(addressPath.get("region")), "%" + location.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(addressPath.get("city")), "%" + location.toUpperCase() + "%"));
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(addressPath.get("country")), "%" + location.toUpperCase() + "%"));
            }
        }
        return getListings(pageable, businessTypes, minimumPrice, maximumPrice, fromDate, toDate, barcode, criteriaBuilder, query, listing, predicates);
    }

    /**
     * Search for listings by business name and optional filters.
     *
     * @param names        A list of business names.
     * @param pageable     A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @param businessTypes The types of businesses to search for. (Optional)
     * @param minimumPrice Lower end of prices to include in search. (Optional)
     * @param maximumPrice Higher end of prices to include in search. (Optional)
     * @param fromDate     Earlier end of close dates to include in search. (Optional)
     * @param toDate       Later end of close dates to include in search. (Optional)
     * @param barcode      The barcode to match to listings (Optional)
     * @return A Page object containing all matching listing results.
     *
     * Preconditions:  A non-null list of names to search for businesses.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching listing results.
     */
    @Override
    public Page<Listing> findAllListingsByBusinessName(List<String> names, Pageable pageable, List<BusinessType> businessTypes, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate, String barcode) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Listing> query = criteriaBuilder.createQuery(Listing.class);

        Root<Listing> listing = query.from(Listing.class);

        Path<String> businessNamePath = listing.get(INVENTORY_ITEM_STRING).get(PRODUCT_STRING).get(BUSINESS_STRING).get("name");

        ArrayList<Predicate> predicates = CustomRepositoryUtils.getPredicates(names, businessNamePath, criteriaBuilder);

        return getListings(pageable, businessTypes, minimumPrice, maximumPrice, fromDate, toDate, barcode, criteriaBuilder, query, listing, predicates);
    }

    /**
     * Gets the page of Listings from predicates and applies the optional filters.
     *
     * @param pageable Pageable for Pagination/Sorting
     * @param businessTypes Type of Businesses
     * @param minimumPrice Lower end of price range
     * @param maximumPrice Higher end of price range
     * @param fromDate Earlier date of close date range
     * @param toDate Later date of close date range
     * @param barcode A barcode to match the listings to
     * @param criteriaBuilder Criteria builder
     * @param query Query for Listings location
     * @param listing Root for listing location
     * @param predicates Predicates from searchQuery
     * @return A Page of Listings that apply to filters (can be empty)
     *
     * Preconditions:  predicates contains at least one Predicate
     *                 pageable is a valid Pageable
     *                 query and listing are for the same place
     * Postconditions: A matching Page of Listings
     */
    private Page<Listing> getListings(Pageable pageable, List<BusinessType> businessTypes, Double minimumPrice, Double maximumPrice, LocalDateTime fromDate, LocalDateTime toDate, String barcode, CriteriaBuilder criteriaBuilder, CriteriaQuery<Listing> query, Root<Listing> listing, ArrayList<Predicate> predicates) {

        // Optional filters
        ArrayList<Predicate> predicateList = new ArrayList<>();
        if (businessTypes != null) {
            // where businessType = type
            Predicate predicateForBusinessType = criteriaBuilder.isTrue(listing.get(INVENTORY_ITEM_STRING).get(PRODUCT_STRING).get(BUSINESS_STRING).get("businessType").in(businessTypes));
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
                    listing.get(CLOSES_STRING).as(LocalDateTime.class), fromDate
            );
            predicateList.add(predicateForFromDate);
        }
        if (toDate != null) {
            Predicate predicateForToDate = criteriaBuilder.lessThanOrEqualTo(
                    listing.get(CLOSES_STRING).as(LocalDateTime.class), toDate
            );
            predicateList.add(predicateForToDate);
        }
        if (barcode != null && !barcode.equals("")) {
            // where businessType = type
            Predicate predicateForBarcode = criteriaBuilder.equal(listing.get(INVENTORY_ITEM_STRING).get(PRODUCT_STRING).get("barcode"), barcode);
            predicateList.add(predicateForBarcode);
        }

        Predicate predicateExpireDate = criteriaBuilder.greaterThanOrEqualTo(
                listing.get(INVENTORY_ITEM_STRING).get("expires").as(LocalDateTime.class), LocalDateTime.now()
        );
        predicateList.add(predicateExpireDate);

        Predicate predicateForFromDate = criteriaBuilder.greaterThanOrEqualTo(
                listing.get(CLOSES_STRING).as(LocalDateTime.class), LocalDateTime.now()
        );
        predicateList.add(predicateForFromDate);


        predicateList.add(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        // the where clause of the query
        query.where(criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()])));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), listing, criteriaBuilder));

        // this query fetches the listings as per the page limit
        List<Listing> listings = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Listing> listingRootCount = countQuery.from(Listing.class);
        countQuery.select(criteriaBuilder.count(listingRootCount)).where(criteriaBuilder
                .and(predicateList.toArray(
                        new Predicate[predicateList.size()])),
                        criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))
        );

        // fetches the count of all listings as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(listings, pageable, count);
    }


    /**
     * Given a listing id attempt to delete it. And created a notification for all bookmarked users.
     *
     * @param id This is the id of the listing to be deleted if exists.
     * @return Returns true if succeeds.
     * @throws FailedToDeleteListingException Thrown when something goes wrong. The message will contain the details.
     */
    @Transactional
    public Boolean deleteListing(Integer id) throws FailedToDeleteListingException {
        // Try to get the listing
        Listing listing = entityManager.find(Listing.class, id);
        if (listing == null) {
            throw new FailedToDeleteListingException(String.format("Listing with id (%d). Does not exist.", id));
        }

        // Try to create a notification
        ListingNotification notification;
        try {
            notification = new ListingNotification(String.format("Listing for '%s' from business '%s' has been deleted. Sorry for the inconvenience.", listing.getInventoryItem().getProduct().getName(), listing.getInventoryItem().getProduct().getBusiness().getName()));
        } catch (IllegalListingNotificationArgumentException err) {
            String errMessage = String.format("Failed to create listing notification for listing (%d) delete.", id);
            throw new FailedToDeleteListingException(errMessage);
        }

        // Add the users to it.
        notification.setUsers(listing.getBookmarkedListings());

        // Attempt to save the changes
        try {
            entityManager.remove(listing);
            entityManager.persist(notification);
            entityManager.flush();
        } catch (Exception err) {
            throw new FailedToDeleteListingException(err.getMessage());
        }
        return true;
    }
}
