package site.nomoreparties.stellarburgers.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание ключей пользователя для целей api
 */
public class User {
    @Getter
    @Setter
    private String email;
    @Getter
    @Setter
    private String password;
    @Getter
    @Setter
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public User() {

    }

    private List<String> builder = new ArrayList<>(); // для создания шаблона объекта

    public User addEmail(String email) {
        builder.add("\"email\": " + "\"" + email + "\"");
        return this;
    }
    public User addPassword(String password) {
        builder.add("\"password\": " + "\"" + password + "\"");
        return this;
    }
    public User addName(String name) {
        builder.add("\"name\": " + "\"" + name + "\"");
        return this;
    }

    public String buildJSONToString() {
        return "{" + "\n" + String.join( ",", builder) + "\n"+ "}";
    }
}
