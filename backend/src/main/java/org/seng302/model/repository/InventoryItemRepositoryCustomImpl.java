package org.seng302.model.repository;

import org.seng302.model.InventoryItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.List;

public class InventoryItemRepositoryCustomImpl implements InventoryItemRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    /**
     * Search for Inventory Items with a certain barcode for a business
     * @param barcode Barcode number
     * @param businessId Business Id
     * @param paging Pageable object
     * @return Page of Inventory items fitting criteria
     */
    @Override
    public Page<InventoryItem> findInventoryItemsByBarcodeAndBusinessId(String barcode, Integer businessId, Pageable paging) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<InventoryItem> query = criteriaBuilder.createQuery(InventoryItem.class);

        Root<InventoryItem> inventoryItemRoot = query.from(InventoryItem.class);

        Path<String> barcodePath = inventoryItemRoot.get("product").get("barcode");
        Path<String> businessIdPath = inventoryItemRoot.get("businessId");

        // the where clause of the query
        query.where(criteriaBuilder.and(criteriaBuilder.equal(barcodePath, barcode), criteriaBuilder.equal(businessIdPath, businessId)));

        // the order by clause of the query
        query.orderBy(QueryUtils.toOrders(paging.getSort(), inventoryItemRoot, criteriaBuilder));

        // this query fetches the inventory items as per the page limit
        List<InventoryItem> inventoryItems = entityManager.createQuery(query).setFirstResult((int) paging.getOffset()).setMaxResults(paging.getPageSize()).getResultList();

        // create a count query used to display "Showing 1-5 of x results"
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<InventoryItem> inventoryItemRootCount = countQuery.from(InventoryItem.class);
        countQuery.select(criteriaBuilder.count(inventoryItemRootCount)).where(criteriaBuilder.and(criteriaBuilder.equal(barcodePath, barcode), criteriaBuilder.equal(businessIdPath, businessId)));

        // fetches the count of all inventory items as per given criteria
        Long count = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(inventoryItems, paging, count);
    }
}
