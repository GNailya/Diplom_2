package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.Order;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertTrue;

import static site.nomoreparties.stellarburgers.OrderClient.orderCreate;
import static site.nomoreparties.stellarburgers.model.Order.getIngredientOrder;
import static site.nomoreparties.stellarburgers.model.Order.getInvalideIngredientOrder;


public class OrderCreateTest {
    private User user;
    private UserClient userClient;
    String accessToken;




    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
        Response responseLogin = userClient.login(UserCredentials.from(user));
        accessToken = responseLogin.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);

    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void orderCreateWithAuthorizationTest() {
        Response orderCreate = orderCreate(getIngredientOrder(), accessToken);

        int statusCode = orderCreate.getStatusCode();
        boolean isOrderCreate = orderCreate.jsonPath().getBoolean("success");

        assertThat(statusCode, equalTo(200));
        assertTrue(isOrderCreate);

    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void orderCreateOutAuthorizationTest() {
        Response orderCreate = orderCreate(getIngredientOrder(), "");

        int statusCode = orderCreate.getStatusCode();
        boolean isOrderCreate = orderCreate.jsonPath().getBoolean("success");

        assertThat(statusCode, equalTo(200));
        assertTrue(isOrderCreate);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void orderCreateOutIngredientTest() {
        Order order = new Order(null);
        Response orderCreate = orderCreate(order, "accessToken");

        int statusCode = orderCreate.getStatusCode();
        String message = orderCreate.jsonPath().getString("message");

        assertThat(statusCode, equalTo(400));
        assertThat(message, equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с невалидным хешем ингредиентов")
    public void orderCreateWithInvalidIngredientTest() {
        Response orderCreate = orderCreate(getInvalideIngredientOrder(), "accessToken");

        int statusCode = orderCreate.getStatusCode();
        assertThat(statusCode, equalTo(500));

    }
}
