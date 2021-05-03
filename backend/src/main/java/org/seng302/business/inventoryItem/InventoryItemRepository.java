package org.seng302.business.inventoryItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

/**
 * InventoryItemRepository interface
 */
@RepositoryRestResource
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {

    /**
     * Search for an InventoryItem by its id
     * @param id id
     * @return InventoryItem object if exists
     */
    Optional<InventoryItem> findInventoryItemById(Integer id);

    Optional<InventoryItem> findInventoryItemByProductId(String productId);

    /**
     * Finds all product inventory items with the given business ID.
     *
     * @param businessId A business ID.
     * @return A list of product inventory items with the given business ID.
     */
    Page<InventoryItem> findInventoryItemsByBusinessId(Integer businessId, Pageable paging);

    List<InventoryItem> findAllByBusinessId(Integer businessId);



}
