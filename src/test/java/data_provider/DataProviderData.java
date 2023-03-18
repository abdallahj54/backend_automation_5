package data_provider;

import org.testng.annotations.DataProvider;

public class DataProviderData {

    @DataProvider(name = "Course")
    public static Object[][] getDataFromDataProvider(){

        return new Object[][] {
                {"Java", 1, "Batch5"},
                {"Selenium", 2, "Batch5"},
                {"SoftSkills", 3, "Batch5"},
                {"API", 4, "Batch5"},
                {"Performance", 5, "Batch5"}
        };
    }
}
