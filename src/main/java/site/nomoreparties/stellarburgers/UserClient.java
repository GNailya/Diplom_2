package site.nomoreparties.stellarburgers;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.model.User;
import site.nomoreparties.stellarburgers.model.UserCredentials;

import static io.restassured.RestAssured.given;

public class UserClient extends RestAssuredClient {


    @Step("Создание пользователя")
    public Response create(User user) {
        return (Response) given()
                .spec(getSpec())
                .body(user)
                .when()
                .post(BASE_URL + "/api/auth/register/")
                .then().log().all().extract();

    }

    @Step("Логин пользователя")
    public Response login(UserCredentials creds) {
        return (Response) given()
                .spec(getSpec())
                .body(creds)
                .when()
                .post(BASE_URL + "/api/auth/login/")
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
                .delete(BASE_URL + "/api/auth/user/")
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
                .patch(BASE_URL + "/api/auth/user").then().log().all().extract();

    }

}
