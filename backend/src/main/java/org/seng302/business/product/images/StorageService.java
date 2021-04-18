package org.seng302.business.product.images;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

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


    @Override
    public Optional<Resource> loadFileByFileName(String fileName) {
        Optional<Resource> resource = Optional.empty();

        try {
            Path file = rootPath.resolve(fileName);
            resource = Optional.of(new UrlResource(file.toUri()));

            if (resource.get().exists() || resource.get().isReadable()) {
                return resource;
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException error) {
            return Optional.empty();
        }

    }

}
