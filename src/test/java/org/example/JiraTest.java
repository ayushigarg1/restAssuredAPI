package org.example;


import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import java.io.File;

import static io.restassured.RestAssured.given;

public class JiraTest {
    public static void main(String[] args) {

        //Login scenario api
        SessionFilter session = new SessionFilter();
        RestAssured.baseURI = "http://localhost:8085";

        String response = given().relaxedHTTPSValidation().header("Content-Type", "application/json").body("{ \"username\": \"ayushigarg\", \"password\": \"Ayushi123\" }").log().all().filter(session).when().post("/rest/auth/1/session/").then().extract().response().asString();

        String expectedMsg = "added comment to add title.";
//add comment
        String addCommentResponse = given().pathParam("id", "10024").log().all().header("Content-Type", "application/json").body("{\n" + "    \"body\": \"" + expectedMsg + "\",\n" + "    \"visibility\": {\n" + "        \"type\": \"role\",\n" + "        \"value\": \"Administrators\"\n" + "    }\n" + "}").filter(session).when().post("/rest/api/2/issue/{id}/comment").then().log().all().assertThat().statusCode(201).extract().response().asString();
        JsonPath commentJs = new JsonPath(addCommentResponse);
        String commentId = commentJs.getString("id");
        System.out.println("comment id is " + commentId);

        //add attachment to an issue

        given().header("X-Atlassian-Token", "no-check").filter(session).pathParam("id", "10024").header("Content-Type", "multipart/form-data").multiPart("file", new File("jira.text")).when().post("rest/api/2/issue/{id}/attachments").then().log().all().assertThat().statusCode(200);


        //Get issue
        String issueDetails = given().filter(session).pathParam("id", "10024").queryParam("fields", "comment").log().all().when().get("/rest/api/2/issue/{id}").then().log().all().extract().response().asString();
        System.out.println("issue is " + issueDetails);
        JsonPath js1 = new JsonPath(issueDetails);
        int commentsCount = js1.getInt("fields.comment.comments.size()");

        for (int i = 0; i < commentsCount; i++) {
            String commentIdIssue = js1.get("fields.comment.comments[" + i + "].id").toString();

            if (commentIdIssue.equalsIgnoreCase(commentId)) {
                String message = js1.get("fields.comment.comments[" + i + "].body").toString();

                System.out.println("messgae is = " + message);

                Assert.assertEquals(message, expectedMsg);

            }


        }
    }


}



