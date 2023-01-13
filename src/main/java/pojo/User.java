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
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String email;
    private String password;
    private String name;

}
