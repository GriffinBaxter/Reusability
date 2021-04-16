package org.seng302.business.product.images;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService implements StorageServiceInterface{

    private final Path rootPath = Paths.get("product-images");

    @Override
    public void initialize() throws IOException {
        Files.createDirectory(rootPath);
    }

    @Override
    public void storeFile(MultipartFile file, String fileName) throws IOException{
        Files.copy(file.getInputStream(), this.rootPath.resolve(fileName));
    }

    // Implement load image as resource method

}
