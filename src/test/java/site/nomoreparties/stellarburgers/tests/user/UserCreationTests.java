package site.nomoreparties.stellarburgers.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.UserData;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class UserCreationTests extends Steps {
    private final Utils utils = new Utils();
    private String refreshToken;
    private String accessToken;
    private UserData basicUserData;

    @Before
    @Step("Выполить предварительные действия для тестов по созданию пользователя")
    public void setUp() {
        RestAssured.baseURI = BURGER_BASE_URI;
        basicUserData = utils.generateRandomUser();
    }

    @After
    @Step("Удалить тестовые данные")
    public void tearDown() {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Создание пользователя. Успешно")
    @Description("Проверяет, что при заполнении обязательных полей валидными значениями пользователь создается успешно." +
            "Получен статус 200, в теле ответа получен accessToken и refreshToken")
    public void createUserSuccessReturnStatus200ok() {
        ValidatableResponse response = createUser(basicUserData);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    @DisplayName("Создание пользователя. Почта = null. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым email." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailEmailIsNullReturnStatus403forbidden() {
        basicUserData.setEmail(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Почта не заполнена. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым email." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailEmailIsBlankReturnStatus403forbidden() {
        basicUserData.setEmail("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Пароль = null. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым паролем." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailPasswordIsNullReturnStatus403forbidden() {
        basicUserData.setPassword(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Пароль не заполнен. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым паролем." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailPasswordIsBlankReturnStatus403forbidden() {
        basicUserData.setPassword("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Имя = null. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым именем." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailNameIsNullReturnStatus403forbidden() {
        basicUserData.setName(null);
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Имя не заполнено. Провал")
    @Description("Проверяет, что нельзя создать пользователя с пустым именем." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailNameIsBlankReturnStatus403forbidden() {
        basicUserData.setName("");
        ValidatableResponse response = createUser(basicUserData);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("Email, password and name are required fields", response.extract().path("message"));

    }

    @Test
    @DisplayName("Создание пользователя. Дубликат почты. Провал")
    @Description("Проверяет, что нельзя создать пользователя с неуникальным e-mail." +
            "Получен статус 403 и сообщение об ошибке")
    public void createUserFailDublicateEmailReturnStatus403forbidden() {
        createUser(basicUserData);
        UserData dublicateUser = new UserData(basicUserData.getEmail(), utils.generateRandomPassword(), utils.generateRandomName());
        ValidatableResponse response = createUser(dublicateUser);

        assertEquals(403, response.extract().statusCode());
        assertFalse(response.extract().path("success"));
        assertEquals("User already exists", response.extract().path("message"));
    }

    @Test
    @DisplayName("Создание пользователя. Дубликат имени. Успешно")
    @Description("Проверяет, что можно создать пользователя с неуникальным именем." +
            "Получен статус 200 и сообщение получен accessToken и refreshToken")
    public void createUserSuccessDublicateNameReturnStatus200ok() {
        createUser(basicUserData);
        UserData dublicateUser = new UserData(utils.generateRandomEmail(), utils.generateRandomPassword(), basicUserData.getName());
        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    @DisplayName("Создание пользователя. Дубликат пароля. Успешно")
    @Description("Проверяет, что можно создать пользователя с неуникальным паролем." +
            "Получен статус 200 и сообщение получен accessToken и refreshToken")
    public void createUserSuccessDublicatePasswordReturnStatus200ok() {
        createUser(basicUserData);
        UserData dublicateUser = new UserData(utils.generateRandomEmail(), basicUserData.getPassword(), utils.generateRandomName());

        ValidatableResponse response = createUser(dublicateUser);

        accessToken = response.extract().path("accessToken").toString().substring(7);
        refreshToken = response.extract().path("refreshToken");

        assertEquals(200, response.extract().statusCode());
        assertTrue(response.extract().path("success"));
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());

    }

    @Test
    @DisplayName("Создание пользователя. Короткий пароль. Провал")
    @Description("Проверяет, что нельзя создать пользователя паролем с 6 символами и меньше.")
    public void createUserFailShortPassword() {
        basicUserData.setPassword(utils.generateShortPassword());

        ValidatableResponse response = createUser(basicUserData);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

    @Test
    @DisplayName("Валидация.Создание пользователя без пароля. Провал")
    @Description("Проверяет, что система не создаст пользователя если в запросе нет поля password")
    public void createUserValidationFailPasswordOmitted() {
        UserData user = new UserData()
                .addEmail(basicUserData.getEmail())
                .addName(basicUserData.getName());

        ValidatableResponse response = createUserString(user);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

    @Test
    @DisplayName("Валидация.Создание пользователя без почты. Провал")
    @Description("Проверяет, что система не создаст пользователя если в запросе нет поля email")
    public void createUserValidationFailEmailOmitted() {
        UserData user = new UserData()
                .addPassword(basicUserData.getPassword())
                .addName(basicUserData.getName());

        ValidatableResponse response = createUserString(user);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

    @Test
    @DisplayName("Валидация.Создание пользователя без имени. Провал")
    @Description("Проверяет, что система не создаст пользователя если в запросе нет поля name")
    public void createUserValidationFailNameOmitted() {
        UserData user = new UserData()
                .addEmail(basicUserData.getEmail())
                .addPassword(basicUserData.getPassword());

        ValidatableResponse response = createUserString(user);

        assertNotEquals(200, response.extract().statusCode());
        assertFalse(response.extract().path("success"));

    }

}
