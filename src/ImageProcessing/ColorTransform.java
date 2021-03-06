package ImageProcessing;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorTransform {
    public static BufferedImage transformColors(BufferedImage image, ArrayList<Color> colors,
                                                ProgressMonitor monitor) {
        System.out.println("Transforming colors found in input image...");
        BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color closestColor = null;
                int closestDistance = 200000;

                for (Color color : colors) {
                    if (closestColor == null) {
                        closestColor = color;
                        closestDistance = getColorDistance(color, new Color(image.getRGB(x, y)));
                    }
                    else {
                        int temp = getColorDistance(color, new Color(image.getRGB(x, y)));
                        if (temp < closestDistance) {
                            closestColor = color;
                            closestDistance = temp;
                        }
                    }
                }

                outputImage.setRGB(x, y, closestColor.getRGB());
            }

            int progress = x * monitor.getMaximum() / image.getWidth();
            monitor.setNote(String.format("Completed %d%%\n", progress));
            monitor.setProgress(progress);
        }

        monitor.close();
        System.out.println("Finished modifying colors");
        return outputImage;
    }

    public static int getColorDistance(Color color1, Color color2) {
        int red1 = color1.getRed();
        int red2 = color2.getRed();
        int rmean = (red1 + red2) >> 1;
        int r = red1 - red2;
        int g = color1.getGreen() - color2.getGreen();
        int b = color1.getBlue() - color2.getBlue();
        return (((512+rmean)*r*r)>>8) + 4*g*g + (((767-rmean)*b*b)>>8);
    }
}
