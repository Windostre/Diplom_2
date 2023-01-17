package site.nomoreparties.stellarburgers.tests;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.orders.OrderData;
import site.nomoreparties.stellarburgers.clients.orders.OrderSteps;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class OrderCreationUnauthorizedTests extends OrderSteps {
    private List<String> ingredients = new ArrayList<>();
    private OrderData orderData;
    private Integer orderNumber;
    @Before
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        ValidatableResponse ingredientsList = getIngredientsList();
        ingredients = ingredientsList.extract().path("data._id");
        orderData = new OrderData(ingredients);
    }

    @Test
    public void createOrderSuccessReturnStatus200ok() {
        ValidatableResponse response = createOrder(orderData);

        orderNumber = response.extract().path("order.number");

        assertEquals(200, response.extract().statusCode());
        assertThat(response.extract().path("name"), notNullValue());
        assertTrue(response.extract().path("success"));
        assertTrue(orderNumber > 0);

    }

    @Test
    public void createOrderFailNoIngredientsReturnStatus404BadRequest() {
        ingredients.clear();
        ValidatableResponse response = createOrder(orderData);

        assertEquals(400, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Ingredient ids must be provided", response.extract().path("message"));

    }
    @Test
    public void createOrderFailInvalidIngredientsReturnStatus500InternalServerError() {
        ingredients.add("61c0c5a71d1f82001dfdfdaa79");
        ValidatableResponse response = createOrder(orderData);

        assertEquals(500, response.extract().statusCode());

    }

}
