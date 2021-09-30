package org.seng302.model.repository;

import org.seng302.model.Business;
import org.seng302.model.User;
import org.seng302.model.enums.BusinessType;
import org.seng302.utils.CustomRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class BusinessRepositoryCustomImpl implements BusinessRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    /**
     * Search for businesses by business names.
     *
     * @param names    A list of business names.
     * @param pageable A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching business results.
     *
     * Preconditions:  A non-null list of names to search for businesses.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching business results.
     */
    @Override
    public Page<Business> findAllBusinessesByNames(List<String> names, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Business> query = criteriaBuilder.createQuery(Business.class);

        Root<Business> business = query.from(Business.class);

        Path<String> namePath = business.get("name");

        List<Predicate> predicates =  CustomRepositoryUtils.getPredicates(names, namePath, criteriaBuilder);

        // the where clause of the query
        query.where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), business, criteriaBuilder));

        // this query fetches the businesses as per the page limit
        List<Business> businesses = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Business> businessRootCount = countQuery.from(Business.class);
        countQuery.select(criteriaBuilder.count(businessRootCount)).where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        // fetches the count of all businesses as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(businesses, pageable, count);

    }

    /**
     * Search for businesses by business names and business type.
     *
     * @param names        A list of business names.
     * @param businessType The type of a business to search for.
     * @param pageable     A pageable object containing the requested page number, the number of results in a page and a sort object.
     * @return A Page object containing all matching business results.
     *
     * Preconditions:  A non-null list of names to search for businesses.
     *                 A non-null business type that has been converted from type String to type BusinessType.
     *                 A non-null pageable object.
     * Postconditions: A page object containing all matching business results.
     */
    @Override
    public Page<Business> findAllBusinessesByNamesAndType(List<String> names, BusinessType businessType, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Business> query = criteriaBuilder.createQuery(Business.class);

        Root<Business> business = query.from(Business.class);

        Path<String> namePath = business.get("name");

        List<Predicate> predicates =  CustomRepositoryUtils.getPredicates(names, namePath, criteriaBuilder);

        // where businessType = type
        Predicate predicateForBusinessType = criteriaBuilder.equal(business.get("businessType"), businessType);

        // the rest of the where clause (names)
        query.where(criteriaBuilder.and(predicateForBusinessType, criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), business, criteriaBuilder));

        // this query fetches the businesses as per the page limit
        List<Business> businesses = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Business> businessRootCount = countQuery.from(Business.class);
        countQuery.select(criteriaBuilder.count(businessRootCount)).where(criteriaBuilder.and(predicateForBusinessType, criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));

        // fetches the count of all businesses as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(businesses, pageable, count);
    }
}