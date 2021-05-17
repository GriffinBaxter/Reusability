package org.seng302.marketplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.address.AddressRepository;
import org.seng302.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketplaceCardResource {

    @Autowired
    private MarketplaceCardRepository cardRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LogManager.getLogger(MarketplaceCardResource.class.getName());

    public MarketplaceCardResource(
             MarketplaceCardRepository cardRepository, UserRepository userRepository, AddressRepository addressRepository
    ) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }
}
