package api.tdd.pet_store;

import api.pojo_classes.pet_store.AddAPet;
import api.pojo_classes.pet_store.Category;
import api.pojo_classes.pet_store.Tags;
import api.pojo_classes.pet_store.UpdateAPet;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigReader;
import utils.DataProviderUtil;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AddPetToStoreWithLombokAndDataProvider {
    static Logger logger = LogManager.getLogger(AddPetToStoreWithLombok.class);

    Response response;

    @BeforeSuite
    public void testStarts() {
        logger.info("Staring the test suite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("Starting the API test");
        RestAssured.baseURI = ConfigReader.getProperty("PetStoreBaseURI");
    }

    @Test(dataProvider = "DataFromExcel", dataProviderClass = DataProviderUtil.class)
    // All the data types coming from the Excel file are String, so all method arguments are String
    public void addPetWithDataProvider(String petId, String categoryId, String categoryName,
                                       String petName, String photoUrl, String tagId,
                                       String tagName, String petStatus){
        Category category = Category
                .builder()
                .id(Integer.parseInt(categoryId))
                .name(categoryName)
                .build();

        Tags tags = Tags
                .builder()
                .id(Integer.parseInt(tagId))
                .name(tagName)
                .build();

        AddAPet addAPet = AddAPet
                .builder()
                .id(Integer.parseInt(petId))
                .category(category)
                .name(petName)
                .photoUrls(Arrays.asList(photoUrl))
                .tags(Arrays.asList(tags))
                .status(petStatus)
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(addAPet)
                .when().post("/v2/pet")
                .then().log().all()
                .extract().response();
    }
}
