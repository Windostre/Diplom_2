package site.nomoreparties.stellarburgers.tests.order;

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
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;

    }

    @Test
    public void createOrderSuccessReturnStatus200ok() {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderUnauthorized(orderData);

        orderNumber = response.extract().path("order.number");

        assertEquals(200, response.extract().statusCode());
        assertThat(response.extract().path("name"), notNullValue());
        assertTrue(response.extract().path("success"));
        assertTrue(orderNumber > 0);

    }

    @Test
    public void createOrderFailNoIngredientsReturnStatus404BadRequest() {
        List<String> ingredients = new ArrayList<>();
        orderData = new OrderData(ingredients);
        ValidatableResponse response = createOrderUnauthorized(orderData);

        assertEquals(400, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Ingredient ids must be provided", response.extract().path("message"));

    }

    @Test
    public void createOrderFailInvalidIngredientsReturnStatus500InternalServerError() {
        orderData = utils.generateFakeIngredients();

        ValidatableResponse response = createOrderUnauthorized(orderData);

        assertEquals(500, response.extract().statusCode());

    }

    @Test
    public void createOrderFailInvalidAndValidIngredientsReturnStatus500InternalServerError() {
        orderData = utils.generateFakeAndValidIngredients(getIngredients());

        ValidatableResponse response = createOrderUnauthorized(orderData);

        assertEquals(500, response.extract().statusCode());

    }

}
