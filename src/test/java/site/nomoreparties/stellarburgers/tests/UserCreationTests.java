package site.nomoreparties.stellarburgers.tests;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.*;
import site.nomoreparties.stellarburgers.helpers.Methods;
import site.nomoreparties.stellarburgers.helpers.Utils;
import site.nomoreparties.stellarburgers.pojo.User;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class UserCreationTests extends Methods {
    private String refreshToken;
    private String accessToken;
    private Utils utils = new Utils();
    private User basicUserData = utils.generateRandomUser();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @After
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    public void createUserSuccessReturnStatus200ok() {
        ValidatableResponse response = createUser(basicUserData);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserFailEmailIsNullReturnStatus403forbidden() {
        basicUserData.setEmail(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailEmailIsBlankReturnStatus403forbidden() {
        basicUserData.setEmail("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailPasswordIsNullReturnStatus403forbidden() {
        basicUserData.setPassword(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailPasswordIsBlankReturnStatus403forbidden() {
        basicUserData.setPassword("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailNameIsNullReturnStatus403forbidden() {
        basicUserData.setName(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailNameIsBlankReturnStatus403forbidden() {
        basicUserData.setName("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailDublicateEmailReturnStatus403forbidden() {
        createUser(basicUserData);
        User dublicateUser = new User(basicUserData.getEmail(), utils.generateRandomPassword(),utils.generateRandomName());
        ValidatableResponse response = createUser(dublicateUser);

        assertEquals(403, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));
        assertEquals("User already exists", response.extract().path("message"));
    }

    @Test
    public void createUserSuccessDublicateNameReturnStatus200ok() {
        createUser(basicUserData);
        User dublicateUser = new User(utils.generateRandomEmail(), utils.generateRandomPassword(),basicUserData.getName());
        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserSuccessDublicatePasswordReturnStatus200ok() {
        createUser(basicUserData);
        User dublicateUser = new User(utils.generateRandomEmail(), basicUserData.getPassword(),utils.generateRandomName());
        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserFailShortPassword() {
        basicUserData.setPassword(utils.generateShortPassword());
        ValidatableResponse response = createUser(basicUserData);

        assertNotEquals(200, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));

    }

    @Test
    public void createUserValidationFailEmailOmittedReturnStatus403forbidden() {
        User user = new User()
                .addPassword(basicUserData.getPassword())
                .addName(basicUserData.getName());
        ValidatableResponse response = createUserString(user.buildJSONToString());

        assertNotEquals(200, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));

    }

    @Test
    public void createUserValidationFailPasswordOmittedReturnStatus403forbidden() {
        User user = new User()
                .addEmail(basicUserData.getEmail())
                .addName(basicUserData.getName());
        ValidatableResponse response = createUserString(user.buildJSONToString());

        assertNotEquals(200, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));

    }

    @Test
    public void createUserValidationFailNameOmittedReturnStatus403forbidden() {
        User user = new User()
                .addEmail(basicUserData.getEmail())
                .addPassword(basicUserData.getPassword());
        ValidatableResponse response = createUserString(user.buildJSONToString());

        assertNotEquals(200, response.extract().statusCode());
        assertEquals(false, response.extract().path("success"));

    }

}
