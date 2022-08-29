package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {
    private final String ORDERS = "orders";

    @Step("Создание заказа")
    public Response orderCreate(Order order, String accessToken) {

        return (Response) given()
                .spec(getSpec())
                .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(ORDERS).then().log().all().extract();
    }
    @Step("Получение списка заказов")
    public Response getOrderList(String accessToken) {

        return (Response) given()
                .spec(getSpec())
                .header("authorization", accessToken)
                .when()
                .get(ORDERS).then().log().all().extract();
    }
}
