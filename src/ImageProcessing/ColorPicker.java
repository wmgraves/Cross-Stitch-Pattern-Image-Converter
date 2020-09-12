package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Handles calculating color information
 */
public class ColorPicker {
    /**
     * Calculates and returns the average color of each color group in a list
     * @param colorGroups The list of all color groups
     * @return The average color
     */
    public static ArrayList<Color> averageColors(ArrayList<ArrayList<Color>> colorGroups) {
        System.out.println("\nAveraging color groups...");
        ArrayList<Color> averagedColors = new ArrayList<>();

        for (ArrayList<Color> colorGroup : colorGroups) {
            int red = 0, green = 0, blue = 0;

            for (Color color : colorGroup) {
                red += color.getRed();
                green += color.getGreen();
                blue += color.getBlue();
            }

            averagedColors.add(new Color(red/colorGroup.size(), green/colorGroup.size(),
                    blue/colorGroup.size()));
        }

        System.out.println("Finished averaging color groups");
        return averagedColors;
    }
}
