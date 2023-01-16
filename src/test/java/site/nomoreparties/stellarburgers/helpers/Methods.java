package site.nomoreparties.stellarburgers.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import site.nomoreparties.stellarburgers.pojo.User;

import static io.restassured.RestAssured.given;

/**
 * Вспомогательльные тестовые методы
 */

public class Methods extends Constants {
    protected ValidatableResponse createUser(User user) {
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
                .delete(API_USER_DELETE)
                .then();
        return response;
    }

    protected ValidatableResponse createUserString(User user) {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user.buildJSONToString())
                .when()
                .post(API_USER_REGISTER)
                .then();
        return response;
    }

    protected ValidatableResponse login(String accessToken, User loginData) {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .body(loginData.buildJSONToString())
                .when()
                .post(API_USER_LOGIN)
                .then();
        return response;
    }
}
