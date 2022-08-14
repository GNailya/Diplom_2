package site.nomoreparties.stellarburgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class UserCredentials {
    public String email;
    public String password;

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public static UserCredentials from(User user) {
        return new UserCredentials(user.email, user.password);
    }

    public static UserCredentials getRandomUserCredentials() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomNumeric(6);
        return new UserCredentials(email, password);
    }

    @Override
    public String toString() {
        return "UserCredentials{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

