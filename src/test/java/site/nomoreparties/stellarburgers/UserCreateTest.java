package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.User;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class UserCreateTest {
    private UserClient userClient;
    private User user;
    String accessToken;

    @Before
    public void setUp() {
        user = User.getRandom();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
            userClient.delete(accessToken);

    }

    @Test
    @DisplayName("Cоздание уникального пользователя")
    public void userSuccesCreateTest() {
        Response responseReg = userClient.create(user);

        int statusCode = responseReg.getStatusCode();
        boolean isSuccesCreated = responseReg.jsonPath().getBoolean("success");
        String email = responseReg.jsonPath().getString("user.email");
        String name = responseReg.jsonPath().getString("user.name");
        accessToken = responseReg.body().jsonPath().getString("accessToken");
        String refreshToken = responseReg.body().jsonPath().getString("accessToken");

        assertThat(statusCode, equalTo(200));
        assertTrue(isSuccesCreated);
        assertEquals(user.getEmail().toLowerCase(), email);
        assertEquals(user.getName(), name);
        assertThat(accessToken, notNullValue());
        assertThat(refreshToken, notNullValue());


    }

    @Test
    @DisplayName("создать пользователя, который уже зарегистрирован")
    public void createAnExistingUserTest() {
        userClient.create(user);
        Response doubleReg = userClient.create(user);

        int statusCode = doubleReg.getStatusCode();
        String message = doubleReg.jsonPath().getString("message");

        assertThat(statusCode, equalTo(403));
        assertThat(message, equalTo("User already exists"));
    }

}

