package org.example;

import files.Payload;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ComplexJsonParse {
    public static void main(String[] args) {
        JsonPath jsonPath = new JsonPath(Payload.CoursePrice());

        //print no. of courses

        int courseNumber = jsonPath.getInt("courses.size()");
        System.out.println("course " + courseNumber);

        //Print Purchase Amount

        int purchaseAmt = jsonPath.getInt("dashboard.purchaseAmount");
        System.out.println("purchase amount " + purchaseAmt);

        //Print Title of the first course
        String title = jsonPath.getString("courses.get(0).title");
        System.out.println("1st title is " + title);

        // Print All course titles and their respective Prices
        for (int i = 0; i < courseNumber; i++) {
            String courses = jsonPath.getString("courses.get(" + i + ").title");
            int coursePrice = jsonPath.getInt("courses.get(" + i + ").price");
            System.out.println("course name " + courses + " and Prices is " + coursePrice);

            //Print no of copies sold by RPA Course
            for (int j = 0; j < courseNumber; j++) {
                String coursesTitle = jsonPath.getString("courses.get(" + j + ").title");
                if (coursesTitle.equalsIgnoreCase("RPA")) {
                    int copies = jsonPath.getInt("courses.get(" + j + ").copies");
                    System.out.println("RPA copies number " + copies);
                    break;
                }

            }
            //Verify if Sum of all Course prices matches with Purchase Amount
            double sum = 0;
            for (int k = 0; k < courseNumber; k++) {

                int allCopies = jsonPath.getInt("courses.get(" + k + ").copies");
                int copiesPrice = jsonPath.getInt("courses.get(" + k + ").price");
                System.out.println("all copies " + allCopies);
                double amount = copiesPrice * allCopies;
                System.out.println("Amount" + amount);

                sum = sum + amount;

            }
            System.out.println("sum is " + sum);
            Assert.assertEquals(sum, purchaseAmt);

        }


    }
}
