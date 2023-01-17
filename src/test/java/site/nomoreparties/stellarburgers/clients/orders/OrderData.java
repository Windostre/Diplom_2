package site.nomoreparties.stellarburgers.clients.orders;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Описание ключей заказа для целей api
 */
public class OrderData {
    @Getter
    @Setter
    private List<String> ingredients;

    public OrderData(List<String> ingredients) {
        this.ingredients = ingredients;
    }


}
