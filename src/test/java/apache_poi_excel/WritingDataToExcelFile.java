package apache_poi_excel;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class WritingDataToExcelFile {

    public static void main(String[] args) throws IOException {
        //Getting the file path
        String filePath = "test_data/WriteData.xlsx";

        //Created a workbook object
        XSSFWorkbook workbook = new XSSFWorkbook();

        //Created sheet on the new workbook
        XSSFSheet sheet = workbook.createSheet("Tech");

        //Go to the specific row on that sheet
        XSSFRow row = sheet.createRow(1);

        //Go to the specific cell in the specified row
        XSSFCell cell = row.createCell(1);

        //Writing the cell value
        cell.setCellValue("Tech Global");

        //Store the file path into the system
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);

        //Complete the writing process on the file
        workbook.write(fileOutputStream);

        //Closing the file after writing completion
        fileOutputStream.close();
    }
}
