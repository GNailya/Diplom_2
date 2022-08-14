package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient {

    @Step("Создание заказа")
    public static Response orderCreate(Order order, String accessToken) {

        return (Response) given()
                .spec(getSpec())
                .headers("Authorization", accessToken)
                .body(order)
                .when()
                .post(BASE_URL + "/api/orders").then().log().all().extract();
    }
    @Step("Получение списка заказов")
    public static Response getOrderList(String accessToken) {

        return (Response) given()
                .spec(getSpec())
                .header("authorization", accessToken)
                .when()
                .get(BASE_URL + "/api/orders").then().log().all().extract();
    }
}
