package ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Comparator;

public class MedianCutQuantization {
    public static ArrayList<ArrayList<Color>> quantize(BufferedImage image, int maxNumColors,
                                                       ProgressMonitor monitor) {
        System.out.println("Performing median cut quantization...");
        // Initialize variables
        ArrayList<Color> colors = new ArrayList<>();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                colors.add(new Color(image.getRGB(x, y)));
            }
        }

        ArrayList<ArrayList<Color>> colorBuckets = new ArrayList<>();
        colorBuckets.add(colors);
        while (colorBuckets.size() < maxNumColors) {
            ArrayList<ArrayList<Color>> newColorBuckets = new ArrayList<>();

            for (int i = 0; i < colorBuckets.size(); i++) {
                int numRemaining = colorBuckets.size() - i;

                if (i*2 + numRemaining < maxNumColors) {
                    for (ArrayList<Color> newBucket : medianCutHelper(colorBuckets.get(i))) {
                        newColorBuckets.add(newBucket);
                    }
                }
                else {
                    newColorBuckets.add(colorBuckets.get(i));
                }
            }
            colorBuckets = newColorBuckets;

            int progress = colorBuckets.size() * monitor.getMaximum() / maxNumColors;
            monitor.setNote(String.format("Completed %d%%\n", progress));
            monitor.setProgress(progress);
        }
        System.out.println("Finished quantization");
        monitor.close();

        return colorBuckets;
    }

    /**
     * Helper method that sorts a list of colors by the rgb component with the greatest range and
     * returns two sublists.
     * @param colors The list of colors
     * @return Two sublists of sorted colors
     */
    private static ArrayList<ArrayList<Color>> medianCutHelper(ArrayList<Color> colors) {
        int minRed = 0, maxRed = 0, minGreen = 0, maxGreen = 0, minBlue = 0, maxBlue = 0;

        for (Color color : colors) {
            if (color.getRed() < minRed) { minRed = color.getRed(); }
            if (color.getRed() > maxRed) { maxRed = color.getRed(); }
            if (color.getGreen() < minGreen) { minGreen = color.getGreen(); }
            if (color.getGreen() > maxGreen) { maxGreen = color.getGreen(); }
            if (color.getBlue() < minBlue) { minBlue = color.getBlue(); }
            if (color.getBlue() > maxBlue) { maxBlue = color.getBlue(); }
        }

        // Sort list by the color that has the greatest range
        int redRange = maxRed - minRed;
        int greenRange = maxGreen - minGreen;
        int blueRange = maxBlue - minBlue;

        if (redRange > greenRange && redRange > blueRange) {
            colors.sort(new RedComparator());
        }
        else if (greenRange > redRange && greenRange > blueRange) {
            colors.sort(new GreenComparator());
        }
        else {
            colors.sort(new BlueComparator());
        }

        ArrayList<ArrayList<Color>> returnList = new ArrayList<>();
        returnList.add(new ArrayList<Color>(colors.subList(0, colors.size()/2-1)));
        returnList.add(new ArrayList<Color>(colors.subList(colors.size()/2, colors.size())));

        return returnList;
    }

    static class RedComparator implements Comparator<Color> {
        @Override
        public int compare(Color o1, Color o2) {
            return o1.getRed() - o2.getRed();
        }
    }

    static class GreenComparator implements Comparator<Color> {
        @Override
        public int compare(Color o1, Color o2) {
            return o1.getGreen() - o2.getGreen();
        }
    }

    static class BlueComparator implements Comparator<Color> {
        @Override
        public int compare(Color o1, Color o2) {
            return o1.getBlue() - o2.getBlue();
        }
    }
}
