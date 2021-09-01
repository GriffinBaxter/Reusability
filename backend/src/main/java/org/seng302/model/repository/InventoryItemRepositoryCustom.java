package org.seng302.model.repository;

import org.seng302.model.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryItemRepositoryCustom {

    /**
     * Search for Inventory Items with a certain barcode for a business
     * @param barcode Barcode number
     * @param businessId Business Id
     * @param paging Pageable object
     * @return Page of Inventory items fitting criteria
     */
    Page<InventoryItem> findInventoryItemsByBarcodeAndBusinessId(String barcode, Integer businessId, Pageable paging);
}
