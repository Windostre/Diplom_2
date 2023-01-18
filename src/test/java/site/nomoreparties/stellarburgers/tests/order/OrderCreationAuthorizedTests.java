package site.nomoreparties.stellarburgers.tests.order;

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
    private Integer orderNumber;
    private UserData userData;
    private UserData loginData;
    private String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        userData = utils.generateRandomUser();
        loginData = new UserData()
                .addEmail(userData.getEmail())
                .addPassword(userData.getPassword());

        ValidatableResponse createResponse = createUser(userData);
        accessToken = createResponse.extract().path("accessToken");

        ValidatableResponse loginResponse = login(accessToken, loginData);
        accessToken = loginResponse.extract().path("accessToken").toString().substring(7);

    }

    @After
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    public void createOrderSuccessReturnStatus200ok() {
        orderData = utils.generateValidIngredientsList(getIngredients());
        ValidatableResponse response = createOrderAuthorized(accessToken, orderData);

        orderNumber = response.extract().path("order.number");

        assertEquals(200, response.extract().statusCode());
        assertThat(response.extract().path("name"), notNullValue());
        assertTrue(response.extract().path("success"));
        assertTrue(orderNumber > 0);
    }
}
