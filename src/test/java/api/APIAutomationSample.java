package api;

import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

public class APIAutomationSample {
    public static void main(String[] args) {

        /**
         * Response is an interface coming from the RestAssured Library
         * The Response variable "response" stores all the components of the API calls
         * including the request and response
         * RestAssured is written with BDD flow
         */

        Response response;

        Faker faker = new Faker();

        response = RestAssured //creating a user
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .body("{\n" +
                        "    \"name\": \"" + faker.name().fullName() + "\",\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when().post("https://gorest.co.in/public/v2/users")
                .then().log().all().extract().response();

        int postID = response.jsonPath().getInt("id");
        System.out.println("ID is coming from response " + postID);

        response = RestAssured //get a single user
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .when().get("https://gorest.co.in/public/v2/users/" + postID)
                .then().log().all().extract().response();

        response = RestAssured //get all users
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .when().get("https://gorest.co.in/public/v2/users/")
                .then().log().all().extract().response();

        response = RestAssured //update specific user using post
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .body("{\n" +
                        "    \"name\": \"" + faker.name().fullName() + "\",\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when().put("https://gorest.co.in/public/v2/users/" + postID)
                .then().log().all().extract().response();

        response = RestAssured //update specific user using patch
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .body("{\n" +
                        "    \"name\": \"" + faker.name().fullName() + "\",\n" +
                        "    \"gender\": \"male\",\n" +
                        "    \"email\": \"" + faker.internet().emailAddress() + "\",\n" +
                        "    \"status\": \"active\"\n" +
                        "}")
                .when().patch("https://gorest.co.in/public/v2/users/" + postID)
                .then().log().all().extract().response();

        int patchID = response.jsonPath().getInt("id");
        Assert.assertEquals(postID, patchID, "Expected ID is " + postID + " and we found " + patchID);

        response = RestAssured //delete specific user
                .given().log().all()
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer 714f48f55e3bc44a6de28e6140db9d8c69b45e20110d3b2de1e903a402941ca2")
                .when().delete("https://gorest.co.in/public/v2/users/" + postID)
                .then().log().all().extract().response();
    }
}
