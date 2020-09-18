package IO;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;

public class FlossDataReader {
    /**
     * Read floss data from the default data file
     * @return A HashMap with <Color, <Floss Code, Description, In Stock>>
     */
    public static HashMap<Color, Triplet<String, String, Boolean>> readFlossDataFile(ProgressMonitor monitor) {
        // Open the data file
        File file = new File("src/IO/DMCFlossData.xlsx");
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook((new FileInputStream(file)));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Create a HashMap and fill it with all of the floss data
        DataFormatter dataFormatter = new DataFormatter();
        HashMap<Color, Triplet<String, String, Boolean>> returnMap = new HashMap<>();
        XSSFSheet sheet = workbook.getSheet("Data");

        for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
            // Get the color
            XSSFRow row = sheet.getRow(i);
            XSSFCell cell = row.getCell(2);
            byte[] rgbComponents = cell.getCellStyle().getFillForegroundColorColor().getRGB();
            int[] rgb = new int[rgbComponents.length];
            for (int a = 0; a < rgb.length; a++) { rgb[a] = java.lang.Byte.toUnsignedInt(rgbComponents[a]); }
            Color color = new Color(rgb[0], rgb[1], rgb[2]);

            // Get related information
            String code = dataFormatter.formatCellValue(row.getCell(0));
            String description = dataFormatter.formatCellValue(row.getCell(1));
            Boolean inStock = dataFormatter.formatCellValue(row.getCell(3)).trim().toLowerCase().charAt(0)
                    == 'y';

            // Store row information
            returnMap.put(color, new Triplet(code, description, inStock));

            // Update progress
            int progress = i * monitor.getMaximum() / sheet.getLastRowNum();
            monitor.setNote(String.format("Completed %d%%\n", progress));
            monitor.setProgress(progress);
        }

        monitor.close();
        return returnMap;
    }
}
