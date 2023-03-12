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

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AddPetToStoreWithLombok {
    static Logger logger = LogManager.getLogger(AddPetToStoreWithLombok.class);

    Response response;

    @BeforeSuite
    public void testStarts(){
        logger.info("Staring the test suite");
    }

    @BeforeTest
    public void beforeTest() {
        System.out.println("Starting the API test");
        // By having a RestAssured URI set implicitly in to rest assured
        // we just add a path to the post-call
        RestAssured.baseURI = ConfigReader.getProperty("PetStoreBaseURI");
    }

    @Test
    public void addPetToStore(){
        Category category = Category
                .builder()
                .id(10)
                .name("horse")
                .build();

        Tags tags0 = Tags
                .builder()
                .id(15)
                .name("unicorn")
                .build();

        Tags tags1 = Tags
                .builder()
                .id(16)
                .name("pearl")
                .build();

        AddAPet addAPet = AddAPet
                .builder()
                .id(8)
                .category(category)
                .name("rainbow")
                .photoUrls(Arrays.asList("My Horse's Photo URL"))
                .tags(Arrays.asList(tags0, tags1))
                .status("available")
                .build();


        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(addAPet)
                .when().post("/v2/pet")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();

        // getting the pet id from the response body
        int actualPetId = response.jsonPath().getInt("id");
        int actualTagId0 = response.jsonPath().getInt("tags[0].id");

        int actualPetIdWithJayWay = JsonPath.read(response.asString(), "id");
        logger.info("My id with JayWay is " + actualPetIdWithJayWay);

        int actualTagId0WithJayWay = JsonPath.read(response.asString(), "tags[0].id");
        logger.info("My pet tag Id with JayWay is " + actualTagId0WithJayWay);

        // getting the pet id from the request body
        //  int expectedPetId = addAPet.getId();
        int expectedPetId = 3;
        int expectedTagsId0 = tags0.getId();

        // We are logging the information
        logger.info("My actual pet id is " + actualPetId);

        //  We are debugging the assertion
        logger.debug("The actual pet id should be " + expectedPetId + " but we found " + actualPetId);
        // Assert.assertEquals(actualPetId, expectedPetId);

        // Assertion with Hamcrest
        assertThat(
                // the reason why we are asserting
                "I am checking if expected value is matching with the actual value ",
                // actual value
                actualPetIdWithJayWay,
                // expected value
                is(expectedPetId)
        );


        System.out.println("-----Update The Pet-----");
        Category updateCategory = Category
                .builder()
                .name("horse")
                .id(11)
                .build();

        UpdateAPet updateAPet = UpdateAPet
                .builder()
                .id(8)
                .category(updateCategory)
                .name("rainbow")
                .photoUrls(Arrays.asList("My Horse's Photo URL"))
                .tags(Arrays.asList(tags0, tags1))
                .status("unavailable")
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(updateAPet)
                .when().put("/v2/pet")
                .then().log().all()
                .assertThat().statusCode(200)
                .extract().response();
    }
}