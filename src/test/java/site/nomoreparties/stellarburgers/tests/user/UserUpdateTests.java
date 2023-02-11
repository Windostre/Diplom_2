package site.nomoreparties.stellarburgers.tests.user;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.clients.UserData;
import site.nomoreparties.stellarburgers.helpers.Checks;
import site.nomoreparties.stellarburgers.helpers.Steps;
import site.nomoreparties.stellarburgers.helpers.Utils;

public class UserUpdateTests extends Steps {
    private final Utils utils = new Utils();
    private final Checks check = new Checks();
    private String refreshToken;
    private String accessToken;
    private UserData basicUserData;
    private String updatedEmail;
    private String updatedName;

    @Before
    @Step("Выполить предварительные действия для тестов по изменению данных пользователя")
    public void setUp() throws InterruptedException {
        basicUserData = utils.generateRandomUser();
        ValidatableResponse response = createUser(basicUserData);
        accessToken = response.extract().path("accessToken");
        refreshToken = response.extract().path("refreshToken");
        updatedEmail = "upd" + basicUserData.getEmail();
        updatedName = "upd" + basicUserData.getName();

    }

    @After
    @Step("Удалить тестовые данные")
    public void tearDown() throws InterruptedException {
        if (accessToken == null || "".equals(accessToken)) {
            return;
        }
        deleteUser(accessToken);
    }

    @Test
    @DisplayName("Изменить данные пользователя без авторизации. Провал")
    @Description("Проверяет, что нельзя обновить данные пользователя без авторизации." +
            "Получен статус 401 и сообщение об ошибке")
    public void updateUnauthorizedUserFailReceiveStatus401Unauthorized() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);
        ValidatableResponse response = updateUserData(updatedUserData, "");

        check.userUpdateUnauthorisedFail(response);
        check.userUpdateUnauthorizedErrorMessageIsCorrect(response);
    }

    @Test
    @DisplayName("Изменить данные пользователя. Пользователь вышел из системы. Провал")
    @Description("Проверяет, что нельзя обновить данные пользователя, если пользователь вышел из системы." +
            "Получен статус 401 и сообщение об ошибке")
    public void updateUnauthorizedUserFailLogoutReceiveStatus401Unauthorized() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);

        logout(refreshToken);
        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        check.userUpdateUnauthorisedFail(response);
        check.userUpdateUnauthorizedErrorMessageIsCorrect(response);
    }

    @Test
    @DisplayName("Изменить данные пользователя. Пользователь авторизован. Успешно")
    @Description("Проверяет, что можно успшно обновить данные пользователя после авторизации." +
            "Получен статус 200 и в теле ответа есть данные")
    public void updateAuthorizedUserSuccessReceiveNewDataAndStatus200Ok() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail(updatedEmail)
                .addName(updatedName);

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        check.userUpdateSuccessfully(response);
        check.userUpdateNewEmailIsSet(response, updatedEmail);
        check.userUpdateNewNameIsSet(response, updatedName);

    }

    @Test
    @DisplayName("Изменить данные пользователя. Пустой e-mail. Провал")
    @Description("Проверяет, что нельзя обновить данные пользователя на пустые значения")
    public void updateAuthorizedUserFailEmailIsBlank() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail("")
                .addName(basicUserData.getName());

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        check.userUpdateFailStatusIsNot200(response);

    }

    @Test
    @DisplayName("Изменить данные пользователя. Пустое имя. Провал")
    @Description("Проверяет, что нельзя обновить данные пользователя на пустые значения")
    public void updateAuthorizedUserFailNameIsBlank() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail(basicUserData.getEmail())
                .addName("");

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        check.userUpdateFailStatusIsNot200(response);

    }

    @Test
    @DisplayName("Изменить данные пользователя. E-mail уже использован. Провал")
    @Description("Проверяет, что нельзя обновить пользователя email на неуникальный." +
            "Получен статус 403 и сообщение об ошибке")
    public void updateAuthorizedUserFailEmailAlreadyExistReceiveStatus403Forbidden() throws InterruptedException {
        UserData updatedUserData = new UserData()
                .addEmail(CONSTANT_USER_EMAIL)
                .addName(basicUserData.getName());

        ValidatableResponse response = updateUserData(updatedUserData, accessToken);

        check.userUpdateDublicateEmailFail(response);
        check.userUpdateDublicateErrorMessageIsCorrect(response);

    }

}
