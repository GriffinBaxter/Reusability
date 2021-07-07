package org.seng302.services;

import jdk.jshell.spi.ExecutionControl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.seng302.controller.ListingResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class FileStorageService {

    private final Path rootPath;
    private static final Logger logger = LogManager.getLogger(FileStorageService.class.getName());

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
            FileUtils.deleteDirectory(rootPath.toFile());
            Files.createDirectory(rootPath);
            String log = "Successfully created " + rootPath + " directory";
            logger.info(log);
        } catch (IOException e) {
            String log = "Failed to create " + rootPath + " directory";
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
        System.out.println(fileName);
        System.out.println(file);
        try {
            Files.copy(file.getInputStream(), this.rootPath.resolve(fileName));
            String log = "Successfully stored file into " + fileName;
            logger.debug(log);
            return true;
        } catch (IOException e) {
            String log = "Failed to store " + fileName;
            logger.debug(log);
            return false;
        }
    }

    /**
     * Retrieves the file using a filename to search
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

    public boolean deleteFile(String fileName) {
        try {
            return Files.deleteIfExists(this.rootPath.resolve(fileName));
        } catch (DirectoryNotEmptyException e) {
            var errorMessage = String.format("Directory %s does not exist.", fileName);
            logger.error(errorMessage);
            return false;
        } catch (IOException e) {
            var errorMessage = String.format("An I/O error occurred deleting %s", fileName);
            logger.error(errorMessage);
            return false;
        }
    }
}
