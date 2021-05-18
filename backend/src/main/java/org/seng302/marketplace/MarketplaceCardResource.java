package org.seng302.marketplace;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.business.BusinessResource;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketplaceCardResource {

    // TODO autowire the needed repositories, when implementing endpoints.
    // TODO create constructor for MarketplaceCardResource with Autowired repositories.

    private static final Logger logger = LogManager.getLogger(BusinessResource.class.getName());
}
