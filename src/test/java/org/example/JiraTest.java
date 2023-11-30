package org.example;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.annotations.Test;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraTest {
    @Test

    //add a comment
    public void addComment(){

        RestAssured.baseURI ="http://localhost:8080";

        //Login scenario
        SessionFilter session = new SessionFilter();

        given().header("Content-Type","application/json").body("{ \"username\": \"ayushigarg\", \"password\": \"Happylife16\" }").log().all()
              .filter(session).when().post("/rest/auth/1/session/")
                        .then().log().all().extract().response().asString();

//add comment
        given().pathParam("id","10004").body("{\n" +
                "    \"body\": \"added comment to add title new.\",\n" +
                "    \"visibility\": {\n" +
                "        \"type\": \"role\",\n" +
                "        \"value\": \"Administrators\"\n" +
                "    }\n" +
                "}").header("Content-Type","application/json")
                .filter(session).when().post("rest/api/2/issue/{id}/comment")
                .then().log().all().assertThat().statusCode(201).extract().response().asString();

        //Add attachment

     String comment =  given().header("X-Atlassian-Token" , "no-check").filter(session).pathParam("id","10004")
                .header("Content-Type","multipart/form-data")
                .multiPart("file", new File("jira.text"))
                .when().post("/rest/api/2/issue/{id}/attachments")
                .then().log().all().assertThat().statusCode(200).extract().response().asString();

     JsonPath jsComment = new JsonPath(comment);
    String commentId = jsComment.getString("id");



        //get issue

       String getIssueDetails =  given().filter(session).pathParam("id","10004")
               .queryParam("fields","comment")
                .when().get("/rest/api/2/issue/{id}").
                then().log().all().assertThat().statusCode(200).toString();
        System.out.println("get issue " +getIssueDetails);

       /* JsonPath js = new JsonPath(getIssueDetails);
       int commentCounts = js.getInt("fields.comment.comments.size()");
       for(int i=0; i<= commentCounts; i++){
           js.getInt("fields.comment.comments["+i+"].id");*/
       }


}
