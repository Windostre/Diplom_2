package site.nomoreparties.stellarburgers;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import utils.Utils;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class UserCreationTests {
    private String accessToken;
    private String refreshToken;
    Utils utils = new Utils();
    User user = utils.generateRandomUser();


    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/";
    }

    @After
    public void deleteUser() {
        ValidatableResponse response = given().log().all()
                .auth().oauth2(accessToken)
                .header("Content-type", "application/json")
                .when()
                .delete("/api/auth/user")
                .then();
        Assert.assertEquals(true, response.extract().path("success"));
        Assert.assertEquals("User successfully removed", response.extract().path("message"));
    }

    @Test
    public void createUserSuccessReceivedStatus200ok() {
        ValidatableResponse response = given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("api/auth/register")
                .then();
        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        Assert.assertEquals(200, response.extract().statusCode());
        Assert.assertEquals(true, response.extract().path("success"));
        assertThat(accessToken,notNullValue());
        assertThat(refreshToken,notNullValue());

    }

}
