package ProgramUI;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SettingsUI {
    private Dimension maxImageSize;
    private JLabel patternDisplay;
    private JPanel patternDisplayArea;

    public SettingsUI(BufferedImage image) {
        int pad = 5;
        int borderThickness = 2;

        // Create the main window
        JFrame window = new JFrame("Pattern Creator Settings");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(600, 400);
        window.setVisible(true);

        SpringLayout layout = new SpringLayout();
        window.setLayout(layout);

        // Create the image preview area
        maxImageSize = new Dimension(300, 300);
        patternDisplayArea = new JPanel();
        patternDisplayArea.setBorder(BorderFactory.createLineBorder(Color.gray, borderThickness));
        patternDisplayArea.setVisible(true);
        patternDisplayArea.setLayout(new SpringLayout());
        window.add(patternDisplayArea);
        layout.putConstraint(SpringLayout.WEST, patternDisplayArea, pad, SpringLayout.WEST, window);
        layout.putConstraint(SpringLayout.NORTH, patternDisplayArea, pad, SpringLayout.NORTH, window);
        layout.putConstraint(SpringLayout.WIDTH, patternDisplayArea, maxImageSize.width-pad+2*borderThickness,
                SpringLayout.WEST, patternDisplayArea);
        layout.putConstraint(SpringLayout.HEIGHT, patternDisplayArea, maxImageSize.height-pad+2*borderThickness,
                SpringLayout.NORTH, patternDisplayArea);

        // Add the image
        updateDisplayImage(image);

    }

    public void updateDisplayImage(BufferedImage image) {
        if (patternDisplay != null) { patternDisplayArea.remove(patternDisplay); }
        Dimension newImageSize = getDisplayDimensions(image, maxImageSize);
        patternDisplay = new JLabel(new ImageIcon(resizeDisplayImage(image, newImageSize.width,
                newImageSize.height)));
        patternDisplay.setVisible(true);
        patternDisplayArea.add(patternDisplay);

        SpringLayout layout = (SpringLayout) patternDisplayArea.getLayout();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, patternDisplay, 0, SpringLayout.HORIZONTAL_CENTER,
                patternDisplayArea);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, patternDisplay, 0, SpringLayout.VERTICAL_CENTER,
                patternDisplayArea);

        patternDisplayArea.revalidate();
    }

    private BufferedImage resizeDisplayImage(BufferedImage image, int newWidth, int newHeight) {
        int type = BufferedImage.TYPE_INT_ARGB;


        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, type);
        Graphics2D g = resizedImage.createGraphics();

        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.drawImage(image, 0, 0, newWidth, newHeight, null);
        g.dispose();


        return resizedImage;
    }

    private Dimension getDisplayDimensions(BufferedImage image, Dimension maxSize) {
        int original_width = image.getWidth();
        int original_height = image.getHeight();
        int bound_width = maxSize.width;
        int bound_height = maxSize.height;
        int new_width = original_width;
        int new_height = original_height;

        // Check if width should be scaled
        if (original_width > bound_width) {
            // Scale width to fit
            new_width = bound_width;
            // Scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // Check if height should be scaled
        if (new_height > bound_height) {
            // Scale height to fit
            new_height = bound_height;
            // Scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }
}
