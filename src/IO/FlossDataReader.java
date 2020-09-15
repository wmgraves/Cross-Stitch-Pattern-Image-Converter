package IO;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class FlossDataReader {
    /**
     * Read floss data from the default data file
     * @return A HashMap with <Color, <Floss Code, Description, In Stock>>
     */
    public static HashMap<Color, Triplet<String, String, Boolean>> readFlossDataFile() {
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
        HashMap<Color, Triplet<String, String, Boolean>> returnMap = new HashMap<>();
        XSSFSheet sheet = workbook.getSheet("Data");

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            // Get the color
            XSSFRow row = sheet.getRow(i);
            XSSFCell cell = row.getCell(2);
            byte[] rgbComponents = cell.getCellStyle().getFillForegroundColorColor().getRGB();
            int[] rgb = new int[rgbComponents.length];
            for (int a = 0; a < rgb.length; a++) { rgb[a] = java.lang.Byte.toUnsignedInt(rgbComponents[a]); }
            Color color = new Color(rgb[0], rgb[1], rgb[2]);

            // Get related information
            cell = row.getCell(0);
            String code = cell.getRawValue();

            cell = row.getCell(1);
            String description = cell.getRawValue();

            cell = row.getCell(3);
            boolean inStock = cell.getRawValue().trim().toLowerCase().charAt(0) == 'y';

            // Store row information
            returnMap.put(color, new Triplet(code, description, inStock));
        }

        return returnMap;
    }
}
