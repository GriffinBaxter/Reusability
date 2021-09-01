package org.seng302.model.repository;

import org.seng302.model.Product;
import org.seng302.utils.CustomRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    /**
     * Search for products for a business by selected fields
     * @param search list of values to search for
     * @param fields list of fields to search ("id", "name", "manufacturer", "description")
     * @param businessId id of business
     * @param pageable Pageable object
     * @return Page of Products by search criteria
     */
    @Override
    public Page<Product> findAllProductsByBusinessIdAndIncludedFields(List<String> search, List<String> fields, Integer businessId, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);

        Root<Product> product = query.from(Product.class);

        Path<String> path;
        ArrayList<Predicate> predicates = new ArrayList<>();

        // Valid fields are "id", "name", "manufacturer" & "description"
        for (String field : fields) {
            path = product.get(field);
            predicates.addAll(CustomRepositoryUtils.getPredicates(search, path, criteriaBuilder));
        }

        Path<String> businessPath = product.get("businessId");

        // the where clause of the query
        query.where(criteriaBuilder.and(criteriaBuilder.equal(businessPath, businessId), criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(pageable.getSort(), product, criteriaBuilder));

        // this query fetches the products as per the page limit
        List<Product> products = entityManager.createQuery(query).setFirstResult((int) pageable.getOffset()).setMaxResults(pageable.getPageSize()).getResultList();

        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<Product> productRootCount = countQuery.from(Product.class);
        countQuery.select(criteriaBuilder.count(productRootCount))
                .where(criteriaBuilder.and(criteriaBuilder.equal(businessPath, businessId),
                        criteriaBuilder.or(predicates.toArray(new Predicate[predicates.size()]))));

        // fetches the count of all products as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(products, pageable, count);
    }
}