package site.nomoreparties.stellarburgers.clients;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание ключей пользователя для целей api
 */
public class UserData {
    private final List<String> builder = new ArrayList<>(); // для создания шаблона объекта
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String name;

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public UserData() {

    }

    public UserData addEmail(String email) {
        builder.add("\"email\": " + "\"" + email + "\"");
        return this;
    }

    public UserData addPassword(String password) {
        builder.add("\"password\": " + "\"" + password + "\"");
        return this;
    }

    public UserData addName(String name) {
        builder.add("\"name\": " + "\"" + name + "\"");
        return this;
    }

    public String buildJSONToString() {
        return "{" + "\n" + String.join(",", builder) + "\n" + "}";
    }
}
