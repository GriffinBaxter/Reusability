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

    /**
     * Initialized the sotrage service to create folder to store the images.
     * @throws IOException - Thrown when it fails to create a directory named after the rootPath
     */
    @Override
    public void initialize() throws IOException {
        Files.createDirectory(rootPath);
    }

    /**
     * Stores a file under a fileName.
     * @param file - The actual file (MultipartFile)
     * @param fileName - The name you want to give the file.
     * @throws IOException - Thrown when fails to create the file.
     */
    @Override
    public void storeFile(MultipartFile file, String fileName) throws IOException{
        Files.copy(file.getInputStream(), this.rootPath.resolve(fileName));
    }

    /**
     * Retrieives the file using a filename to search
     * @param fileName - The filename you want to find.
     * @return Returns the resource of the file inside an Optional container. Otherwise the Optional will be empty.
     */
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
