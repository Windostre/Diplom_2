package site.nomoreparties.stellarburgers.tests.order;

import clients.OrderData;
import clients.UserData;
import helpers.Checks;
import helpers.Steps;
import helpers.Utils;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class OrderGetListTests extends Steps {

    private final Utils utils = new Utils();
    private final Checks check = new Checks();
    String createdOrderId;
    private OrderData orderData;
    private UserData userData;
    private String accessToken;

    @Before
    @Step("Выполить предварительные действия для тестов по получению списка заказов")
    public void setUp() throws InterruptedException {
        userData = utils.generateRandomUser();
        orderData = utils.generateValidIngredientsList(getIngredients());

        ValidatableResponse createResponse = createUser(userData);
        accessToken = createResponse.extract().path("accessToken");
        ValidatableResponse orderResponse = createOrderAuthorized(accessToken, orderData);
        createdOrderId = orderResponse.extract().path("order._id");

    }

    @Test
    @DisplayName("Получить список заказов пользователя без авторизации. Провал")
    @Description("Проверяет, что нельзя получить список заказов конкретного пользователя без авторизации. " +
            "Получен статус 401 и сообщение об ошибке")
    public void getUserOrdersFailUnauthorizedReturnStatus401Unauthorized() throws InterruptedException {
        accessToken = "";
        ValidatableResponse response = getOrders(accessToken);

        check.getUserOrderFail(response);
        check.getUserOrderErrorMessageIsCorrect(response);

    }

    @Test
    @DisplayName("Получить список заказов пользователя. Пользователь авторизован. Успешно")
    @Description("Проверяет, что авторизованный пользователь может получить списко своих заказов. " +
            "Получен статус 200 и в ответе есть список всех заказазов пользователя с их общим количеством и отдельно за день. ")
    public void getUserOrdersSuccessAuthorizedReturnOrderListAnd200Ok() throws InterruptedException {
        ValidatableResponse response = getOrders(accessToken);

        List<String> orders = response.extract().path("orders");
        int ordersQuantity = orders.size();

        check.getUserOrderSuccess(response);
        check.getUserOrderAreNotEmpty(response);
        check.getUserOrderHasProperOrderId(response, createdOrderId);
        check.getUserOrderHasAllUserOrdersOnly(response, ordersQuantity);

    }

}
