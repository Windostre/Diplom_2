package clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Описание ключей заказа для целей api
 */
@AllArgsConstructor
@Getter
@Setter
public class OrderData {
    private List<String> ingredients;


}
