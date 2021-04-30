package org.seng302.business.inventoryItem;

import org.seng302.business.BusinessRepository;
import org.seng302.business.product.ProductRepository;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * InventoryItemResource class
 */
@RestController
public class InventoryItemResource {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Constructor used to insert mocked repositories for testing.
     *
     * @param inventoryItemRepository InventoryItemRepository
     * @param productRepository ProductRepository
     * @param businessRepository BusinessRepository
     * @param userRepository UserRepository
     */
    public InventoryItemResource(InventoryItemRepository inventoryItemRepository,
                                ProductRepository productRepository,
                                BusinessRepository businessRepository,
                                UserRepository userRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
        this.productRepository = productRepository;
        this.businessRepository = businessRepository;
        this.userRepository = userRepository;
    }

    public static InventoryPayload convertToPayload(InventoryItem item){
        return new InventoryPayload();
    }

}
