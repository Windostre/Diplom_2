package site.nomoreparties.stellarburgers.tests.order;

import clients.OrderData;
import helpers.Checks;
import helpers.Steps;
import helpers.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class OrderCreationUnauthorizedTests extends Steps {

    private final Utils utils = new Utils();
    private final Checks check = new Checks();
    private OrderData orderData;

    @Test
    @DisplayName("Создание заказа. Пользователь не авторизован. Успешно")
    @Description("Проверяет, что можно создать заказ неавторизованному пользователю." +
            "В ответе получены статус, имя и номер заказа")
    public void createOrderSuccessReturnStatus200ok() throws InterruptedException {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderUnauthorized(orderData);

        check.orderCreatedSuccessfully(response);
        check.orderCreatedHasName(response);
        check.orderCreatedHasOrderNumber(response);

    }

    @Test
    @DisplayName("Создание заказа без ингридиентов. Провал")
    @Description("Проверяет, что нельзя создать заказ без ингридиентов. Получен статус 400 и сообщение об ошибке")
    public void createOrderFailNoIngredientsReturnStatus404BadRequest() throws InterruptedException {
        List<String> ingredients = new ArrayList<>();
        orderData = new OrderData(ingredients);
        ValidatableResponse response = createOrderUnauthorized(orderData);

        check.orderCreateFail(response);
        check.orderCreateNoIngredientsErrorMessageIsCorrect(response);

    }

    @Test
    @DisplayName("Создание заказа только с невалидными ингридиентами. Провал")
    @Description("Проверяет, что нельзя создать заказ с несуществующими ингридиентами. Получен статус 500")
    public void createOrderFailInvalidIngredientsReturnStatus500InternalServerError() throws InterruptedException {
        orderData = utils.generateFakeIngredients();

        ValidatableResponse response = createOrderUnauthorized(orderData);

        check.orderCreateFailStatus500(response);

    }

    @Test
    @DisplayName("Создание заказа с валдиными и невалидными ингридиентов. Провал")
    @Description("Проверяет, что нельзя создать заказ, где присутсвуют несуществующие ингридиенты. Получен статус 500")
    public void createOrderFailInvalidAndValidIngredientsReturnStatus500InternalServerError() throws InterruptedException {
        orderData = utils.generateFakeAndValidIngredients(getIngredients());

        ValidatableResponse response = createOrderUnauthorized(orderData);

        check.orderCreateFailStatus500(response);

    }

}
