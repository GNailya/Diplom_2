package site.nomoreparties.stellarburgers;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellarburgers.model.User;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static site.nomoreparties.stellarburgers.model.User.getDefault;

@RunWith(Parameterized.class)
public class CreateWithEmptyFieldTest {
    private final String email;
    private final String password;
    private final String name;
    private UserClient userClient;
    private Response regWithNulEmail;

    public CreateWithEmptyFieldTest(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    @Parameterized.Parameters
    public static Object[][] getData() {
        return new Object[][]{
                {"", "123456", "Micha"},
                {"Mark@yandex.ru", "", "Micha"},
                {"Masha@yandex.ru", "787999", ""},
        };
    }

    @Before
    public void setUp() {
        userClient = new UserClient();

    }

    @After
    public void tearDown() {

        String accessToken = regWithNulEmail.body().jsonPath().getString("accessToken");
        if (accessToken != null) {
            userClient.delete(accessToken);
        }
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей.")
    public void createWithNulFieldTest() {
        User user = getDefault(email, password, name);
        regWithNulEmail = userClient.create(user);

        int statusCode = regWithNulEmail.getStatusCode();
        String message = regWithNulEmail.jsonPath().getString("message");

        assertThat(statusCode, equalTo(403));
        assertThat(message, equalTo("Email, password and name are required fields"));


    }

}




