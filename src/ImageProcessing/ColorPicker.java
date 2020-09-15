package ImageProcessing;

import IO.Triplet;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;

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
        System.out.println("Averaging color groups...");
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

    public static ArrayList<Color> removeDuplicates(ArrayList<Color> colors) {
        ArrayList<Color> returnList = new ArrayList<>();

        for (Color color : colors) {
            if (!returnList.contains(color)) {
                returnList.add(color);
            }
        }

        return returnList;
    }

    public static ArrayList<Color> convertIntoFlossColors(HashMap<Color,
            Triplet<String, String, Boolean>> colorList, ArrayList<Color> colors) {
        ArrayList<Color> returnList = new ArrayList<>();

        for (Color color : colors) {
            returnList.add(getClosestFlossColor(colorList, color).getKey());
        }

        return returnList;
    }

    public static Map.Entry<Color, Triplet<String, String, Boolean>> getClosestFlossColor(HashMap<Color,
            Triplet<String, String, Boolean>> colorList, Color color) {
        if (colorList.containsKey(color)) { return new AbstractMap.SimpleEntry<>(color, colorList.get(color)); }

        Color closestColor = null;
        int minColorDistance = -1;
        for (Color currentColor : colorList.keySet()) {
            int temp = ColorTransform.getColorDistance(color, currentColor);
            if (closestColor == null) {
                closestColor = currentColor;
                minColorDistance = temp;
            }
            else if (temp < minColorDistance) {
                closestColor = currentColor;
                minColorDistance = temp;
            }
        }

        return new AbstractMap.SimpleEntry<>(closestColor, colorList.get(closestColor));
    }
}
