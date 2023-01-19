package site.nomoreparties.stellarburgers.helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

/**
 * Общий класс проверок
 */
public class Checks {

    @Step("Проверка. Пользователь успешно авторизован")
    public static void checkUserLoginSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Пользователь не смог авторизоваться")
    public static void checkUserLoginFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке авторизации корректно")
    public static void checkUserLoginFailMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка. Пользователь успешно создан")
    public static void checkUserCreateSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Получен accessToken")
    public static void checkAccessTokenReceived(ValidatableResponse response) {
        response.assertThat()
                .body("accessToken", startsWith("Bearer "));
    }

    @Step("Проверка. Получен refreshToken")
    public static void checkRefreshTokenReceived(ValidatableResponse response) {
        response.assertThat()
                .body("refreshToken", is(notNullValue()));
    }

    @Step("Проверка. Пользователь не создан")
    public static void checkUserCreateFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке создания корректно")
    public static void checkUserCreateValidationErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка. Сообщение об ошибке создания корректно")
    public static void checkUserCreateDublicateErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка. Пользователь не создан. Ожидаемый статус не 200")
    public static void checkUserCreateFailStatusIsNot200(ValidatableResponse response) {
        response.assertThat()
                .statusCode(is(not(200)))
                .body("success", is(false));
    }

    @Step("Проверка. Данные пользователя не обновлены")
    public static void checkUserUpdateUnauthorisedFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public static void checkUserUpdateUnauthorizedErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("You should be authorised"));
    }


    @Step("Проверка. Данные пользователя успешно обновлены")
    public static void checkUserUpdateSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Почта пользователя успешно обновлена")
    public static void checkUserUpdateNewEmailIsSet(ValidatableResponse response, String newEmail) {
        response.assertThat()
                .body("user.email", equalTo(newEmail));
    }

    @Step("Проверка. Имя пользователя успешно обновлено")
    public static void checkUserUpdateNewNameIsSet(ValidatableResponse response, String newEmail) {
        response.assertThat()
                .body("user.name", equalTo(newEmail));
    }

    @Step("Проверка. Данные пользователя не обновлены. Ожидаемый статус не 200")
    public static void checkUserUpdateFailStatusIsNot200(ValidatableResponse response) {
        response.assertThat()
                .statusCode(is(not(200)))
                .body("success", is(false));
    }

    @Step("Проверка. Данные пользователя не обновлены")
    public static void checkUserUpdateDublicateEmailFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public static void checkUserUpdateDublicateErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("User with such email already exists"));
    }

    @Step("Проверка. Заказ создан успешно")
    public static void checkOrderCreatedSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. В заказе есть наименование")
    public static void checkOrderCreatedHasName(ValidatableResponse response) {
        response.assertThat()
                .body("name", notNullValue());
    }

    @Step("Проверка. В заказу присвоен id")
    public static void checkOrderCreatedHasOrderId(ValidatableResponse response) {
        response.assertThat()
                .body("order._id", notNullValue());
    }

    @Step("Проверка. В заказе переданы ингредиенты")
    public static void checkOrderCreatedHasIngredients(ValidatableResponse response) {
        response.assertThat()
                .body("order.ingredients", notNullValue());
    }

    @Step("Проверка. В заказе переданы данные пользователя")
    public static void checkOrderCreatedHasUserData(ValidatableResponse response, String ownerName, String ownerEmail) {
        response.assertThat()
                .body("order.owner.name", equalTo(ownerName))
                .body("order.owner.email", equalTo(ownerEmail));
    }

    @Step("Проверка. В заказе передана есть дата создания заказа")
    public static void checkOrderCreatedHasDateCreatedAt(ValidatableResponse response) {
        response.assertThat()
                .body("order.owner.createdAt", notNullValue());
    }

    @Step("Проверка. В заказе передана есть дата обновления заказа")
    public static void checkOrderCreatedHasDateUpdatedAt(ValidatableResponse response) {
        response.assertThat()
                .body("order.owner.updatedAt", notNullValue());
    }

    @Step("Проверка. В заказ в статусе Done")
    public static void checkOrderCreatedHasStatusDone(ValidatableResponse response) {
        response.assertThat()
                .body("order.status", equalTo("done"));
    }

    @Step("Проверка. В заказу присвоен номер")
    public static void checkOrderCreatedHasOrderNumber(ValidatableResponse response) {
        response.assertThat()
                .body("order.number", greaterThan(0));
    }

    @Step("Проверка. В заказе есть цена")
    public static void checkOrderCreatedHasPrice(ValidatableResponse response) {
        response.assertThat()
                .body("order.number", greaterThan(0));
    }

    @Step("Проверка. Заказ не создан")
    public static void checkOrderCreateFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(400)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public static void checkOrderCreateNoIngredientsErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка. Заказ не создан")
    public static void checkOrderCreateFailStatus500(ValidatableResponse response) {
        response.assertThat()
                .statusCode(500);
    }

    @Step("Проверка. Заказы пользователя не получены")
    public static void checkGetUserOrderFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public static void checkGetUserOrderErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка. Заказы пользователя получены успешно")
    public static void checkGetUserOrderSuccess(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. В заказы пользователя не пустые")
    public static void checkGetUserOrderAreNotEmpty(ValidatableResponse response) {
        response.assertThat()
                .body("orders", not(emptyArray()));
    }

    @Step("Проверка. В заказах полученных передан правильный id заказа")
    public static void checkGetUserOrderHasProperOrderId(ValidatableResponse response, String createdOrderId) {
        response.assertThat()
                .body("orders._id[0]", equalTo(createdOrderId));
    }

    @Step("Проверка. Получены все заказаы пользователя")
    public static void checkGetUserOrderHasAllUserOrdersOnly(ValidatableResponse response, int ordersQuantity) {
        response.assertThat()
                .body("total", equalTo(ordersQuantity));
    }


}
