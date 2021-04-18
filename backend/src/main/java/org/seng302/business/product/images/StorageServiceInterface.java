package org.seng302.business.product.images;


import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Optional;

public interface StorageServiceInterface {

    void initialize() throws IOException;

    public void storeFile(MultipartFile file, String fileName) throws IOException;


    public Optional<Resource> loadFileByFileName(String fileName);

}