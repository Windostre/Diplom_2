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

public class UserCreationTests extends Methods {
    private String refreshToken;
    private String accessToken;
    Utils utils = new Utils();
    User basicUserData = utils.generateRandomUser();


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

        Assert.assertEquals(200, response.extract().statusCode());
        Assert.assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserFailEmailIsNullReturnStatus403forbidden() {
        basicUserData.setEmail(null);
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailEmailIsBlankReturnStatus403forbidden() {
        basicUserData.setEmail("");
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailPasswordIsNullReturnStatus403forbidden() {
        basicUserData.setPassword(null);
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailPasswordIsBlankReturnStatus403forbidden() {
        basicUserData.setPassword("");
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailNameIsNullReturnStatus403forbidden() {
        basicUserData.setName(null);
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailNameIsBlankReturnStatus403forbidden() {
        basicUserData.setName("");
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailDublicateEmailReturnStatus403forbidden() {
        createUser(basicUserData);
        User dublicateUser = new User().builder()
                .email(basicUserData.getEmail())
                .password(utils.generateRandomPassword())
                .name(utils.generateRandomName())
                .build();
        ValidatableResponse response = createUser(dublicateUser);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("User already exists", response.extract().path("message"));

    }

    @Test
    public void createUserSuccessDublicateNameReturnStatus200ok() {
        createUser(basicUserData);
        User dublicateUser = new User().builder()
                .email(utils.generateRandomEmail())
                .password(utils.generateRandomPassword())
                .name(basicUserData.getName())
                .build();
        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        Assert.assertEquals(200, response.extract().statusCode());
        Assert.assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserSuccessDublicatePasswordReturnStatus200ok() {
        createUser(basicUserData);
        User dublicateUser = new User().builder()
                .email(utils.generateRandomEmail())
                .password(basicUserData.getPassword())
                .name(utils.generateRandomName())
                .build();
        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        Assert.assertEquals(200, response.extract().statusCode());
        Assert.assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserFailShortPassword() {
        basicUserData.setPassword(utils.generateShortPassword());
        ValidatableResponse response = createUser(basicUserData);

        Assert.assertNotEquals(200, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
       // Assert.assertEquals("User already exists", response.extract().path("message"));

    }

}
