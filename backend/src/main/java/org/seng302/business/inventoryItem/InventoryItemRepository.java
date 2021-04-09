package org.seng302.business.inventoryItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * InventoryItemRepository interface
 */
@RepositoryRestResource
public interface InventoryItemRepository extends JpaRepository<InventoryItem, String> {
    
}
