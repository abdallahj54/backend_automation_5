package stepDef.api_step_def.tech_global;

import api.pojo_classes.tech_global.Students;
import com.github.javafaker.Faker;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static utils.Hooks.response;
import static utils.Hooks.techGlobalBaseUrl;

public class TechGlobalStepDef {

    private static Logger logger = LogManager.getLogger(TechGlobalStepDef.class);

    Faker faker = new Faker();

    @Given("Create an user with {string}, {string}, email, {string} and {string}")
    public void createAnUserWithEmailAnd(String expectedFirstName, String expectedLastName, String expectedDob, String urlPath) {
        Students students = Students
                .builder()
                .firstName(expectedFirstName)
                .lastName(expectedLastName)
                .email(faker.internet().emailAddress())
                .dob(expectedDob)
                .build();

        response = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(students)
                .when().post(techGlobalBaseUrl + urlPath)
                .then().log().all()
                .extract().response();
    }

//    @And("Validate the status code is {int}")
//    public void validateTheStatusCodeIs(int arg0) {
//
//    }
}
