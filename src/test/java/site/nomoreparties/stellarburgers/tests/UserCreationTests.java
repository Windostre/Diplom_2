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
    User userData = utils.generateRandomUser();


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
//        Assert.assertEquals(true, response.extract().path("success"));
//        Assert.assertEquals("User successfully removed", response.extract().path("message"));
    }

    @Test
    @Ignore
    public void createUserSuccessReturnStatus200ok() {
        ValidatableResponse response = createUser(userData);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        Assert.assertEquals(200, response.extract().statusCode());
        Assert.assertEquals(true, response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    public void createUserFailEmailIsNullReturnStatus403forbidden() {
        userData.setEmail(null);
        ValidatableResponse response = createUser(userData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    public void createUserFailEmailIsBlankReturnStatus403forbidden() {
        userData.setEmail("");
        ValidatableResponse response = createUser(userData);

        Assert.assertEquals(403, response.extract().statusCode());
        Assert.assertEquals(false, response.extract().path("success"));
        Assert.assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

}
