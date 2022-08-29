package site.nomoreparties.stellarburgers.model;

import java.util.List;

public class Order {

    public List<String> ingredients;

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }


    public static Order getIngredientOrder() {
        List<String> list = List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa70");
        return new Order(list);
    }

    public static Order getInvalideIngredientOrder() {
        List<String> list = List.of("6000000000000000", "616765444576778hujm");
        return new Order(list);
    }

    @Override
    public String toString() {
        return "Order{" +
                "ingredients=" + ingredients +
                '}';
    }

}



