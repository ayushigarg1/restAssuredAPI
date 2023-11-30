package org.example;

import files.Payload;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;

public class DynamicPayload {

    @Test(dataProvider = "BooksData", priority = 1)
    public void addBook(String isbn, String aisle) {
        RestAssured.baseURI = "http://216.10.245.166";
        String response = given().header("Content-Type", "application/json").body(Payload.addBook(isbn, aisle)).when().post("Library/Addbook.php").then().assertThat().statusCode(200).body("Msg", equalTo("successfully added")).extract().response().asString();

        System.out.println("Add book " + response);
        JsonPath jsonPath = new JsonPath(response);
        String retrieveId = jsonPath.getString("ID");
        System.out.println("Book ID is " + retrieveId);
    }

    @Test(dataProvider = "BooksData", priority = 2)
    public void deleteBook(String isbn, String aisle) {
        String id = isbn + aisle;
        System.out.println("id is " + id);
        //deleteBook
        RestAssured.baseURI = "http://216.10.245.166";
        String deleteResponse = given().header("Content-Type", "application/json").body(Payload.deleteBook(id)).when().delete("Library/DeleteBook.php").then().assertThat().statusCode(200).extract().response().asString();

        System.out.println("delete response is " + deleteResponse);

    }


    @DataProvider(name = "BooksData")
    public Object[][] getData() {
        return new Object[][]{{"testt", "12334"}, {"testt1", "453454"}, {"testt2", "83787"}};
    }
}
