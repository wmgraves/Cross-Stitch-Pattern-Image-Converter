package ProgramUI;

import IO.PatternPreviewCreator;
import ImageProcessing.ColorPicker;
import ImageProcessing.ColorTransform;
import ImageProcessing.Kernel;
import ImageProcessing.MedianCutQuantization;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles running the entire program.
 */
public class Driver {
    /**
     * Main method that runs the program
     * @param args unused
     */
    public static void main(String[] args) {
        // Get the input image file
        System.out.println("Prompting user to select file...");
        JFileChooser fileSelector = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "png");
        fileSelector.setFileFilter(filter);
        fileSelector.setMultiSelectionEnabled(false);

        int returnValue = fileSelector.showOpenDialog(null);
        if (returnValue != JFileChooser.APPROVE_OPTION) {
            System.out.println("No file was selected, aborting program");
            return;
        }
        System.out.println("File \"" + fileSelector.getSelectedFile() + "\" selected");

        BufferedImage image;
        try {
            image = ImageIO.read(fileSelector.getSelectedFile());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        new SettingsUI(image);

        // Get info from user about desired pattern
        //TODO: Add UI/method for this
        HashMap<String, BufferedImage> imagesToSave = new HashMap<>();
        HashMap<String, Object> settings = new HashMap<>();
        settings.put("Count", new Integer(18));
        settings.put("Number of Colors", new Integer(100));
        settings.put("Output File Path", "C:\\Users\\William\\Desktop\\");

        // Process image based on user inputs
        //TODO: Add UI/method for this
        ArrayList<ArrayList<Color>> colorGroups = MedianCutQuantization.quantize(image,
                (Integer) settings.get("Number of Colors"));
        ArrayList<Color> colorList = ColorPicker.averageColors(colorGroups);

        int width = 324, height = 216;
        BufferedImage currentImage = Kernel.applyKernel(image, Kernel.KernelType.LANCZOS, width, height);
        imagesToSave.put("ScaledDownImage", currentImage);
        currentImage = Kernel.applyKernel(currentImage, Kernel.KernelType.SHARPEN, width, height);
        imagesToSave.put("SharpensImage", currentImage);
        currentImage = ColorTransform.transformColors(currentImage, colorList);
        imagesToSave.put("FinalImage", currentImage);

        // Create a new image to show the result
        //TODO: Add method/UI for this
        System.out.println("\nSaving " + imagesToSave.size() + " output files...");
        boolean success = true;
        for (Map.Entry<String, BufferedImage> entry : imagesToSave.entrySet()) {
            success = success && PatternPreviewCreator.createPNGFile(entry.getValue(),
                    settings.get("Output File Path") + entry.getKey() + ".png");
        }

        if (success) {
            System.out.println("All output files created successfully");
        }
        else {
            System.out.println("Some (or all) output files were not created successfully");
        }
    }

}
