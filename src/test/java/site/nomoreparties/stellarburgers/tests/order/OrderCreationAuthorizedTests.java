package site.nomoreparties.stellarburgers.tests.order;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class OrderCreationAuthorizedTests extends Steps {
    private final Utils utils = new Utils();
    private OrderData orderData;
    private UserData userData;
    private String accessToken;


    @Before
    @Step("Выполить предварительные действия для тестов по созданию заказа")
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        userData = utils.generateRandomUser();

        ValidatableResponse createResponse = createUser(userData);
        accessToken = createResponse.extract().path("accessToken").toString().substring(7);

    }

    @After
    @Step("Удалить тестовые данные")
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание заказа. Пользователь авторизован. Успешно")
    @Description("Проверяет, что можно создать заказ авторизованному пользователю с указанием валидных ингридиентов. " +
            "В ответе возвращен заказ со списком ингридиентов, данные пользователя, дата создания и изменения, id, наименование, номер и цена заказа")
    public void createOrderSuccessReturnStatus200ok() {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderAuthorized(accessToken, orderData);


        boolean success = response.extract().path("success");
        String name = response.extract().path("name");
        String orderId = response.extract().path("order._id");
        String ownerName = response.extract().path("order.owner.name");
        String ownerEmail = response.extract().path("order.owner.email");
        String createdAt = response.extract().path("order.owner.createdAt");
        String updatedAt = response.extract().path("order.owner.updatedAt");
        String orderStatus = response.extract().path("order.status");
        int orderNumber = response.extract().path("order.number");
        int orderPrice = response.extract().path("order.number");

        assertEquals(200, response.extract().statusCode());
        assertTrue(success);
        assertThat(name, notNullValue());
        assertThat(orderId, notNullValue());
        assertThat(response.extract().path("order.ingredients"), notNullValue());
        assertEquals(userData.getName(), ownerName);
        assertEquals(userData.getEmail(), ownerEmail);
        assertThat(createdAt, notNullValue());
        assertThat(updatedAt, notNullValue());
        assertEquals("done", orderStatus);
        assertTrue(orderNumber > 0);
        assertTrue(orderPrice > 0);
    }
}
