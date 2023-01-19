package site.nomoreparties.stellarburgers.tests.order;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import java.util.List;

import static io.restassured.RestAssured.given;
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
    public void getUserOrdersFailUnauthorizedReturnStatus401Unauthorized() {
        accessToken = "";
        ValidatableResponse response = getOrders(accessToken);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("You should be authorised", response.extract().path("message"));

    }

    @Test
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
