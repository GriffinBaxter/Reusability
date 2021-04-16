package org.seng302.business.product.images;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface storageService {

    void init();

    void store(MultipartFile file);

    Path load(String filePath);

    Resource loadAsResource(String filePath);

}
