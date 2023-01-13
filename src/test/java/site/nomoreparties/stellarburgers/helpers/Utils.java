package site.nomoreparties.stellarburgers.helpers;

import com.github.javafaker.Faker;
import site.nomoreparties.stellarburgers.pojo.User;

public class Utils {
    Faker faker = new Faker();
    public User generateRandomUser() {
        String email = faker.internet().emailAddress();
        String password = faker.internet()
                .password(6,12,true,true,true);
        String name = faker.name().firstName();
        return new User(email, password, name);

    }

    public String generateRandomName() {
        return faker.name().firstName();
    }

    public String generateRandomPassword() {
        return faker.internet()
                .password(6,12,true,true,true);
    }

    public String generateShortPassword() {
        return faker.internet()
                .password(1,5,true,true,true);
    }

    public String generateRandomEmail() {
        return faker.internet().emailAddress();
    }
}
