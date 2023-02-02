package site.nomoreparties.stellarburgers.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;

import static io.restassured.RestAssured.given;

/**
 * Вспомогательльные тестовые методы
 */

public class Steps extends Constants {
    @Step("Создать пользователя")
    protected ValidatableResponse createUser(UserData user) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_REGISTER)
                .then();
        return response;
    }

    @Step("Удалить пользователя")
    protected ValidatableResponse deleteUser(String accessToken) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .when()
                .delete(USER_DATA)
                .then().log().all();
        return response;
    }

    @Step("Создать пользователя")
    protected ValidatableResponse createUserString(UserData user) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user.buildJSONToString())
                .when()
                .post(USER_REGISTER)
                .then();
        return response;
    }

    @Step("Авторизоваться")
    protected ValidatableResponse login(UserData loginData) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(loginData.buildJSONToString())
                .when()
                .post(USER_LOGIN)
                .then().log().status();
        return response;
    }

    @Step("Обновить данные пользователя")
    protected ValidatableResponse updateUserData(UserData userData, String accessToken) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(userData.buildJSONToString())
                .when()
                .patch(USER_DATA)
                .then().log().all();
        return response;
    }

    @Step("Выйти из системы")
    protected ValidatableResponse logout(String refreshToken) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body("{\"token\": " + "\"" + refreshToken + "\"}")
                .when()
                .post(USER_LOGOUT)
                .then().log().all();
        return response;
    }

    @Step("Получить значения ингридиентов")
    protected ValidatableResponse getIngredients() {
        ValidatableResponse response = given()
                .header("Content-type", "application/json")
                .when()
                .get(INGREDIENTS)
                .then();
        return response;
    }

    @Step("Создать заказ без авторизации")
    protected ValidatableResponse createOrderUnauthorized(OrderData orderData) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(ORDERS)
                .then().log().all();
        return response;
    }

    @Step("Создать заказ с авторизацией")
    protected ValidatableResponse createOrderAuthorized(String accessToken, OrderData orderData) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(orderData)
                .when()
                .post(ORDERS)
                .then().log().all();
        return response;
    }

    @Step("Получить список заказов")
    protected ValidatableResponse getOrders(String accessToken) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .when()
                .get(ORDERS)
                .then().log().all();
        return response;
    }
}
