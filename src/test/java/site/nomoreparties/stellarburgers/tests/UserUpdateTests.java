package site.nomoreparties.stellarburgers.tests;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.user.UserSteps;
import site.nomoreparties.stellarburgers.helpers.Utils;
import site.nomoreparties.stellarburgers.clients.user.UserData;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class UserUpdateTests extends UserSteps {
    private String refreshToken;
    private String accessToken;
    private Utils utils = new Utils();
    private UserData basicUserData = utils.generateRandomUser();

    private String updatedEmail;
    private String updatedName;
    @Before
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        ValidatableResponse response = createUser(basicUserData);
        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");
        updatedEmail = "upd" + basicUserData.getEmail();
        updatedName = "upd" + basicUserData.getName();

    }

    @After
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    public void updateUnauthorizedUserFailReceiveStatus401Unauthorized() {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);
        ValidatableResponse response = updateUserData(updatedUserData,"");

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("You should be authorised", response.extract().path("message"));
    }

    @Test
    public void updateUnauthorizedUserFailLogoutReceiveStatus401Unauthorized() {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);

        logout(refreshToken);
        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        assertEquals(401, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("You should be authorised", response.extract().path("message"));
    }

    @Test
    public void updateAuthorizedUserSuccessReceiveNewDataAndStatus200Ok() {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
        assertEquals(updatedEmail, response.extract().path("user.email"));
        assertEquals(updatedName, response.extract().path("user.name"));

    }

    @Test
    public void updateAuthorizedUserFailEmailIsBlank() {
        UserData updatedUserData = new UserData()
                .addEmail("")
                .addName(updatedName);

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

    @Test
    public void updateAuthorizedUserFailNameIsBlank() {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName("");

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

    @Test
    public void updateAuthorizedUserFailEmailAlreadyExistReceiveStatus403Forbiden() {
        UserData updatedUserData = new UserData()
                .addEmail(CONSTANT_USER_EMAIL)
                .addName(updatedName);

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("User with such email already exists", response.extract().path("message"));

    }

}
