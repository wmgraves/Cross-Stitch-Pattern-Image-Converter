package ImageProcessing;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Kernel {

    public static BufferedImage applyKernel(BufferedImage image, KernelType kernel, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        System.out.println("Applying " + kernel.name() + " kernel...");

        // Collect kernel sums
        double[][] values = new double[width][height];
        double maxValue = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int xPos = x * image.getWidth()/width;
                int yPos = y * image.getHeight()/height;

                Color color = getColorWithKernel(image, kernel, xPos, yPos);
                result.setRGB(x, y, color.getRGB());
            }
        }

        System.out.println("Finished applying kernel");
        return result;
    }

    public static BufferedImage visualizeKernel(BufferedImage image, KernelType kernel, int width, int height) {
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        System.out.println("Creating " + kernel.name() + " kernel visualization...");

        // Collect kernel sums
        double[][] values = new double[width][height];
        double maxValue = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int xPos = x * image.getWidth()/width;
                int yPos = y * image.getHeight()/height;

                Color color = getColorWithKernel(image, kernel, xPos, yPos);
                values[x][y] = color.getRed() + color.getGreen() + color.getBlue();
                if (values[x][y] > maxValue) {
                    maxValue = values[x][y];
                }
            }
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int value = (int) Math.min(((values[x][y] / maxValue) * 255), 255);
                Color color = new Color(value, value, value);
                result.setRGB(x, y, color.getRGB());
            }
        }

        System.out.println("Finished creating visualization");
        return result;
    }

    public static Color getColorWithKernel(BufferedImage image, KernelType kernel, int xPos, int yPos) {
        int[] centerPosition = getKernelCenterOffset(kernel.data);
        double sum = 0, red = 0, green = 0, blue = 0;

        for (int x = -centerPosition[0]; x < centerPosition[0]; x++) {
            for (int y = -centerPosition[1]; y < centerPosition[1]; y++) {
                int realX = xPos + x, realY = yPos + y;
                if (realX < 0 || realX >= image.getWidth() || realY < 0 || realY >= image.getHeight()) {
                    continue;
                }

                Color color = new Color(image.getRGB(realX, realY));
                sum += kernel.data[x+centerPosition[0]][y+centerPosition[1]];
                red += color.getRed() * kernel.data[x+centerPosition[0]][y+centerPosition[1]];;
                green += color.getGreen() * kernel.data[x+centerPosition[0]][y+centerPosition[1]];
                blue += color.getBlue() * kernel.data[x+centerPosition[0]][y+centerPosition[1]];
            }
        }

        return new Color((int) Math.min(Math.abs(red/sum), 255), (int) Math.min(Math.abs(green/sum), 255),
                (int) Math.min(Math.abs(blue/sum), 255));
    }

    private static int[] getKernelCenterOffset(double[][] data) {
        return new int[]{data.length/2, data[0].length/2};
    }

    public enum KernelType {
        EDGE_DETECTION(new double[][] {{0, -1, 0}, {-1, 4, -1}, {0, -1, 0}}),
        SHARPEN(new double[][] {{-0.5, -1, -0.5}, {-1, 7, -1}, {-0.5, -1, -0.5}}),
        LANCZOS(getLanczosKernel(2, 7, 7));

        public double[][] data;
        private KernelType(double[][] data) {
            this.data = data;
        }

        private static double[][] getLanczosKernel(double a, int width, int height) {
            double[][] return2dArray = new double[width][height];
            int[] centerLocation = getKernelCenterOffset(return2dArray);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    double xPos = x - centerLocation[0];
                    double yPos = y - centerLocation[1];
                    xPos /= 2;
                    yPos /= 2;

                    if (xPos == 0) { return2dArray[x][y] = 1; }
                    else if (-a <= xPos && xPos <= a) {
                        return2dArray[x][y] = Math.sin(Math.PI*xPos)/(Math.PI*xPos);
                    }
                    else { return2dArray[x][y] = 0; }

                    if (yPos == 0) { return2dArray[x][y] *= 1; }
                    else if (-a <= yPos && yPos <= a) {
                        return2dArray[x][y] *= Math.sin(Math.PI*yPos)/(Math.PI*yPos);
                    }
                    else { return2dArray[x][y] = 0; }
                }
            }

            return return2dArray;
        }
    }
}
