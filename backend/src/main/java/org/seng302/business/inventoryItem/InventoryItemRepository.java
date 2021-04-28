package org.seng302.business.inventoryItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
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

}
