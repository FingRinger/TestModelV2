package test;

import data.DataGenerator;
import data.Info;
import data.RequestToAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.getRandomLogin;
import static data.DataGenerator.getRandomPassword;

public class AuthTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        Info registeredUser = DataGenerator.getUser("active");
        RequestToAPI.registration(registeredUser);
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $(".heading").shouldBe(ownText("Личный кабинет"));
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        Info notRegisteredUser = DataGenerator.getUser("active");
        RequestToAPI.registration(notRegisteredUser);
        $("[data-test-id='login'] input").setValue(getRandomLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id = 'error-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id = 'error-notification'] [class = 'notification__content']").should(text("Неверно указан логин или пароль"));
        $("[data-test-id = 'error-notification'] button").click();
        $("[data-test-id = 'error-notification']").should(hidden);
    }
    @Test
    void shouldGetErrorIfBlockedUser() {
        Info blockedUser = DataGenerator.getUser("blocked");
        RequestToAPI.registration(blockedUser);
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id = 'error-notification']").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id = 'error-notification'] [class = 'notification__content']").should(text("Пользователь заблокирован"));
        $("[data-test-id = 'error-notification'] button").click();
        $("[data-test-id = 'error-notification']").should(hidden);
    }

    @Test
    void shouldGetErrorIfWrongBlockerLogin() {
        Info wrongLogin = DataGenerator.getUser("blocked");
        RequestToAPI.registration(wrongLogin);
        $("[data-test-id='login'] input").setValue(getRandomLogin());
        $("[data-test-id='password'] input").setValue(wrongLogin.getPassword());
        $("[data-test-id='action-login']").click();
        $("[data-test-id = 'error-notification'] [class = 'notification__content']").should(text("Неверно указан логин или пароль"));
        $("[data-test-id = 'error-notification'] button").click();
        $("[data-test-id = 'error-notification']").should(hidden);
    }

    @Test
    void shouldGetErrorIfWrongPasswordAndBlockerUser() {
        Info wrongLogin = DataGenerator.getUser("blocked");
        RequestToAPI.registration(wrongLogin);
        $("[data-test-id='login'] input").setValue(wrongLogin.getLogin());
        $("[data-test-id='password'] input").setValue(getRandomLogin());
        $("[data-test-id='action-login']").click();
        $("[data-test-id = 'error-notification'] [class = 'notification__content']").should(text("Неверно указан логин или пароль"));
        $("[data-test-id = 'error-notification'] button").click();
        $("[data-test-id = 'error-notification']").should(hidden);
    }

}

