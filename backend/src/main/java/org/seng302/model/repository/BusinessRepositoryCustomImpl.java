package org.seng302.model.repository;

import org.seng302.model.Business;
import org.seng302.model.enums.BusinessType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

public class BusinessRepositoryCustomImpl implements BusinessRepositoryCustom {

    @PersistenceContext
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

        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(namePath, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(namePath), "%" + name.toUpperCase() + "%"));
            }
        }
        query.select(business)
                .where(criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()])));

        List<Business> businesses = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(businesses, pageable, businesses.size());
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

        List<Predicate> predicates = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                name = name.replaceAll("^\"+|\"+$", ""); // Remove quotations.
                predicates.add(criteriaBuilder.equal(namePath, name));
            } else {
                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(namePath), "%" + name.toUpperCase() + "%"));
            }
        }

        Predicate predicateForBusinessType
                = criteriaBuilder.equal(business.get("businessType"), businessType);

        query.select(business)
                .where(criteriaBuilder.and(predicateForBusinessType, criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));

        List<Business> businesses = entityManager.createQuery(query).getResultList();
        return new PageImpl<>(businesses, pageable, businesses.size());
    }
}