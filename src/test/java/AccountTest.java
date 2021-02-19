import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class AccountTest extends BaseTest{
    //обновленная версия без хардкода
    @Test
    void getAccountInfoTest() {
        given()
                .headers("Authorization", token)
//                .headers(headers)
                .when()
                .get("/account/{username}", username)
                .then()
                .statusCode(200);
    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .headers(headers)
                .log()
                .all()
                .when()
                .get("https://api.imgur.com/3/account/trosbalos")
                .prettyPeek()
                .then()
                .statusCode(200);
    }


    @Test
    void getAccountInfoWithoutToken() {
        when()
                .get("https://api.imgur.com/3/account/trosbalos")
                .then()
                .statusCode(401);
    }

    @Test
    void getAccountInfoVerifyUrlTest() {
        String url = given()
                .headers(headers)
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/trosbalos")
                //   .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .log()
                .status()
                .extract()
                .response()
                .jsonPath()
                .getString("data.url");
        assertThat(url, equalTo("trosbalos"));
    }

    @Test
    void getAccountInfoVerifyUrlInGivenPartTest() {
        given()
                .headers(headers)
                .log()
                .uri()
                .expect()
                .body("success", is(true))
                .body("data.url", is("trosbalos"))
                .when()
                .get("https://api.imgur.com/3/account/trosbalos")
                //   .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .log()
                .status();
    }


}
