package org.seng302;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configures exposed directories to allow for access to files in the file system.
 * e.g., product-images
 * Source: https://www.codejava.net/frameworks/spring-boot/spring-boot-file-upload-tutorial
 */
@Configuration
public class ResourceConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        exposeDirectory("storage", registry);
    }

    /**
     * Exposes a directory to be access via the api endpoint.
     * @param directoryPath name of directory we want to expose.
     * @param registry resouce handler for the system.
     */
    public void exposeDirectory(String directoryPath, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(directoryPath);
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        if (directoryPath.startsWith("../")) directoryPath = directoryPath.replace("../", "");

        String resourceLocationStart = "";
        if (System.getProperty("os.name").startsWith("Windows")) {
            // Using a Windows system
            resourceLocationStart = "file:/";
        } else {
            // Using a Unix-like system
            resourceLocationStart = "file://";
        }

        registry.addResourceHandler("/" + directoryPath + "/**").addResourceLocations(
                resourceLocationStart + uploadPath + "/"
        );
    }
}
