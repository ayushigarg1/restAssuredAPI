package org.example;

import files.ReusableMethod;
import files.Payload;
import io.restassured.RestAssured;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class DemoTest {
    public static void main(String[] args) {

        //validate add place api
        //Given, when, then
        RestAssured.baseURI = "https://rahulshettyacademy.com";
        String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
                .body(Payload.AddPlace()
                .when().post("maps/api/place/add/json")
                .then().assertThat().statusCode(200).body("scope", equalTo("APP"))
                .header("Server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();

        System.out.println("Response is " + response);
        //retrieve place id

        String placeId = ReusableMethod.rawToJson(response).getString("place_id");
        System.out.println("place id is " + placeId);

        //update place
        String newAddress = "test address";
        given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body("{\n" +
                "\"place_id\":\"" +placeId+"\",\n" +
                "\"address\":\""+newAddress+"\",\n" +
                "\"key\":\"qaclick123\"\n" +
                "}")
                .when().put("maps/api/place/update/json")
                .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));

       //Get place

     String getPlaceResponse =   given().log().all().queryParam("key", "qaclick123")
                .queryParam("place_id", placeId)
                .when().get("maps/api/place/get/json")
                .then().assertThat().log().all().statusCode(200).extract().response().asString();


    String actualAddress= ReusableMethod.rawToJson(getPlaceResponse).getString("address");
        System.out.println("actual address " + actualAddress);
        //TestnG asserts
        Assert.assertEquals(newAddress,actualAddress);



        //Add place->update place with new address-> get place to validate if new address is present in response
    }

}
