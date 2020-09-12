package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ColorTransform {
    public static void transformColors(BufferedImage inputImage, BufferedImage outputImage, ArrayList<Color> colors) {
        System.out.println("\nModifying colors found in input image...");

        int nextStatus = 10;
        for (int x = 0; x < inputImage.getWidth(); x++) {
            for (int y = 0; y < inputImage.getHeight(); y++) {
                Color closestColor = null;
                int closestDistance = 200000;

                for (Color color : colors) {
                    if (closestColor == null) {
                        closestColor = color;
                        closestDistance = getColorDistance(color, new Color(inputImage.getRGB(x, y)));
                    }
                    else {
                        int temp = getColorDistance(color, new Color(inputImage.getRGB(x, y)));
                        if (temp < closestDistance) {
                            closestColor = color;
                            closestDistance = temp;
                        }
                    }
                }

                outputImage.setRGB(x, y, closestColor.getRGB());
            }

            if (((float) x) / inputImage.getWidth() * 100 >= nextStatus) {
                System.out.println("  " + nextStatus + "%");
                nextStatus += 10;
            }
        }

        System.out.println("Finished modifying colors");
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
