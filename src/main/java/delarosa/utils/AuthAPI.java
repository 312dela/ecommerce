package delarosa.utils;

import io.restassured.RestAssured;

public class AuthAPI {
    public static String loginAndGetToken(String email, String password) {
        return RestAssured.given()
                .baseUri("https://rahulshettyacademy.com")
                .contentType("application/json")
                .body("{\"userEmail\":\"" + email + "\",\"userPassword\":\"" + password + "\"}")
                .post("/api/ecom/auth/login")
                .then().extract().path("token");
    }
}
