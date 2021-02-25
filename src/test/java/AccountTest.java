import org.junit.jupiter.api.Test;


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
                .get(ACCOUNT_USERNAME, username)
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
                .get(API_3_ACC_TROS)
                .prettyPeek()
                .then()
                .statusCode(200);
    }


    @Test
    void getAccountInfoWithoutToken() {
        when()
                .get(API_3_ACC_TROS)
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
                .get(API_3_ACC_TROS)
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
        assertThat(url, equalTo(username));
    }

    @Test
    void getAccountInfoVerifyUrlInGivenPartTest() {
        given()
                .headers(headers)
                .log()
                .uri()
                .expect()
                .body("success", is(true))
                .body("data.url", is(username))
                .when()
                .get(API_3_ACC_TROS)
                //   .prettyPeek()
                .then()
                .statusCode(200)
                .contentType("application/json")
                .log()
                .status();
    }


}
