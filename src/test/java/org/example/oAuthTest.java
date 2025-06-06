package org.example;

import groovyjarjarantlr4.v4.runtime.atn.SemanticContext;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class oAuthTest {
    public static void main(String[] args) {

        String[] courseTitle = {"Selenium Webdriver Java", "Cypress", "Protractor"};

//basic auth
        given()
                .baseUri("https://api.example.com")
                .header("Authorization", "Bearer xyz123")
                .queryParam("type", "admin")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10));

//OR
        given().auth().basic("username", "password")
                .when()
                .get("https://api.example.com/users")
                .then()
                .statusCode(200)
                .body("size()", equalTo(10));

        //get authorization Code
        String url = "https://rahulshettyacademy.com/getCourse.php?state=test&code=4%2F0AfJohXnGOxfUovloL4Sx0oard3qt-DtSzEPmWmcVkmtS6N1QlTjswzMJ6bLeVU5L0g1qpQ&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
        String partialCode = url.split("code=")[1];
        String code = partialCode.split("&scope")[0];
        System.out.println("code is " + code);


//exchange code
        RestAssured.baseURI = "https://www.googleapis.com";

        String accessTokenResponse = given().urlEncodingEnabled(false)
                .queryParams("code", code)
                .queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
                .queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
                .queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
                .queryParams("grant_type", "authorization_code").log().all()
                .when()
                .post("/oauth2/v4/token").asString();
        //   .then().extract().asString();

        System.out.println("accessTokenResponse : " + accessTokenResponse);

        JsonPath js = new JsonPath(accessTokenResponse);
        String accessToken = js.getString("access_token");

// Restassured to understand in which format you are passing response

        GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
                .when().get("https://rahulshettyacademy.com/getCourse.php").then()
                .extract().response().as(GetCourse.class);
        System.out.println(gc.getLinkedIn());
        System.out.println(gc.getInstructor());
        System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());

        List<Api> apiCourses = gc.getCourses().getApi();
        for (int i = 0; i < apiCourses.size(); i++) {
            if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing")) {
                System.out.println(apiCourses.get(i).getPrice());
            }
        }
        //print course title names of WebAutomation

        List<WebAutomation> webAutomation = gc.getCourses().getWebAutomation();
        ArrayList<String> actualList = new ArrayList<String>();

        for (int i = 0; i < webAutomation.size(); i++) {
            System.out.println(webAutomation.get(i).getCourseTitle());
            actualList.add(webAutomation.get(i).getCourseTitle());

        }
        List<String> expectedResult = Arrays.asList(courseTitle);
        Assert.assertTrue(actualList.equals(expectedResult));

    }
}
