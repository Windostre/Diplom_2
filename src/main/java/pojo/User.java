package pojo;

import lombok.*;

/**
 * Описание ключей пользователя для целей api
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class User {

    private String email;
    private String password;
    private String name;

}
