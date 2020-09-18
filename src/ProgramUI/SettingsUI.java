package ProgramUI;

import IO.FlossDataReader;
import IO.Triplet;
import ImageProcessing.ColorPicker;
import ImageProcessing.ColorTransform;
import ImageProcessing.MedianCutQuantization;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

public class SettingsUI {
    // Pattern preview stuff
    private Dimension maxImageSize;
//    private JLabel patternDisplay;
//    private JPanel patternDisplayArea;

    // Settings stuff
    HashMap<String, String> settings = new HashMap<>();
    HashMap<String, Component> components = new HashMap<>();
    HashMap<Color, Triplet<String, String, Boolean>> flossData;
    private BufferedImage originalImage;
    private ArrayList<Color> selectedColors;

    public SettingsUI(BufferedImage image) {
        int pad = 5;
        int borderThickness = 2;
        originalImage = image;

        // Create the main window
        JFrame window = new JFrame("Pattern Converter Settings");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(940, 550);
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        SpringLayout layout = new SpringLayout();
        window.setLayout(layout);

        // Create the image preview area
        maxImageSize = new Dimension(500, 500);
        JPanel patternDisplayArea = new JPanel();
        patternDisplayArea.setBorder(BorderFactory.createLineBorder(Color.gray, borderThickness));
        patternDisplayArea.setVisible(true);
        patternDisplayArea.setLayout(new SpringLayout());
        window.add(patternDisplayArea);
        layout.putConstraint(SpringLayout.WEST, patternDisplayArea, pad, SpringLayout.WEST, window);
        layout.putConstraint(SpringLayout.NORTH, patternDisplayArea, pad, SpringLayout.NORTH, window);
        layout.putConstraint(SpringLayout.WIDTH, patternDisplayArea,
                maxImageSize.width-pad+2*borderThickness, SpringLayout.WEST, patternDisplayArea);
        layout.putConstraint(SpringLayout.HEIGHT, patternDisplayArea,
                maxImageSize.height-pad+2*borderThickness, SpringLayout.NORTH, patternDisplayArea);
        components.put("Pattern Display Area", patternDisplayArea);

        // Add pattern size settings
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createLineBorder(Color.gray, borderThickness));
        settingsPanel.setVisible(true);
        settingsPanel.setLayout(new SpringLayout());
        window.add(settingsPanel);
        layout.putConstraint(SpringLayout.WEST, settingsPanel, pad, SpringLayout.EAST, patternDisplayArea);
        layout.putConstraint(SpringLayout.NORTH, settingsPanel, pad, SpringLayout.NORTH, window);
        layout.putConstraint(SpringLayout.EAST, settingsPanel, 200, SpringLayout.WEST, settingsPanel);
        layout.putConstraint(SpringLayout.SOUTH, settingsPanel, 100, SpringLayout.NORTH, settingsPanel);
        components.put("Settings Panel", settingsPanel);

