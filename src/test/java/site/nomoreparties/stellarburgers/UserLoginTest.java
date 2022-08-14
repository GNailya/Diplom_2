package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.apache.http.auth.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UserLoginTest {
    private User user;
    private UserClient userClient;
    String accessToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
        userClient.create(user);
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);

    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    public void userSuccesLoginTest() {
        Response responseLogin = userClient.login(UserCredentials.from(user));

        int statusCode = responseLogin.getStatusCode();
        boolean isSuccesLogin = responseLogin.jsonPath().getBoolean("success");
        String email = responseLogin.jsonPath().getString("user.email");
        String name = responseLogin.jsonPath().getString("user.name");
        String accessToken = responseLogin.body().jsonPath().getString("accessToken");
        String refreshToken = responseLogin.body().jsonPath().getString("accessToken");

        assertThat(statusCode, equalTo(200));
        assertTrue(isSuccesLogin);
        assertEquals(user.getEmail().toLowerCase(), email);
        assertEquals(user.getName(), name);
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());
    }

    @Test
    @DisplayName("логин с неверным логином и паролем")
    public void loginWithInvalidFieldTest() {
        Response responseLogin = userClient.login(UserCredentials.getRandomUserCredentials());

        int statusCode = responseLogin.getStatusCode();
        String message = responseLogin.jsonPath().getString("message");

        assertThat(statusCode, equalTo(401));
        assertThat(message, equalTo("email or password are incorrect"));
    }
}
