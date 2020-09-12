package IO;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PatternPreviewCreator {
    public static boolean createPNGFile(BufferedImage image, String filePath) {
        if (image == null) { return false; }

        try {
            ImageIO.write(image, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
