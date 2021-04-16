package org.seng302.business.product.images;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageServiceInterface {

    void initialize() throws IOException;

    public void storeFile(MultipartFile file, String fileName) throws IOException;

}