package org.seng302.model.repository;

import org.seng302.model.Listing;
import org.seng302.model.Product;
import org.seng302.utils.CustomRepositoryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

import org.seng302.utils.CustomRepositoryUtils;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<Product> findAllProductsBySingleField(List<String> names, Integer businessId, String field, Pageable pageable) throws Exception {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);

        Root<Product> product = query.from(Product.class);

        Path<String> path;
        switch (field) {
            case "productId":
                path = product.get("id");
                break;
            case "name":
                path = product.get("name");
                break;
            case "manufacturer":
                path = product.get("manufacturer");
                break;
            case "description":
                path = product.get("description");
                break;
            default:    // field parameter is invalid
                throw new Exception("Invalid field");
        }

        ArrayList<Predicate> predicates = CustomRepositoryUtils.getPredicates(names, path, criteriaBuilder);

        return getProducts(predicates, businessId, pageable, product, query, criteriaBuilder);
    }

    @Override
    public Page<Product> findAllProductsByAllFields(List<String> names, Integer businessId, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = criteriaBuilder.createQuery(Product.class);

        Root<Product> product = query.from(Product.class);

        Path<String> path = product.get("id");
        ArrayList<Predicate> predicates = CustomRepositoryUtils.getPredicates(names, path, criteriaBuilder);

        path = product.get("name");
        predicates.addAll(CustomRepositoryUtils.getPredicates(names, path, criteriaBuilder));

        path = product.get("manufacturer");
        predicates.addAll(CustomRepositoryUtils.getPredicates(names, path, criteriaBuilder));

        path = product.get("description");
        predicates.addAll(CustomRepositoryUtils.getPredicates(names, path, criteriaBuilder));

        return getProducts(predicates, businessId, pageable, product, query, criteriaBuilder);
    }

    private Page<Product> getProducts(ArrayList<Predicate> predicates, Integer businessId, Pageable pageable, Root<Product> product, CriteriaQuery<Product> query, CriteriaBuilder criteriaBuilder) {

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