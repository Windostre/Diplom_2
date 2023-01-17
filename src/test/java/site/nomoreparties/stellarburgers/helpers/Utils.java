package site.nomoreparties.stellarburgers.helpers;

import com.github.javafaker.Faker;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import site.nomoreparties.stellarburgers.clients.orders.OrderData;
import site.nomoreparties.stellarburgers.clients.user.UserData;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class Utils {
    Random random = new Random();
    Faker faker = new Faker();

    public UserData generateRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet()
                .password(6, 12, true, true, true);
        String name = faker.name().firstName();
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
        int randomIgredient = random.nextInt(ingredients.size());

        return ingredients.get(randomIgredient);
    }

    public OrderData createIngredientListWithApi(ValidatableResponse response) {
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
            String fakeIngredient = RandomStringUtils.randomAlphanumeric(24, 25);
            ingredients.add(fakeIngredient.toLowerCase(Locale.ROOT));
        }
        return new OrderData(ingredients);


    }


}
