package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static site.nomoreparties.stellarburgers.model.Order.getIngredientOrder;
import static site.nomoreparties.stellarburgers.model.User.getRandom;

public class GetOrderListTest {
    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;


    @Before
    public void setUp() {
        User user = getRandom();
        userClient = new UserClient();
        userClient.create(user);
        Response responseLogin = userClient.login(UserCredentials.from(user));
        accessToken = responseLogin.body().jsonPath().getString("accessToken");
        orderClient = new OrderClient();
        orderClient.orderCreate(getIngredientOrder(), accessToken);


    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);

    }

    @Test
    @DisplayName("Получение списка заказов")
    public void getOrderListWithAuthorizationTest() {
        Response response = orderClient.getOrderList(accessToken);

        List<Object> orders = response.jsonPath().getList("orders");
        boolean isGetList = response.jsonPath().getBoolean("success");

        assertFalse(orders.isEmpty());
        assertTrue(isGetList);

    }

    @Test
    @DisplayName("Получение списка заказов неавторизованным пользователем")
    public void getOrderListTest() {
        Response response = orderClient.getOrderList("");

        int statusCode = response.getStatusCode();
        String message = response.jsonPath().getString("message");

        assertThat(statusCode, equalTo(401));
        assertThat(message, equalTo("You should be authorised"));

    }
}
