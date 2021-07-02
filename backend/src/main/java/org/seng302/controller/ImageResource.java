package org.seng302.controller;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.Authorization;
import org.seng302.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ImageResource {

    private static final Logger logger = LogManager.getLogger(ImageResource.class.getName());

    @PostMapping("/businesses/{businessId}/products/{productId}/images")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Image created successfully")
    public void createImage(
            @RequestParam("images") MultipartFile[] images,
            @CookieValue(value = "JSESSIONID", required = false) String sessionToken,
            @PathVariable Integer businessId,
            @PathVariable String productId
    ) {



    }

}
