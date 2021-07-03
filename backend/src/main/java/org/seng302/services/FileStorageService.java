package org.seng302.services;

import jdk.jshell.spi.ExecutionControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.seng302.controller.ListingResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileStorageService {

    private final Path rootPath;
    private static final Logger logger = LogManager.getLogger(ListingResource.class.getName());

    public FileStorageService(String rootPath) {
        this.rootPath = Paths.get(rootPath);
        this.initialize();
    }

    /**
     * Initializes the directory which stores files (by creating it if it does not exist)
     *
     */
    public void initialize() {
        try {
            Files.createDirectory(rootPath);
            String log = "Successfully created " + rootPath + "directoy";
            logger.info(log);
        } catch (IOException e) {
            String log = "Failed to create " + rootPath + "directoy";
            logger.error(log);
        }
    }

    /**
     * Stores a given file into a path resolved from the file name that is provided.
     *
     * @param file The file to be stored.
     * @param fileName The final place to store it.
     * @return true if the file was stored correctly. Otherwise false.
     */
    public boolean storeFile(MultipartFile file, String fileName) {
        try {
            Files.copy(file.getInputStream(), this.rootPath.resolve(fileName));
            String log = "Succesfully stored file into " + fileName;
            logger.debug(log);
            return true;
        } catch (IOException e) {
            String log = "Failed to store " + fileName;
            logger.debug(log);
            return false;
        }
    }

    /**
     * Retrieives the file using a filename to search
     *
     * @param fileName - The filename you want to find.
     * @return Returns the resource of the file inside an Optional container. Otherwise the Optional will be empty.
     */
    public Optional<Resource> loadFileByFileName(String fileName) {
        Optional<Resource> resource;

        try {
            resource = Optional.of(new UrlResource(rootPath.resolve(fileName).toUri()));

            if (resource.get().exists() || resource.get().isReadable()) {
                return resource;
            } else {
                return Optional.empty();
            }
        } catch (MalformedURLException error) {
            return Optional.empty();
        }

    }

    public void deleteFile(int fileName) throws ExecutionControl.NotImplementedException {
        throw new ExecutionControl.NotImplementedException("Not created yet...");
    }
}
