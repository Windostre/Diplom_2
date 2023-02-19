package helpers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static org.hamcrest.Matchers.*;

/**
 * Общий класс проверок
 */
public class Checks {

    @Step("Проверка. Пользователь успешно авторизован")
    public void userLoginSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Пользователь не смог авторизоваться")
    public void userLoginFailed(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке авторизации корректно")
    public void userLoginFailMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Step("Проверка. Пользователь успешно создан")
    public void userCreateSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Получен accessToken")
    public void accessTokenReceived(ValidatableResponse response) {
        response.assertThat()
                .body("accessToken", startsWith("Bearer "));
    }

    @Step("Проверка. Получен refreshToken")
    public void refreshTokenReceived(ValidatableResponse response) {
        response.assertThat()
                .body("refreshToken", is(notNullValue()));
    }

    @Step("Проверка. Пользователь не создан")
    public void userCreateFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке создания корректно")
    public void userCreateValidationErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Step("Проверка. Сообщение об ошибке создания корректно")
    public void userCreateDublicateErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("User already exists"));
    }

    @Step("Проверка. Пользователь не создан. Ожидаемый статус не 200")
    public void userCreateFailStatusIsNot200(ValidatableResponse response) {
        response.assertThat()
                .statusCode(is(not(200)))
                .body("success", is(false));
    }

    @Step("Проверка. Данные пользователя не обновлены")
    public void userUpdateUnauthorisedFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public void userUpdateUnauthorizedErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("You should be authorised"));
    }


    @Step("Проверка. Данные пользователя успешно обновлены")
    public void userUpdateSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. Почта пользователя успешно обновлена")
    public void userUpdateNewEmailIsSet(ValidatableResponse response, String newEmail) {
        response.assertThat()
                .body("user.email", equalTo(newEmail));
    }

    @Step("Проверка. Имя пользователя успешно обновлено")
    public void userUpdateNewNameIsSet(ValidatableResponse response, String newEmail) {
        response.assertThat()
                .body("user.name", equalTo(newEmail));
    }

    @Step("Проверка. Данные пользователя не обновлены. Ожидаемый статус не 200")
    public void userUpdateFailStatusIsNot200(ValidatableResponse response) {
        response.assertThat()
                .statusCode(is(not(200)))
                .body("success", is(false));
    }

    @Step("Проверка. Данные пользователя не обновлены")
    public void userUpdateDublicateEmailFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(403)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public void userUpdateDublicateErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("User with such email already exists"));
    }

    @Step("Проверка. Заказ создан успешно")
    public void orderCreatedSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. В заказе есть наименование")
    public void orderCreatedHasName(ValidatableResponse response) {
        response.assertThat()
                .body("name", notNullValue());
    }

    @Step("Проверка. В заказу присвоен id")
    public void orderCreatedHasOrderId(ValidatableResponse response) {
        response.assertThat()
                .body("order._id", notNullValue());
    }

    @Step("Проверка. В заказе переданы ингредиенты")
    public void orderCreatedHasIngredients(ValidatableResponse response) {
        response.assertThat()
                .body("order.ingredients", notNullValue());
    }

    @Step("Проверка. В заказе переданы данные пользователя")
    public void orderCreatedHasUserData(ValidatableResponse response, String ownerName, String ownerEmail) {
        response.assertThat()
                .body("order.owner.name", equalTo(ownerName))
                .body("order.owner.email", equalTo(ownerEmail));
    }

    @Step("Проверка. В заказе передана есть дата создания заказа")
    public void orderCreatedHasDateCreatedAt(ValidatableResponse response) {
        response.assertThat()
                .body("order.owner.createdAt", notNullValue());
    }

    @Step("Проверка. В заказе передана есть дата обновления заказа")
    public void orderCreatedHasDateUpdatedAt(ValidatableResponse response) {
        response.assertThat()
                .body("order.owner.updatedAt", notNullValue());
    }

    @Step("Проверка. В заказ в статусе Done")
    public void orderCreatedHasStatusDone(ValidatableResponse response) {
        response.assertThat()
                .body("order.status", equalTo("done"));
    }

    @Step("Проверка. В заказу присвоен номер")
    public void orderCreatedHasOrderNumber(ValidatableResponse response) {
        response.assertThat()
                .body("order.number", greaterThan(0));
    }

    @Step("Проверка. В заказе есть цена")
    public void orderCreatedHasPrice(ValidatableResponse response) {
        response.assertThat()
                .body("order.number", greaterThan(0));
    }

    @Step("Проверка. Заказ не создан")
    public void orderCreateFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(400)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public void orderCreateNoIngredientsErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Step("Проверка. Заказ не создан")
    public void orderCreateFailStatus500(ValidatableResponse response) {
        response.assertThat()
                .statusCode(500);
    }

    @Step("Проверка. Заказы пользователя не получены")
    public void getUserOrderFail(ValidatableResponse response) {
        response.assertThat()
                .statusCode(401)
                .body("success", is(false));
    }

    @Step("Проверка. Сообщение об ошибке корректно")
    public void getUserOrderErrorMessageIsCorrect(ValidatableResponse response) {
        response.assertThat()
                .body("message", equalTo("You should be authorised"));
    }

    @Step("Проверка. Заказы пользователя получены успешно")
    public void getUserOrderSuccess(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Step("Проверка. В заказы пользователя не пустые")
    public void getUserOrderAreNotEmpty(ValidatableResponse response) {
        response.assertThat()
                .body("orders", not(emptyArray()));
    }

    @Step("Проверка. В заказах полученных передан правильный id заказа")
    public void getUserOrderHasProperOrderId(ValidatableResponse response, String createdOrderId) {
        response.assertThat()
                .body("orders._id[0]", equalTo(createdOrderId));
    }

    @Step("Проверка. Получены все заказаы пользователя")
    public void getUserOrderHasAllUserOrdersOnly(ValidatableResponse response, int ordersQuantity) {
        response.assertThat()
                .body("total", equalTo(ordersQuantity));
    }


}
