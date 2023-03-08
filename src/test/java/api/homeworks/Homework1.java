package api.homeworks;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class Homework1 {
    public static void main(String[] args) {

        Response response;

        Faker faker = new Faker();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"firstName\": \"" + faker.name().firstName() + "\",\n" +
                        "    \"lastName\": \"" + faker.name().lastName() + "\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"dob\": \"1992-12-15\"\n" +
                        "}")
                .when().post("https://tech-global-training.com/students")
                .then().log().all().extract().response();

        int postID = response.jsonPath().getInt("id");

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .when().get("https://tech-global-training.com/students")
                .then().log().all().extract().response();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .when().get("https://tech-global-training.com/students/" + postID)
                .then().log().all().extract().response();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"firstName\": \"" + faker.name().firstName() + "\",\n" +
                        "    \"lastName\": \"" + faker.name().lastName() + "\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"dob\": \"1992-12-15\"\n" +
                        "}")
                .when().put("https://tech-global-training.com/students/" + postID)
                .then().log().all().extract().response();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .body("{\n" +
                        "    \"firstName\": \"" + faker.name().firstName() + "\",\n" +
                        "    \"lastName\": \"" + faker.name().lastName() + "\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"dob\": \"1992-12-15\"\n" +
                        "}")
                .when().patch("https://tech-global-training.com/students/" + postID)
                .then().log().all().extract().response();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .when().delete("https://tech-global-training.com/students/" + postID)
                .then().log().all().extract().response();

        response = RestAssured
                .given().log().all()
                .header("Content-Type", "application/json")
                .when().delete("https://tech-global-training.com/students/deleteAll")
                .then().log().all().extract().response();
    }
}
