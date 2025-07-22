package delarosa.utils;

import static io.restassured.RestAssured.*;
import io.restassured.response.Response;

public class OrderAPI {
    public static final String BASE_URL = "https://rahulshettyacademy.com";

    public static String placeOrder(String token, String productId) {
        String body = "{ \"orders\": [ { \"country\": \"Indonesia\", \"productOrderedId\": \"" + productId + "\" } ] }";

        Response response = given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .contentType("application/json")
                .body(body)
                .when()
                .post("/api/ecom/order/create-order")
                .then()
                .statusCode(201)
                .extract().response();

        return response.path("orders[0]");
    }

    public static Response getOrderDetails(String token, String orderId) {
        return given()
                .baseUri(BASE_URL)
                .header("Authorization", token)
                .queryParam("id", orderId)
                .when()
                .get("/api/ecom/order/get-orders-details");
    }
}
