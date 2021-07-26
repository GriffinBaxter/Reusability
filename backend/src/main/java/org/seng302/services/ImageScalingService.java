package org.seng302.services;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Simple service class that contains classes useful for image scaling.
 */
public class ImageScalingService {

    /**
     * Resizes an image given an original image and target dimensions.
     * Source: https://www.baeldung.com/java-resize-image#2-imagegetscaledinstance
     *
     * @param originalImage Image before scaling
     * @param targetWidth target width of the new image
     * @param targetHeight target height of the new image
     * @return newly scaled image.
     */
    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        var resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        var outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
