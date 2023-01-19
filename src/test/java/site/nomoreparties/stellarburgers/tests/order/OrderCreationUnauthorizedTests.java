package site.nomoreparties.stellarburgers.tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class OrderCreationUnauthorizedTests extends Steps {

    private final Utils utils = new Utils();
    private OrderData orderData;
    private Integer orderNumber;

    @Before
    @Step("Выполить предварительные действия для тестов по созданию заказа")
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;

    }

    @Test
    @DisplayName("Создание заказа. Пользователь не авторизован. Успешно")
    @Description("Проверяет, что можно создать заказ неавторизованному пользователю." +
            "В ответе получены статус, имя и номер заказа")
    public void createOrderSuccessReturnStatus200ok() {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderUnauthorized(orderData);

        checkOrderCreatedSuccessfully(response);
        checkOrderCreatedHasName(response);
        checkOrderCreatedHasOrderNumber(response);

    }

    @Test
    @DisplayName("Создание заказа без ингридиентов. Провал")
    @Description("Проверяет, что нельзя создать заказ без ингридиентов. Получен статус 400 и сообщение об ошибке")
    public void createOrderFailNoIngredientsReturnStatus404BadRequest() {
        List<String> ingredients = new ArrayList<>();
        orderData = new OrderData(ingredients);
        ValidatableResponse response = createOrderUnauthorized(orderData);

        checkOrderCreateFail(response);
        checkOrderCreateNoIngredientsErrorMessageIsCorrect(response);

    }

    @Test
    @DisplayName("Создание заказа только с невалидными ингридиентами. Провал")
    @Description("Проверяет, что нельзя создать заказ с несуществующими ингридиентами. Получен статус 500")
    public void createOrderFailInvalidIngredientsReturnStatus500InternalServerError() {
        orderData = utils.generateFakeIngredients();

        ValidatableResponse response = createOrderUnauthorized(orderData);

        checkOrderCreateFailStatus500(response);

    }

    @Test
    @DisplayName("Создание заказа с валдиными и невалидными ингридиентов. Провал")
    @Description("Проверяет, что нельзя создать заказ, где присутсвуют несуществующие ингридиенты. Получен статус 500")
    public void createOrderFailInvalidAndValidIngredientsReturnStatus500InternalServerError() {
        orderData = utils.generateFakeAndValidIngredients(getIngredients());

        ValidatableResponse response = createOrderUnauthorized(orderData);

        checkOrderCreateFailStatus500(response);

    }

}
