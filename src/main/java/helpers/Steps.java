package helpers;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import clients.OrderData;
import clients.UserData;

import static io.restassured.RestAssured.given;

/**
 * Вспомогательльные тестовые методы
 */

public class Steps extends Constants {
    protected RequestSpecification spec() throws InterruptedException {
        Thread.sleep(0);//при 429 добавить ожидание
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BURGER_BASE_URI);
    }

    @Step("Создать пользователя")
    protected ValidatableResponse createUser(UserData user) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .body(user)
                .when()
                .post(USER_REGISTER)
                .then();
        return response;
    }

    @Step("Удалить пользователя")
    protected ValidatableResponse deleteUser(String accessToken) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .header("Authorization", accessToken)
                .when()
                .delete(USER_DATA)
                .then().log().all();
        return response;
    }

    @Step("Создать пользователя")
    protected ValidatableResponse createUserString(UserData user) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .body(user.buildJSONToString())
                .when()
                .post(USER_REGISTER)
                .then();
        return response;
    }

    @Step("Авторизоваться")
    protected ValidatableResponse login(UserData loginData) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .body(loginData.buildJSONToString())
                .when()
                .post(USER_LOGIN)
                .then().log().status();
        return response;
    }

    @Step("Обновить данные пользователя")
    protected ValidatableResponse updateUserData(UserData userData, String accessToken) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .header("Authorization", accessToken)
                .body(userData.buildJSONToString())
                .when()
                .patch(USER_DATA)
                .then().log().all();
        return response;
    }

    @Step("Выйти из системы")
    protected ValidatableResponse logout(String refreshToken) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .body("{\"token\": " + "\"" + refreshToken + "\"}")
                .when()
                .post(USER_LOGOUT)
                .then().log().all();
        return response;
    }

    @Step("Получить значения ингридиентов")
    protected ValidatableResponse getIngredients() throws InterruptedException {
        ValidatableResponse response = given()
                .spec(spec())
                .when()
                .get(INGREDIENTS)
                .then();
        return response;
    }

    @Step("Создать заказ без авторизации")
    protected ValidatableResponse createOrderUnauthorized(OrderData orderData) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .body(orderData)
                .when()
                .post(ORDERS)
                .then().log().all();
        return response;
    }

    @Step("Создать заказ с авторизацией")
    protected ValidatableResponse createOrderAuthorized(String accessToken, OrderData orderData) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .header("Authorization", accessToken)
                .body(orderData)
                .when()
                .post(ORDERS)
                .then().log().all();
        return response;
    }

    @Step("Получить список заказов")
    protected ValidatableResponse getOrders(String accessToken) throws InterruptedException {
        ValidatableResponse response = given().log().all()
                .spec(spec())
                .header("Authorization", accessToken)
                .when()
                .get(ORDERS)
                .then().log().all();
        return response;
    }
}
