package site.nomoreparties.stellarburgers.helpers;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;

import static io.restassured.RestAssured.given;

/**
 * Вспомогательльные тестовые методы
 */

public class Steps extends Constants {
    protected ValidatableResponse createUser(UserData user) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(API_USER_REGISTER)
                .then();
        return response;
    }

    protected ValidatableResponse deleteUser(String accessToken) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .when()
                .delete(API_USER_DATA)
                .then();
        return response;
    }

    protected ValidatableResponse createUserString(UserData user) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user.buildJSONToString())
                .when()
                .post(API_USER_REGISTER)
                .then();
        return response;
    }

    protected ValidatableResponse login(String accessToken, UserData loginData) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(loginData.buildJSONToString())
                .when()
                .post(API_USER_LOGIN)
                .then().log().status();
        return response;
    }

    protected ValidatableResponse updateUserData(UserData userData, String accessToken) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(userData.buildJSONToString())
                .when()
                .patch(API_USER_DATA)
                .then().log().all();
        return response;
    }

    protected ValidatableResponse logout(String refreshToken) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body("{\"token\": " + "\"" + refreshToken + "\"}")
                .when()
                .post(API_USER_LOGOUT)
                .then().log().all();
        return response;
    }

    protected ValidatableResponse getIngredients() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .when()
                .get(API_INGREDIENTS)
                .then();
        return response;
    }

    protected ValidatableResponse createOrderUnauthorized(OrderData orderData) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(API_ORDERS)
                .then().log().all();
        return response;
    }

    protected ValidatableResponse createOrderAuthorized(String accessToken, OrderData orderData) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(API_ORDERS)
                .then().log().all();
        return response;
    }

}
