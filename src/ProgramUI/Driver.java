package ProgramUI;

import IO.PatternPreviewCreator;
import ImageProcessing.ColorPicker;
import ImageProcessing.ColorTransform;
import ImageProcessing.Kernel;
import ImageProcessing.MedianCutQuantization;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Handles running the entire program.
 */
public class Driver {
    /**
     * Main method
     * @param args unused
     */
    public static void main(String[] args) {
        //TODO: Add UI/method for selecting file to use
        String filePath = "C:\\Users\\William\\Desktop\\EeveeEvolutionPrismCropped.png";

        // Get a usable image from the selected file
        BufferedImage image;
        try {
            image = ImageIO.read(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Get info from user about desired pattern
        //TODO: Add UI/method for this
        HashMap<String, Object> settings = new HashMap<>();
        settings.put("Count", new Integer(18));
        settings.put("Number of Colors", new Integer(100));
        settings.put("Output File Path", "C:\\Users\\William\\Desktop\\");

        // Process image based on user inputs
        //TODO: Add UI/method for this
        ArrayList<ArrayList<Color>> colorGroups = MedianCutQuantization.quantize(image,
                (Integer) settings.get("Number of Colors"));
        ArrayList<Color> averageColors = ColorPicker.averageColors(colorGroups);

        int width = image.getWidth()/2, height = image.getHeight()/2;
        BufferedImage patternPreview;// = ColorTransform.transformColors(image, averageColors);
        BufferedImage kernelImage = Kernel.visualizeKernel(image, Kernel.KernelType.LANCZOS, width, height);
        patternPreview = Kernel.applyKernel(image, Kernel.KernelType.LANCZOS, width, height);
        patternPreview = ColorTransform.transformColors(patternPreview, averageColors);

        // Create a new image to show the result
        //TODO: Add method/UI for this
        boolean success = true;
        success = success && PatternPreviewCreator.createPNGFile(patternPreview, settings.get("Output File Path")
                + "preview.png");
        success = success && PatternPreviewCreator.createPNGFile(kernelImage, settings.get("Output File Path")
                + "kernel.png");

        if (success) {
            System.out.println("\nAll output files created successfully");
        }
        else {
            System.out.println("\nSome (or all) output files were not created successfully");
        }
    }

}
