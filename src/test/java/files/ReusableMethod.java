package files;

import io.restassured.path.json.JsonPath;

public class ReusableMethod {

    public static JsonPath rawToJson(String response){
        return new JsonPath(response);

    }
}