        JLabel header = new JLabel("Pattern Information");
        header.setVisible(true);
        settingsPanel.add(header);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, header, pad,
                SpringLayout.WEST, settingsPanel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, header, pad,
                SpringLayout.NORTH, settingsPanel);
        components.put("Settings Header", header);

        JLabel countLabel = new JLabel("Fabric Count:");
        countLabel.setVisible(true);
        settingsPanel.add(countLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, countLabel, pad,
                SpringLayout.WEST, settingsPanel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, countLabel, pad,
                SpringLayout.SOUTH, header);
        components.put("Fabric Count Label", countLabel);

        JTextField textField = new JTextField();
        int initialCount = 12;
        textField.setText("" + initialCount);
        textField.setVisible(true);
        settingsPanel.add(textField);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, textField, pad,
                SpringLayout.EAST, countLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, textField, 0,
                SpringLayout.NORTH, countLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.EAST, textField, -pad,
                SpringLayout.EAST, settingsPanel);
        components.put("Fabric Count TextField", textField);

        JLabel lengthLabel = new JLabel("Length (in):");
        lengthLabel.setVisible(true);
        settingsPanel.add(lengthLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, lengthLabel, pad,
                SpringLayout.WEST, settingsPanel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, lengthLabel, pad,
                SpringLayout.SOUTH, countLabel);
        components.put("Length Label", lengthLabel);

        textField = new JTextField();
        textField.setText(String.format("%.2f", ((double) image.getWidth()) / initialCount));
        textField.setVisible(true);
        settingsPanel.add(textField);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, textField, pad,
                SpringLayout.EAST, lengthLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, textField, 0,
                SpringLayout.NORTH, lengthLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.EAST, textField, -pad,
                SpringLayout.EAST, settingsPanel);
        components.put("Length TextField", textField);

        JLabel heightLabel = new JLabel("Height (in):");
        heightLabel.setVisible(true);
        settingsPanel.add(heightLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, heightLabel, pad,
                SpringLayout.WEST, settingsPanel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, heightLabel, pad,
                SpringLayout.SOUTH, lengthLabel);
        components.put("Height Label", heightLabel);

        textField = new JTextField();
        textField.setText(String.format("%.2f", ((double) image.getHeight()) / initialCount));
        textField.setVisible(true);
        settingsPanel.add(textField);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.WEST, textField, pad,
                SpringLayout.EAST, heightLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.NORTH, textField, 0,
                SpringLayout.NORTH, heightLabel);
        ((SpringLayout) settingsPanel.getLayout()).putConstraint(SpringLayout.EAST, textField, -pad,
                SpringLayout.EAST, settingsPanel);
        components.put("Height TextField", textField);

        settingsPanel.revalidate();

        // Add color settings
        JPanel colorsPanel = new JPanel();
        colorsPanel.setBorder(BorderFactory.createLineBorder(Color.gray, borderThickness));
        colorsPanel.setVisible(true);
        colorsPanel.setLayout(new SpringLayout());
        window.add(colorsPanel);
        layout.putConstraint(SpringLayout.WEST, colorsPanel, pad, SpringLayout.EAST, settingsPanel);
        layout.putConstraint(SpringLayout.NORTH, colorsPanel, 0, SpringLayout.NORTH, settingsPanel);
        layout.putConstraint(SpringLayout.EAST, colorsPanel, 200, SpringLayout.WEST, colorsPanel);
        layout.putConstraint(SpringLayout.SOUTH, colorsPanel, patternDisplayArea.getHeight(),
                SpringLayout.NORTH, colorsPanel);
        components.put("Colors Panel", colorsPanel);

        header = new JLabel("Color Information");
        header.setVisible(true);
        colorsPanel.add(header);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.WEST, header, pad,
                SpringLayout.WEST, colorsPanel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.NORTH, header, pad,
                SpringLayout.NORTH, colorsPanel);
        components.put("Colors Header", header);

        JLabel numberLabel = new JLabel("Number of Colors:");
        numberLabel.setVisible(true);
        colorsPanel.add(numberLabel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.WEST, numberLabel, pad,
                SpringLayout.WEST, colorsPanel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.NORTH, numberLabel, pad,
                SpringLayout.SOUTH, header);
        components.put("Number of Colors Label", numberLabel);

        textField = new JTextField();
        int initialColorNum = 200;
        textField.setText("" + initialColorNum);
        textField.setVisible(true);
        colorsPanel.add(textField);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.WEST, textField, pad,
                SpringLayout.EAST, numberLabel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.NORTH, textField, 0,
                SpringLayout.NORTH, numberLabel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.EAST, textField, 50,
                SpringLayout.WEST, textField);
        components.put("Number of Colors TextField", textField);

        JScrollPane colorScroll = new JScrollPane();
        colorScroll.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
        colorScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        colorScroll.getVerticalScrollBar().setUnitIncrement(16);
        colorScroll.setVisible(true);
        colorsPanel.add(colorScroll);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.WEST, colorScroll, pad,
                SpringLayout.WEST, colorsPanel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.NORTH, colorScroll, pad,
                SpringLayout.SOUTH, textField);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.EAST, colorScroll, -pad,
                SpringLayout.EAST, colorsPanel);
        ((SpringLayout) colorsPanel.getLayout()).putConstraint(SpringLayout.SOUTH, colorScroll, -pad,
                SpringLayout.SOUTH, colorsPanel);
        components.put("Color ScollPane", colorScroll);

        colorsPanel.revalidate();



        //TODO: Add more settings stuff for kernels and output type and maybe other things



        // Get floss data
        ProgressMonitor progressMonitor = new ProgressMonitor(window, "Reading floss data file...",
                "", 0, 100);
        flossData = FlossDataReader.readFlossDataFile(progressMonitor);

        // Add the image
        components.put("Pattern Display", null);
        updateDisplayImage(originalImage);

        // Transform original image to use selection of floss colors
        progressMonitor = new ProgressMonitor(window, "Selecting floss colors...", "",
                0, 100);
        ArrayList<ArrayList<Color>> colorBuckets = MedianCutQuantization.quantize(originalImage, initialColorNum,
                progressMonitor);
        selectedColors = ColorPicker.averageColors(colorBuckets);
        selectedColors = ColorPicker.convertIntoFlossColors(flossData, selectedColors);
        selectedColors = ColorPicker.removeDuplicates(selectedColors);
        ((JTextField) components.get("Number of Colors TextField")).setText(selectedColors.size() + "");
        updateColorList();
        progressMonitor = new ProgressMonitor(window, "Applying floss colors...", "",
                0, 100);
        BufferedImage transformedImage = ColorTransform.transformColors(image, selectedColors, progressMonitor);
        updateDisplayImage(transformedImage);


    }

    private void updateColorList() {
        JScrollPane colorScroll = ((JScrollPane) components.get("Color ScollPane"));
        if (selectedColors.size() <= 0) { return; }

        JPanel container = new JPanel(new GridLayout(0,1));
        for (Color color : selectedColors) {
            Triplet<String, String, Boolean> colorData = flossData.get(color);
            if (colorData == null) {
                selectedColors.remove(color);
                continue;
            }

            SpringLayout currentLayout = new SpringLayout();
            JPanel currentPanel = new JPanel(currentLayout);
            currentPanel.setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));

            JLabel flossName = new JLabel(colorData.getSecond());
            currentPanel.add(flossName);
            currentLayout.putConstraint(SpringLayout.NORTH, flossName, 0,
                    SpringLayout.NORTH, currentPanel);
            currentLayout.putConstraint(SpringLayout.WEST, flossName, 2,
                    SpringLayout.WEST, currentPanel);

            JPanel flossColor = new JPanel();
            flossColor.setBackground(color);
            flossColor.setBorder(BorderFactory.createLineBorder(Color.black, 1));
            currentPanel.add(flossColor);
            currentLayout.putConstraint(SpringLayout.NORTH, flossColor, 3,
                    SpringLayout.SOUTH, flossName);
            currentLayout.putConstraint(SpringLayout.WEST, flossColor, 1,
                    SpringLayout.WEST, flossName);
            currentLayout.putConstraint(SpringLayout.EAST, flossColor, 40,
                    SpringLayout.WEST, flossColor);
            currentLayout.putConstraint(SpringLayout.SOUTH, flossColor, -3,
                    SpringLayout.SOUTH, currentPanel);

            JLabel flossCode = new JLabel(colorData.getFirst());
            currentPanel.add(flossCode);
            currentLayout.putConstraint(SpringLayout.NORTH, flossCode, 0,
                    SpringLayout.NORTH, flossColor);
            currentLayout.putConstraint(SpringLayout.WEST, flossCode, 5,
                    SpringLayout.EAST, flossColor);

            container.add(currentPanel);
        }

        container.setPreferredSize(new Dimension(colorScroll.getViewport().getWidth(),
                selectedColors.size() * 40));
        colorScroll.setViewportView(container);
        colorScroll.revalidate();
        components.get("Colors Panel").revalidate();
    }

    private void updateDisplayImage(BufferedImage image) {
        // Remove existing image
        JPanel patternDisplayArea = (JPanel) components.get("Pattern Display Area");
        patternDisplayArea.removeAll();

        // Create a new image
        Dimension newImageSize = getDisplayDimensions(image, maxImageSize);
        JLabel patternDisplay = new JLabel(new ImageIcon(resizeDisplayImage(image, newImageSize.width,
                newImageSize.height)));
        patternDisplay.setVisible(true);
        patternDisplayArea.add(patternDisplay);
        components.replace("Pattern Display", patternDisplay);

        // Position and display new image
        SpringLayout layout = (SpringLayout) patternDisplayArea.getLayout();
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, patternDisplay, 0,
                SpringLayout.HORIZONTAL_CENTER, patternDisplayArea);
        layout.putConstraint(SpringLayout.VERTICAL_CENTER, patternDisplay, 0,
                SpringLayout.VERTICAL_CENTER, patternDisplayArea);

        patternDisplayArea.revalidate();
    }

    private BufferedImage resizeDisplayImage(BufferedImage image, int newWidth, int newHeight) {
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
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
