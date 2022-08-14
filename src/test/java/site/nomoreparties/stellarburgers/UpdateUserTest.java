package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static site.nomoreparties.stellarburgers.model.User.getRandom;


public class UpdateUserTest {
    private UserClient userClient;
    private User user;
    String accessToken;


    @Before
    public void setUp() {
        user = getRandom();
        userClient = new UserClient();
        userClient.create(user);
        Response responseLogin = userClient.login(UserCredentials.from(user));
        accessToken = responseLogin.body().jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        userClient.delete(accessToken);
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    public void userUpdatTest() {
        User updatUser = getRandom();
        Response responseUpdate = userClient.updateUser(updatUser, accessToken);

        int statusCode = responseUpdate.getStatusCode();
        boolean isResponseUpdate = responseUpdate.jsonPath().getBoolean("success");
        String email = responseUpdate.jsonPath().getString("user.email");
        String name = responseUpdate.jsonPath().getString("user.name");

        assertThat(statusCode, equalTo(200));
        assertTrue(isResponseUpdate);
        assertEquals(updatUser.getEmail().toLowerCase(), email);
        assertEquals(updatUser.getName(), name);

    }

    @Test
    @DisplayName("Обновление данных пользователя без авторизации")
    public void userUpdatOutLoginTest() {
        Response responseUpdate = userClient.updateUser(getRandom(), "");

        int statusCode = responseUpdate.getStatusCode();
        String message = responseUpdate.jsonPath().getString("message");

        assertThat(statusCode, equalTo(401));
        assertEquals("You should be authorised", message);

    }

    @Test
    @DisplayName("Изменение одного поля, email ")
    public void userUpdatOneFieldTest() {
        User updatEmailUser = new User(getRandom().getEmail(), user.getPassword(), user.getName());
        Response responseUpdate = userClient.updateUser(updatEmailUser, accessToken);

        int statusCode = responseUpdate.getStatusCode();
        boolean isResponseUpdate = responseUpdate.jsonPath().getBoolean("success");
        String email = responseUpdate.jsonPath().getString("user.email");
        String name = responseUpdate.jsonPath().getString("user.name");

        assertThat(statusCode, equalTo(200));
        assertTrue(isResponseUpdate);
        assertEquals(updatEmailUser.getEmail().toLowerCase(), email);
        assertEquals(user.getName(), name);

    }

    @Test
    @DisplayName("Если передать почту, которая уже используется, вернётся код ответа 403 Forbidden.")
    public void userUpdatSetExistsEmailTest() {
        User newUser = getRandom();
        userClient.create(newUser);

        Response responseLoginNewUser = userClient.login(UserCredentials.from(newUser));
        String emailNewUser = responseLoginNewUser.body().jsonPath().getString("user.email");

        User updatExistsEmailUser = new User(emailNewUser, user.getPassword(), user.getName());
        Response responseUpdateReg = userClient.updateUser(updatExistsEmailUser, accessToken);

        int statusCode = responseUpdateReg.getStatusCode();
        String message = responseUpdateReg.jsonPath().getString("message");

        assertThat(statusCode, equalTo(403));
        assertEquals("User with such email already exists", message);

    }
}
