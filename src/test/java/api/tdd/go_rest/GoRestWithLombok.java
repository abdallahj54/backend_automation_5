package api.tdd.go_rest;

import api.pojo_classes.go_rest.CreateGoRestUserWithLombok;
import api.pojo_classes.go_rest.UpdateGoRestUserWithLombok;
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

    Faker faker = new Faker();

    int goRestId;

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
        System.out.println("\n-----Creating the User-----\n");
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
                .time(Matchers.lessThan(3000L))
                .body("name", equalTo("Abdallah Jaber"))
                .contentType(ContentType.JSON)
                .extract().response();

        goRestId = response.jsonPath().getInt("id");


        System.out.println("\n-----Getting the User-----\n");
        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().get("/public/v2/users/" + goRestId)
                .then().log().all()
                .and().assertThat().statusCode(200)
                .time(Matchers.lessThan(3000L))
                .body("name", equalTo("Abdallah Jaber"))
                .contentType(ContentType.JSON)
                .extract().response();


        System.out.println("\n-----Updating the User-----\n");
        UpdateGoRestUserWithLombok updateUser = UpdateGoRestUserWithLombok
                .builder()
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("inactive")
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(updateUser)
                .when().patch("/public/v2/users/" + goRestId)
                .then().log().all()
                .and().assertThat().statusCode(200)
                .time(Matchers.lessThan(3000L))
                .body("name", equalTo("Abdallah Jaber"))
                .contentType(ContentType.JSON)
                .extract().response();


        System.out.println("\n-----Deleting the User-----\n");
        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .when().delete("/public/v2/users/" + goRestId)
                .then().log().all()
                .and().assertThat().statusCode(204)
                .time(Matchers.lessThan(3000L))
                .extract().response();
    }
}
