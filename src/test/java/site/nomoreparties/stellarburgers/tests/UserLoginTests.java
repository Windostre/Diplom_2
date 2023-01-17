package site.nomoreparties.stellarburgers.tests;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.user.UserData;
import site.nomoreparties.stellarburgers.clients.user.UserSteps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import static org.junit.Assert.*;

public class UserLoginTests extends UserSteps {
    private final Utils utils = new Utils();
    private String accessToken;
    private UserData basicUserData;

    @Before
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        basicUserData = utils.generateRandomUser();
        ValidatableResponse response = createUser(basicUserData);
        accessToken = response.extract().path("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    public void loginUserSuccessReturnStatus200ok() {
        UserData loginData = new UserData()
                .addEmail(basicUserData.getEmail())
                .addPassword(basicUserData.getPassword());

        ValidatableResponse response = login(accessToken, loginData);

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
    }

    @Test
    public void loginUserFailsWrongEmailReturnStatus401Unauthorized() {
        UserData loginData = new UserData()
                .addEmail(utils.generateRandomEmail())
                .addPassword(basicUserData.getPassword());

        ValidatableResponse response = login(accessToken, loginData);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

    @Test
    public void loginUserFailsWrongPasswordReturnStatus401Unauthorized() {
        UserData loginData = new UserData()
                .addEmail(basicUserData.getEmail())
                .addPassword(utils.generateRandomPassword());

        ValidatableResponse response = login(accessToken, loginData);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

    @Test
    public void loginUserFailsEmptyEmailReturnStatus401Unauthorized() {
        UserData loginData = new UserData()
                .addEmail("")
                .addPassword(basicUserData.getPassword());

        ValidatableResponse response = login(accessToken, loginData);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

    @Test
    public void loginUserFailsEmptyPasswordReturnStatus401Unauthorized() {
        UserData loginData = new UserData()
                .addEmail(basicUserData.getEmail())
                .addPassword("");

        ValidatableResponse response = login(accessToken, loginData);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("email or password are incorrect", response.extract().path("message"));
    }

}
