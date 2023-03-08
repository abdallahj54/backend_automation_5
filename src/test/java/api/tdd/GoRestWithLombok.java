package api.tdd;

import api.pojo_classes.go_rest.CreateGoRestUserWithLombok;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigReader;

import static org.hamcrest.core.IsEqual.equalTo;

public class GoRestWithLombok {

    Response response;

    /**
     * ObjectMapper is a class coming from fasterxml to convert Java object to JSON
     */

    Faker faker = new Faker();

    int expectedGoRestId;
    String expectedGoRestName;
    String expectedGoRestEmail;
    String expectedGoRestGender;
    String expectedGoRestStatus;

    @BeforeTest
    public void beforeTest(){
        System.out.println("Starting the API Test");
        /**
         * By having RestAssured URI set implicitly into rest assured
         * we just add a path to the post call
         */
        RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");
    }

    @Test
    public void goRestCRUDWithLombok() throws JsonProcessingException {
        // Creating a POJO(Bean) request body with lombok (like an object)
        CreateGoRestUserWithLombok createUser = CreateGoRestUserWithLombok
                .builder()
                .name("Abdallah Jaber")
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("active")
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(createUser)
                .when().post("/public/v2/users")
                .then().log().all()
                .and().assertThat().statusCode(201)
                .time(Matchers.lessThan(10000L))
                .body("name", equalTo("Abdallah Jaber"))
                .contentType(ContentType.JSON)
                .extract().response();
    }
}
