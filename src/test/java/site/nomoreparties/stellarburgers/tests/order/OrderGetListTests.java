package site.nomoreparties.stellarburgers.tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class OrderGetListTests extends Steps {
    private final Utils utils = new Utils();
    String createdOrderId;
    String receivedOrderId;
    boolean success;
    private OrderData orderData;
    private int orderTotal;
    private int orderTotalToDay;
    private UserData userData;
    private String accessToken;

    @Before
    @Step("Выполить предварительные действия для тестов по получению списка заказов")
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        userData = utils.generateRandomUser();
        orderData = utils.generateValidIngredientsList(getIngredients());

        ValidatableResponse createResponse = createUser(userData);
        accessToken = createResponse.extract().path("accessToken").toString().substring(7);
        ValidatableResponse orderResponse = createOrderAuthorized(accessToken, orderData);
        createdOrderId = orderResponse.extract().path("order._id");

    }

    @Test
    @DisplayName("Получить список заказов пользователя без авторизации. Провал")
    @Description("Проверяет, что нельзя получить список заказов конкретного пользователя без авторизации. " +
            "Получен статус 401 и сообщение об ошибке")
    public void getUserOrdersFailUnauthorizedReturnStatus401Unauthorized() {
        accessToken = "";
        ValidatableResponse response = getOrders(accessToken);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("You should be authorised", response.extract().path("message"));

    }

    @Test
    @DisplayName("Получить список заказов пользователя. Пользователь авторизован. Успешно")
    @Description("Проверяет, что авторизованный пользователь может получить списко своих заказов. " +
            "Получен статус 200 и в ответе есть список всех заказазов пользователя с их общим количеством и отдельно за день. ")
    public void getUserOrdersSuccessAuthorizedReturnOrderListAnd200Ok() {
        ValidatableResponse response = getOrders(accessToken);

        success = response.extract().path("success");
        receivedOrderId = response.extract().path("orders._id[0]");
        List<String> orders = response.extract().path("orders");
        int ordersQuantity = orders.size();
        orderTotal = response.extract().path("total");
        orderTotalToDay = response.extract().path("totalToday");

        assertEquals(200, response.extract().statusCode());
        assertTrue(success);
        assertThat(orders, notNullValue());
        assertEquals(createdOrderId, receivedOrderId);
        assertTrue(orderTotal >= orderTotalToDay);
        System.out.println(ordersQuantity);
        assertEquals(ordersQuantity, orderTotal);
    }

}
