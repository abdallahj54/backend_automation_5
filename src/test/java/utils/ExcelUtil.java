package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static Logger logger = LogManager.getLogger(ExcelUtil.class);
    private static XSSFWorkbook workbook;
    private static XSSFSheet sheet;
    private static XSSFRow row;
    private static XSSFCell cell;
    private static String filePath;

    public static void openExcelFile(String fileName, String sheetName){
        filePath = "test_data/" + fileName + ".xlsx";

        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
            logger.info("File " + fileName + " exists!");
            sheet = workbook.getSheet(sheetName);
            logger.info("Sheet " + sheetName + " exists!");
        } catch (Exception e) {
            logger.debug(fileName + " and " + sheetName + " cannot be found!");
        }
    }

    //Getting a single value from the Excel file
    public static String getValue(int rowNumber, int cellNumber){
        row = sheet.getRow(rowNumber);
        cell = row.getCell(cellNumber);
        return cell.toString();
    }

    //Getting all values from the Excel file
    public static List<List<String>> getValues(){
        // creating List of List to store all values from the Excel file
        List<List<String>> allValues = new ArrayList<>();

        // creating loops for getting the rows
        for (int r = sheet.getFirstRowNum() + 1; r <= sheet.getLastRowNum(); r++){
            // creating a row in the sheet
            row = sheet.getRow(r);

            // creating a list for storing row data
            List<String> eachRow = new ArrayList<>();

            // looping each cell in the corresponding row
            for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++){
                // getting each cell value and add them into eachRow list
                eachRow.add(getValue(r, c));
            }
            allValues.add(eachRow);
        }
        return allValues;
    }
}
