package api.tdd.go_rest;

import api.pojo_classes.go_rest.CreateGoRestUserWithoutLombok;
import api.pojo_classes.go_rest.UpdateGoRestUserWithoutLombok;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import utils.ConfigReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

public class GoRestWithoutLombok {

    static Logger logger = LogManager.getLogger(GoRestWithoutLombok.class);

    Response response;
    /**
     * ObjectMapper is a class coming form Jackson to convert a Java object to Json
     */
    ObjectMapper objectMapper = new ObjectMapper();

    Faker faker = new Faker();

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
    public void goRestCRUD() throws JsonProcessingException {
        // Creating a POJO (Bean) object
        CreateGoRestUserWithoutLombok createGoRestUserWithoutLombok = new CreateGoRestUserWithoutLombok();

        // assigned the values to the attributes
        createGoRestUserWithoutLombok.setName("Tom");
        createGoRestUserWithoutLombok.setGender("male");
        createGoRestUserWithoutLombok.setEmail(faker.internet().emailAddress());
        createGoRestUserWithoutLombok.setStatus("active");

        System.out.println("======= Creating the user with POST request =========");

        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer cd6f43f79e931dc381c5c228f3e80c9f6990ec23e970e0325e206b9d241e551e")
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(createGoRestUserWithoutLombok))
                // .when().post("https://gorest.co.in/public/v2/users")
                .when().post("/public/v2/users")
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(201)
                //validating the response time is less than the specified one
                //.time(Matchers.lessThan(4000L))
                //validating the value of the body with hamcrest
                .body("name", equalTo("Tom"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();

        //expected status
        String expectedStatus = createGoRestUserWithoutLombok.getStatus();
        // actual status
        String actualStatus = JsonPath.read(response.asString(), "status");
        // Debug
        logger.debug("The expected status is " + expectedStatus +  " but we found " + actualStatus);
        // Hamcrest
        assertThat(
                // the reason why we are asserting
                "I am checking if expected status is matching with the actual status ",
                // actual value
                actualStatus,
                // expected value
                is(expectedStatus)
        );







        System.out.println("======= Fetching the user with GET request =========");

        expectedGoRestId = response.jsonPath().getInt("id");

        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer cd6f43f79e931dc381c5c228f3e80c9f6990ec23e970e0325e206b9d241e551e")
                .when().get("/public/v2/users/"+ expectedGoRestId)
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(200)
                //validating the response time is less than the specified one
                // .time(Matchers.lessThan(4000L))
                //validating the value from the body with hamcrest
                .body("name", equalTo("Tom"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();


        System.out.println("-==========Updating a User with PUT request============-");


        UpdateGoRestUserWithoutLombok updateGoRestUser = new UpdateGoRestUserWithoutLombok();
        updateGoRestUser.setName("TGlobal");
        updateGoRestUser.setEmail(faker.internet().emailAddress());

        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer cd6f43f79e931dc381c5c228f3e80c9f6990ec23e970e0325e206b9d241e551e")
                .body(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(updateGoRestUser))
//                .when().post("https://gorest.co.in/public/v2/users")
                .when().put("/public/v2/users/" + expectedGoRestId)
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(200)
                //validating the response time is less than the specified one
                //.time(Matchers.lessThan(6000L))
                //validating the value from the body with hamcrest
                .body("name", equalTo("TGlobal"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();

        expectedGoRestName = updateGoRestUser.getName();
        expectedGoRestEmail = updateGoRestUser.getEmail();
        expectedGoRestGender = createGoRestUserWithoutLombok.getGender();
        expectedGoRestStatus = createGoRestUserWithoutLombok.getStatus();

        //"id" in the getInt is the name of the attribute in the response body
        int actualGoRestId = response.jsonPath().getInt("id");
        String actualGoRestName = response.jsonPath().getString("name");
        String actualGoRestEmail = response.jsonPath().getString("email");
        String actualGoRestGender = response.jsonPath().getString("gender");
        String actualGoRestStatus = response.jsonPath().getString("status");

        Assert.assertEquals(actualGoRestId, expectedGoRestId);
        Assert.assertEquals(actualGoRestName, expectedGoRestName);
        Assert.assertEquals(actualGoRestEmail, expectedGoRestEmail);
        Assert.assertEquals(actualGoRestGender, expectedGoRestGender);
        Assert.assertEquals(actualGoRestStatus, expectedGoRestStatus);

        System.out.println("-==========Deleting a User with DELETE request============-");

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer cd6f43f79e931dc381c5c228f3e80c9f6990ec23e970e0325e206b9d241e551e")
                .when().delete("/public/v2/users/"+ expectedGoRestId)
                .then().log().all()
                .and().assertThat().statusCode(204)
                //.time(Matchers.lessThan(6000L))
                .extract().response();

    }
}