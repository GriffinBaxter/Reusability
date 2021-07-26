package org.seng302.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Optional;

/**
 * Provides the user with the ability to load, store and delete files from the storage folder in the file system.
 */
@Service
public class FileStorageService {

    private final Path rootPath;
    private static final Logger logger = LogManager.getLogger(FileStorageService.class.getName());

    /**
     * Ensures that the FileStorageService is ready
     * @param folderName relative sub directory path of /storage/** leading to where you want to create you files.
     */
    public FileStorageService(@Value("") String folderName) {
        this.rootPath = Paths.get("storage/" + folderName);
        this.initialize();
    }

    /**
     * Initializes the directory which stores files (by creating it if it does not exist)
     */
    public void initialize() {
        try {
            Files.createDirectories(rootPath);
            String log = "Successfully created " + rootPath + " directory";
            logger.info(log);
        } catch (IOException e) {
            String log = "Failed to create " + rootPath + " directory";
            logger.error(log, e);
            System.exit(1);
        }
    }

    /**
     * Stores a given file into a path resolved from the file name that is provided.
     *
     * @param file The file to be stored.
     * @param fileName The final place to store it.
     * @return true if the file was stored correctly. Otherwise false.
     * @throws FileAlreadyExistsException When the file attempting to be creating already exists in the file system.
     */
    public boolean storeFile(InputStream file, String fileName) throws FileAlreadyExistsException{
        try {
            Files.copy(file, this.rootPath.resolve(fileName));
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
            if (Paths.get(fileName).startsWith(this.rootPath)) {
                return Files.deleteIfExists(Paths.get(fileName));
            }
            return false;
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

    public String getPathString(String fileName) {
        try {
            return this.rootPath.resolve(fileName).toString();
        } catch (InvalidPathException e) {
            return null;
        }
    }
}
