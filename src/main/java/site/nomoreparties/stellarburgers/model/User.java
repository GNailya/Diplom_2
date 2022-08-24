package site.nomoreparties.stellarburgers.model;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }


    public static User getRandom() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@yandex.ru";
        String password = RandomStringUtils.randomNumeric(6);
        String name = RandomStringUtils.randomAlphabetic(6);

        return new User(email, password, name);
    }

    public static User getDefault(String email, String password, String name) {
        return new User(email, password, name);
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + name + '\'' +
                '}';
    }
}
