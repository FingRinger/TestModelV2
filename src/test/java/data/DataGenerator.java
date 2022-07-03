package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    public static Info getUser(String status) {
        return new Info(getRandomLogin(), getRandomPassword(), status);
    }

    public static String getRandomLogin() {
        Faker faker = new Faker();
        return faker.name().username();
    }

    public static String getRandomPassword() {
        Faker faker = new Faker();
        return faker.internet().password();

    }

}
