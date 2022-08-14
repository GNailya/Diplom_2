package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    private final String ROOT = "api/auth/";

    @Step("Создание пользователя")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(ROOT + "register/")
                .then();

    }

    @Step("Логин пользователя")
    public ValidatableResponse login(UserCredentials creds) {
        return given()
                .spec(getSpec())
                .body(creds)
                .when()
                .post(ROOT + "login/")
                .then();

    }

    @Step("Удаление пользователя")
    public ValidatableResponse delete(int courierId) {
        return given()
                .spec(getSpec())
                .when()
                .delete(ROOT + "user/")
                .then();
    }

    public static class UserCredentials {
        public String emal;
        public String password;

        public UserCredentials(String email, String password) {
            this.emal = email;
            this.password = password;
        }

        public static UserCredentials from(User user) {
            return new UserCredentials(user.email, user.password);
        }
    }
}
