package utils;

import org.testng.annotations.DataProvider;

public class DataProviderUtil {
    @DataProvider(name = "DataFromExcel")
    public static Object[][] getDataFromExcelFileWithDataProvider(){

        // opening the Excel file
        ExcelUtil.openExcelFile("PetDataToRead", "PetData");

        // storing converted data to multidimensional array
        Object[][] dataArray = ExcelUtil.convertListOfListToMultidimensionalArray(ExcelUtil.getValues());

        // closing the Excel file
        ExcelUtil.closingExcelFile();

        return dataArray;
    }
}
