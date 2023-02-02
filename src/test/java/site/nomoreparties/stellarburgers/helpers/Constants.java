package site.nomoreparties.stellarburgers.helpers;

public class Constants {
    protected final String BURGER_BASE_URI = "https://stellarburgers.nomoreparties.site";
    protected final String USER_REGISTER = "/api/auth/register"; // для создания пользователя
    protected final String USER_DATA = "/api/auth/user"; // для получения, обновления, удаления данных пользователя
    protected final String USER_LOGIN = "/api/auth/login"; // авторизация
    protected final String USER_LOGOUT = "/api/auth/logout"; // выход из системы
    protected final String INGREDIENTS = "/api/ingredients"; // список ингридиентов
    protected final String ORDERS = "/api/orders"; // для создания и получения заказа пользователем
    protected final String CONSTANT_USER_EMAIL = "test@testmail.com";
    protected final String CONSTANT_USER_PASSWORD = "test_password";
    protected final String CONSTANT_USER_NAME = "TestName";
}
