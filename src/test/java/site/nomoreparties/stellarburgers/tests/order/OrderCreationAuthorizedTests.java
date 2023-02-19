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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OrderCreationAuthorizedTests extends Steps {

    private final Utils utils = new Utils();
    private final Checks check = new Checks();
    private OrderData orderData;
    private UserData userData;
    private String accessToken;

    @Before
    @Step("Выполить предварительные действия для тестов по созданию заказа")
    public void setUp() throws InterruptedException {
        userData = utils.generateRandomUser();

        ValidatableResponse createResponse = createUser(userData);
        accessToken = createResponse.extract().path("accessToken");

    }

    @After
    @Step("Удалить тестовые данные")
    public void tearDown() throws InterruptedException {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа. Пользователь авторизован. Успешно")
    @Description("Проверяет, что можно создать заказ авторизованному пользователю с указанием валидных ингридиентов. " +
            "В ответе возвращен заказ со списком ингридиентов, данные пользователя, дата создания и изменения, id, наименование, номер и цена заказа")
    public void createOrderSuccessReturnStatus200ok() throws InterruptedException {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderAuthorized(accessToken, orderData);

        String ownerName = response.extract().path("order.owner.name");
        String ownerEmail = response.extract().path("order.owner.email");

        check.orderCreatedSuccessfully(response);
        check.orderCreatedHasName(response);
        check.orderCreatedHasOrderId(response);
        check.orderCreatedHasIngredients(response);
        check.orderCreatedHasUserData(response, ownerName, ownerEmail);
        check.orderCreatedHasDateCreatedAt(response);
        check.orderCreatedHasDateUpdatedAt(response);
        check.orderCreatedHasStatusDone(response);
        check.orderCreatedHasOrderNumber(response);
        check.orderCreatedHasPrice(response);

    }
}
