package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {
    private final String LOGIN = "auth/login/";
    private final String DELETE_OR_UPDATE = "auth/user/";
    private final String REGISTER = "auth/register/";

    @Step("Создание пользователя")
    public Response create(User user) {
        return (Response) given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(REGISTER)
                .then().log().all().extract();

    }

    @Step("Логин пользователя")
    public Response login(UserCredentials creds) {
        return (Response) given()
                .spec(getSpec())
                .body(creds)
                .when()
                .post(LOGIN)
                .then().log().all().extract();

    }

    @Step("Удаление пользователя")
    public void delete(String accessToken) {
        if (accessToken == null) {
            return;
        }
        given()
                .spec(getSpec())
                .header("authorization", accessToken)
                .when()
                .delete(DELETE_OR_UPDATE)
                .then()
                .assertThat().log().all()
                .statusCode(202)
                .extract()
                .path("ok");
    }

    @Step("Обновление данных пользователя")
    public Response updateUser(User user, String accessToken) {

        return (Response) given()
                .spec(getSpec())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(DELETE_OR_UPDATE).then().log().all().extract();

    }

}
