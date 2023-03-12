package api.tdd.go_rest;

import api.pojo_classes.go_rest.CreateGoRestUserWithLombok;
import api.pojo_classes.go_rest.UpdateGoRestUserWithLombok;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class GoRestWithLombokSchemaValidation {

    static Logger logger = LogManager.getLogger(GoRestWithLombokSchemaValidation.class);

    Response response;
    /**
     * ObjectMapper is a class coming form fasterxml to convert Java object to Json
     */
    ObjectMapper objectMapper = new ObjectMapper();

    Faker faker = new Faker();

    int goRestId;

    int expectedGoRestId;
    String expectedGoRestName;
    String expectedGoRestEmail;
    String expectedGoRestGender;
    String expectedGoRestStatus;

    @BeforeTest
    public void beforeTest() {
        System.out.println("Starting the API test");
        // By having RestAssured URI set implicitly in to rest assured
        // we just add path to the post call
        RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");
    }

    @Test
    public void goRestCRUDWithLombok() throws JsonProcessingException, FileNotFoundException {
        // Creating a POJO (Bean) object

        CreateGoRestUserWithLombok createUser = CreateGoRestUserWithLombok
                // with the help of the Lombok, we are assigning the values to variables
                //coming from Bean class
                .builder()
                .name("Tech Global")
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("active")
                .build();

        System.out.println("-----Creating the user-----\n");
        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(createUser)
                .when().post("/public/v2/users")
                .then().log().all()
                .and().assertThat().statusCode(201)
                .body("name", equalTo("Tech Global"))
                .contentType(ContentType.JSON)
                // validating the schema response body with
                //rest assured schema validation library
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchema(new FileInputStream("src/test/java/api/json_schema/go_rest/go_rest_post_response.json")))
                .extract().response();


        System.out.println("\n-----Fetching the user-----\n");
        goRestId = response.jsonPath().getInt("id");

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().get("/public/v2/users/" + goRestId)
                .then().log().all()
                .and().assertThat().statusCode(200)
                .body("name", equalTo("Tech Global"))
                .contentType(ContentType.JSON)
                .extract().response();


        System.out.println("\n-----Updating the user-----\n");
        UpdateGoRestUserWithLombok updateGoRestUserWithLombok = UpdateGoRestUserWithLombok
                //building the update java body
                .builder()
                .email(faker.internet().emailAddress())
                .gender("male")
                .status("inactive")
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(updateGoRestUserWithLombok)
                .when().put("/public/v2/users/" + goRestId)
                .then().log().all()
                .and().assertThat().statusCode(200)
                .body("name", equalTo("Tech Global"))
                .contentType(ContentType.JSON)
                .extract().response();


        System.out.println("\n-----Deleting the user-----\n");
//        /** -----------------------DELETE-----------------------*/
//
//        response = RestAssured
//                .given().log().all()
//                .contentType(ContentType.JSON)
//                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
//                .when().delete("/public/v2/users/" + goRestId)
//                .then().log().all()
//                .and().assertThat().statusCode(204)
//                //validating the response time is less than the specified one
//               // .time(Matchers.lessThan(3000L))
//                .extract().response();


        System.out.println("\n-----HamCrest-----\n");
        // find expected name with LomBok
        String expectedName = createUser.getName();

        // find actual name with JayWay
        String actualName = JsonPath.read(response.asString(), "name");

        // debug wit logger
        logger.debug("The name value should be " + actualName + " but we found " + expectedName);

        // assert it with hamcrest
        assertThat(
                // the reason why we are asserting
                "I am checking if expected name is matching with the actual name ",
                // actual value
                actualName,
                // expected value
                is(expectedName)
        );
    }
}
