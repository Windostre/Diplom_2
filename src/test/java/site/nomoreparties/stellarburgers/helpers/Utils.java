package site.nomoreparties.stellarburgers.helpers;

import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.clients.OrderData;
import site.nomoreparties.stellarburgers.clients.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Вспомогательльные методы, генерации данных
 */

public class Utils {
    Random random = new Random();
    Faker faker = new Faker();

    public UserData generateRandomUser() {
        String email = generateRandomEmail();
        String password = generateRandomPassword();
        String name = generateRandomName();
        return new UserData(email, password, name);

    }

    public String generateRandomName() {
        return faker.name().firstName();
    }

    public String generateRandomPassword() {
        return faker.internet()
                .password(6, 12, true, true, true);
    }

    public String generateShortPassword() {
        return faker.internet()
                .password(1, 5, true, true, true);
    }

    public String generateRandomEmail() {
        return faker.internet().emailAddress();
    }


    public String getRandomIngredient(ValidatableResponse response) {
        List<String> ingredients = response.extract().path("data._id");
        int randomIngredient = random.nextInt(ingredients.size());

        return ingredients.get(randomIngredient);
    }

    public OrderData generateValidIngredientsList(ValidatableResponse response) {
        int size = random.nextInt(10) + 1;
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ingredients.add(getRandomIngredient(response));
        }
        return new OrderData(ingredients);
    }

    public OrderData generateFakeIngredients() {
        int size = random.nextInt(10) + 1;

        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String fakeIngredient = (RandomStringUtils.randomAlphanumeric(24, 25)).toLowerCase();
            ingredients.add(fakeIngredient);
        }
        return new OrderData(ingredients);
    }

    public OrderData generateFakeAndValidIngredients(ValidatableResponse response) {
        int size = random.nextInt(3) + 1;
        List<String> ingredients = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            String fakeIngredient = ("fake" + RandomStringUtils.randomAlphanumeric(20, 21)).toLowerCase();
            String validIngredient = getRandomIngredient(response);
            ingredients.add(fakeIngredient);
            ingredients.add(validIngredient);
        }
        return new OrderData(ingredients);
    }


}
