package org.seng302.model.repository;

import org.seng302.model.InventoryItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface InventoryItemRepositoryCustom {

    Page<InventoryItem> findInventoryItemsByBarcodeAndBusinessId(String barcode, Integer businessId, Pageable paging);
}
