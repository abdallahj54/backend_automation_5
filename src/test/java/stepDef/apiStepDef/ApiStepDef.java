package stepDef.apiStepDef;

import api.pojo_classes.go_rest.CreateGoRestUserWithLombok;
import api.tdd.go_rest.GoRestWithLombok;
import com.github.javafaker.Faker;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import utils.ConfigReader;

import static org.hamcrest.core.IsEqual.equalTo;

public class ApiStepDef {

    static Logger logger = LogManager.getLogger(GoRestWithLombok.class);

    Response response;

    Faker faker = new Faker();

    int goRestId;

    @BeforeTest
    public void beforeTest() {
        System.out.println("Starting the API test");

        RestAssured.baseURI = ConfigReader.getProperty("GoRestBaseURI");
    }

    @Given("I send a POST request with body")
    public void iSendAPOSTRequestWithBody() {
        CreateGoRestUserWithLombok createUser = CreateGoRestUserWithLombok
                // with the help of the Lombok, we are assigning the values to variables
                //coming from Bean class
                .builder()
                .name("Tech Global")
                .email(faker.internet().emailAddress())
                .gender("female")
                .status("active")
                .build();

        System.out.println("Creating the user");
        response = RestAssured
                .given().log().all()
//                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .header("Authorization", ConfigReader.getProperty("GoRestToken"))
                .body(createUser)
//                .when().post("https://gorest.co.in/public/v2/users")
                .when().post("/public/v2/users")
                .then().log().all()
                //validating the status code with rest assured
                .and().assertThat().statusCode(201)
                //validating the response time is less than the specified one
                //.time(Matchers.lessThan(3000L))
                //validating the value from the body with hamcrest
                .body("name", equalTo("Tech Global"))
                //validating the response content type
                .contentType(ContentType.JSON)
                .extract().response();
    }

    @Then("Status code is {int}")
    public void statusCodeIs(int arg0) {

        Assert.assertEquals(response.statusCode(), 201);
    }
}
