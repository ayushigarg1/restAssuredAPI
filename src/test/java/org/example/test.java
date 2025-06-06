package org.example;

import io.restassured.RestAssured;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class    test {

  public static  void main(String[]args)
  {
      RestAssured.baseURI =  ("http://localhost:8080");

      given().header("Content-Type", "application/json").log().all()
              .when().get("/employee/ge")
              .then().log().all().extract().response();
      String newAddress = "test address";
        //update place
      given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json").body("{\n" +
                      "\"place_id\":\"" +"\",\n" +
                      "\"address\":\""+newAddress+"\",\n" +
                      "\"key\":\"qaclick123\"\n" +
                      "}")
              .when().put("maps/api/place/update/json")
              .then().assertThat().log().all().statusCode(200).body("msg",equalTo("Address successfully updated"));




  }
}
