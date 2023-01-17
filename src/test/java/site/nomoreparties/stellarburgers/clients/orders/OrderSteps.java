package site.nomoreparties.stellarburgers.clients.orders;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.helpers.Constants;

import static io.restassured.RestAssured.given;

/**
 * Вспомогательльные тестовые методы
 */
public class OrderSteps extends Constants {
    protected ValidatableResponse getIngredients() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .when()
                .get(API_INGREDIENTS)
                .then();
        return response;
    }

    protected ValidatableResponse createOrder(OrderData orderData) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(API_ORDERS)
                .then().log().all();
        return response;
    }


}
