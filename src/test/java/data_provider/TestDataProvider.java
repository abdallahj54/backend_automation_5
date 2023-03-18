package data_provider;

import org.testng.annotations.Test;

public class TestDataProvider {

    @Test(dataProvider = "Course", dataProviderClass = DataProviderData.class)
    public void testingDataProvider(String courseName, int courseId, String batchName){
        System.out.println(courseName + " | " +  courseId + " | " + batchName);
    }
}
