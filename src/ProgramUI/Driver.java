package ProgramUI;

import IO.FlossDataReader;
import IO.PatternPreviewCreator;
import IO.Triplet;
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
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg",
                "png");
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

        // Get info from user about desired pattern
        new SettingsUI(image);
    }

}
