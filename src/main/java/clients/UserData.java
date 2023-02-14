package clients;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Описание ключей пользователя для целей api
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserData {

    @Getter(AccessLevel.NONE)
    private final List<String> builder = new ArrayList<>(); // для создания шаблона объекта
    private String email;
    private String password;
    private String name;

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
